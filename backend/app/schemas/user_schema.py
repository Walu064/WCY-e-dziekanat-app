from pydantic import BaseModel

class UserCreateSchema(BaseModel):
    album_number: str
    password: str
    first_name: str
    second_name: str
    dean_group: str
    email_address: str
    telephone: str

class UserLoginSchema(BaseModel):
    album_number: str
    password: str

class UserOut(BaseModel):
    id: int
    album_number: str
    first_name: str
    second_name: str
    dean_group: str
    email_address: str
    telephone: str

    class Config:
        from_attributes = True