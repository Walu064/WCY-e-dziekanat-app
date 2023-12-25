from pydantic import BaseModel
from datetime import datetime
from database import Schedule
from .course_schema import CourseDB

class ScheduleBase(BaseModel):
    course_id: int
    date_time: datetime
    classroom: str
    dean_group: str

    class Config:
        from_attributes = True
        

class ScheduleCreate(ScheduleBase):
    pass

class ScheduleDB(ScheduleBase):
    id: int
    course: CourseDB

    class Config:
        from_attributes = True