from pydantic import BaseModel

class CourseBase(BaseModel):
    name: str
    lecturer: str
    type: str

    class Config:
        from_attributes = True

class CourseCreate(CourseBase):
    pass

class CourseDB(CourseBase):
    id: int

    class Config:
        from_attributes = True