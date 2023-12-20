from fastapi import Depends, HTTPException, APIRouter
from sqlalchemy.orm import Session
from schemas import UserSchema
from models import User
from funcs import get_db, verify_password

login_router = APIRouter()

@login_router.post("/login/")
def login(user: UserSchema, db: Session = Depends(get_db)):
    db_user = db.query(User).filter(User.username == user.username).first()
    if not db_user:
        raise HTTPException(status_code=400, detail="Niepoprawna nazwa użytkownika")
    if not verify_password(user.password, db_user.hashed_password):
        raise HTTPException(status_code=400, detail="Niepoprawne hasło")
    return {"message": "Pomyślnie zalogowano"}
