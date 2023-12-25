from fastapi import APIRouter, HTTPException, Depends
from sqlalchemy.orm import Session
from typing import List
from schemas import CourseCreate, CourseDB, ScheduleCreate, ScheduleDB, ScheduleBase
from database import Course, Schedule, get_db

schedule_router = APIRouter()

@schedule_router.post('/courses', response_model = CourseDB)
def create_course(course: CourseCreate, db: Session = Depends(get_db)):
    db_course = Course(**course.dict())
    db.add(db_course)
    db.commit()
    db.refresh(db_course)
    return db_course

@schedule_router.post('/schedules', response_model = ScheduleDB)
def create_schedule(schedule: ScheduleCreate, db: Session = Depends(get_db)):
    db_schedule = Schedule(**schedule.dict())
    db.add(db_schedule)
    db.commit()
    db.refresh(db_schedule)
    return db_schedule

@schedule_router.get('/schedules/{schedule_id}', response_model = ScheduleDB)
def get_schedule(schedule_id: int, db: Session = Depends(get_db)):
    db_schedule = db.query(Schedule).filter(Schedule.id == schedule_id).first()
    if db_schedule is None:
        raise HTTPException(status_code=404, detail="Plan zajęć nie znaleziony")
    return db_schedule

@schedule_router.get('/schedules/by_group/{dean_group}', response_model=List[ScheduleBase])
def get_schedules_by_dean_group(dean_group: str, db: Session = Depends(get_db)):
    schedules = db.query(Schedule).filter(Schedule.dean_group == dean_group).all()
    return [ScheduleBase.from_orm(schedule) for schedule in schedules]

