from typing import List
from fastapi import Depends, APIRouter
from schemas import UserOut
from models import User
from funcs import get_db
from sqlalchemy.orm import Session

get_users_router = APIRouter()

@get_users_router.get("/users/", response_model=List[UserOut])
def read_users(db : Session = Depends(get_db)):
    users = db.query(User).all()
    return users