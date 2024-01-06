from pydantic import BaseModel

# Zmodyfikowane klasy Pydantic
class CourseBase(BaseModel):
    name: str
    lecturer: int  # Typ zmieniony na int
    type: str

class CourseCreate(CourseBase):
    pass

class CourseDB(CourseBase):
    id: int