from fastapi import Depends, HTTPException, APIRouter
from sqlalchemy.orm import Session
from schemas import UserSchema
from models import User
from funcs import get_db, hash_password

register_router = APIRouter()

@register_router.post("/register/")
def register(user: UserSchema, db: Session = Depends(get_db)):
    db_user = db.query(User).filter(User.username == user.username).first()
    if db_user:
        raise HTTPException(status_code=400, detail="Użytkownik już istnieje")
    hashed_password = hash_password(user.password)
    db_user = User(username=user.username, hashed_password=hashed_password)
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return {"username": db_user.username}