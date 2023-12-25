from pydantic import BaseModel

class StudentSchema(BaseModel):
    first_name: str
    last_name: str
    album_number: str
    dean_group: str

class StudentCreateSchema(StudentSchema):
    pass