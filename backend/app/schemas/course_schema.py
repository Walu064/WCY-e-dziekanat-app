from pydantic import BaseModel

# Zmodyfikowane klasy Pydantic
class CourseBase(BaseModel):
    name: str
    lecturer: int
    type: str
    semester: str

class CourseCreate(CourseBase):
    pass

class CourseDB(CourseBase):
    id: int