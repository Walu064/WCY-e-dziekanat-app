from fastapi import APIRouter, HTTPException, Depends
from sqlalchemy.orm import Session
from typing import List
from schemas import UserCreateSchema, UserLoginSchema, UserOut
from database import User, get_db
from funcs import hash_password, verify_password

user_router = APIRouter()

@user_router.post("/register/")
def register(user: UserCreateSchema, db: Session = Depends(get_db)):
    db_user = db.query(User).filter(User.album_number == user.album_number).first()
    if db_user:
        raise HTTPException(status_code=400, detail="Użytkownik już istnieje")
    hashed_password = hash_password(user.password)
    db_user = User(album_number=user.album_number, hashed_password=hashed_password, first_name = user.first_name, second_name = user.second_name, dean_group = user.dean_group)
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return {"username": db_user.album_number}

@user_router.post("/login/")
def login(user: UserLoginSchema, db: Session = Depends(get_db)):
    db_user = db.query(User).filter(User.album_number == user.album_number).first()
    if not db_user:
        raise HTTPException(status_code=400, detail="Niepoprawna nazwa użytkownika")
    if not verify_password(user.password, db_user.hashed_password):
        raise HTTPException(status_code=400, detail="Niepoprawne hasło")
    return {"message": "Pomyślnie zalogowano"}

@user_router.get("/users/", response_model=List[UserOut])
def get_users(db : Session = Depends(get_db)):
    users = db.query(User).all()
    return users