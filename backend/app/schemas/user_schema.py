from pydantic import BaseModel

class UserLoginSchema(BaseModel):
    album_number: str
    password: str

class UserOut(BaseModel):
    id: int
    album_number: str

    class Config:
        from_attributes = True