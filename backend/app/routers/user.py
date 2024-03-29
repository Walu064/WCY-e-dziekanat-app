from fastapi import APIRouter, HTTPException, Body, Depends
from sqlalchemy.orm import Session
from typing import List
from schemas import UserCreateSchema, UserLoginSchema, UserOut
from database import User, get_db
from funcs import hash_password, verify_password

user_router = APIRouter()

@user_router.post("/register/")
def register_user(user: UserCreateSchema, db: Session = Depends(get_db)):
    db_user = db.query(User).filter(User.album_number == user.album_number).first()
    if db_user:
        raise HTTPException(status_code=400, detail="Użytkownik już istnieje")
    hashed_password = hash_password(user.password)
    db_user = User(album_number=user.album_number, hashed_password=hashed_password, first_name=user.first_name, second_name=user.second_name, dean_group=user.dean_group,email_address=user.email_address, telephone=user.telephone)
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return {"username": db_user.album_number}

@user_router.post("/login/")
def login_user(user: UserLoginSchema, db: Session = Depends(get_db)):
    db_user = db.query(User).filter(User.album_number == user.album_number).first()
    if not db_user:
        raise HTTPException(status_code=400, detail="Niepoprawna nazwa użytkownika")
    if not verify_password(user.password, db_user.hashed_password):
        raise HTTPException(status_code=400, detail="Niepoprawne hasło")
    return {"message": "Pomyślnie zalogowano"}

@user_router.get("/get_users/", response_model=List[UserOut])
def get_users(db : Session = Depends(get_db)):
    users = db.query(User).all()
    return users

@user_router.get("/user/{album_number}", response_model=UserOut)
def get_user_by_album_number(album_number: str, db: Session = Depends(get_db)):
    user = db.query(User).filter(User.album_number == album_number).first()
    if not user:
        raise HTTPException(status_code=404, detail="Użytkownik nie znaleziony")
    return user

@user_router.get("/users/by_group/{dean_group}", response_model=List[UserOut])
def get_users_by_dean_group(dean_group: str, db: Session = Depends(get_db)):
    users = db.query(User).filter(User.dean_group == dean_group).all()
    if not users:
        raise HTTPException(status_code=404, detail="Nie znaleziono studentów w tej grupie dziekańskiej")
    return users

@user_router.put("/user/{album_number}")
def update_user_phone_number(album_number: str, telephone: str = Body(...), db: Session = Depends(get_db)):
    db_user = db.query(User).filter(User.album_number == album_number).first()
    if not db_user:
        raise HTTPException(status_code=404, detail="Użytkownik nie znaleziony")

    db_user.telephone = telephone

    db.commit()
    db.refresh(db_user)
    return {"message": "Numer telefonu użytkownika zaktualizowany"}

@user_router.delete("/user/{album_number}")
def delete_user(album_number: str, db: Session = Depends(get_db)):
    db_user = db.query(User).filter(User.album_number == album_number).first()
    if not db_user:
        raise HTTPException(status_code=404, detail="Użytkownik nie znaleziony")

    db.delete(db_user)
    db.commit()
    return {"message": "Użytkownik usunięty"}
