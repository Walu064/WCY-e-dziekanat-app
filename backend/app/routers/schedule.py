from fastapi import APIRouter, HTTPException, Query, Depends
from sqlalchemy.orm import Session
from typing import List
from datetime import datetime, timedelta
from schemas import CourseCreate, CourseDB, ScheduleCreate, ScheduleDB, ScheduleBase
from database import Course, Schedule, get_db
from datetime import datetime

schedule_router = APIRouter()

@schedule_router.post('/create_course/', response_model=CourseDB)
def create_course(course: CourseCreate, db: Session = Depends(get_db)):
    db_course = Course(**course.dict())
    db.add(db_course)
    db.commit()
    db.refresh(db_course)
    return db_course

@schedule_router.get('/get_courses/', response_model=List[CourseDB])
def get_all_courses(db: Session = Depends(get_db)):
    courses = db.query(Course).all()
    return courses

@schedule_router.get('/get_course/{course_id}', response_model=CourseDB)
def get_course(course_id: int, db: Session = Depends(get_db)):
    course = db.query(Course).filter(Course.id == course_id).first()
    if course is None:
        raise HTTPException(status_code=404, detail="Kurs nie znaleziony")
    return course

@schedule_router.put('/update_course/{course_id}', response_model=CourseDB)
def update_course(course_id: int, course: CourseCreate, db: Session = Depends(get_db)):
    db_course = db.query(Course).filter(Course.id == course_id).first()
    if db_course is None:
        raise HTTPException(status_code=404, detail="Kurs nie znaleziony")

    for key, value in course.dict().items():
        setattr(db_course, key, value)

    db.commit()
    db.refresh(db_course)
    return db_course

@schedule_router.delete('/delete_course/{course_id}', status_code=204)
def delete_course(course_id: int, db: Session = Depends(get_db)):
    db_course = db.query(Course).filter(Course.id == course_id).first()
    if db_course is None:
        raise HTTPException(status_code=404, detail="Kurs nie znaleziony")

    db.delete(db_course)
    db.commit()
    return {"message": "Kurs usunięty pomyślnie"}

@schedule_router.post('/create_schedule/', response_model = ScheduleDB)
def create_schedule(schedule: ScheduleCreate, db: Session = Depends(get_db)):
    db_schedule = Schedule(**schedule.dict())
    db.add(db_schedule)
    db.commit()
    db.refresh(db_schedule)
    return db_schedule

@schedule_router.get('/get_schedule/by_id/{schedule_id}', response_model = ScheduleDB)
def get_schedule(schedule_id: int, db: Session = Depends(get_db)):
    db_schedule = db.query(Schedule).filter(Schedule.id == schedule_id).first()
    if db_schedule is None:
        raise HTTPException(status_code=404, detail="Plan zajęć nie znaleziony")
    return db_schedule

@schedule_router.get('/get_schedules/by_group/{dean_group}', response_model=List[ScheduleBase])
def get_schedules_by_dean_group(dean_group: str, db: Session = Depends(get_db)):
    schedules = db.query(Schedule).filter(Schedule.dean_group == dean_group).all()
    return [ScheduleBase.from_orm(schedule) for schedule in schedules]

@schedule_router.get('/today_schedules/by_group/{dean_group}', response_model=List[ScheduleBase])
def get_today_schedules_by_dean_group(dean_group: str, db: Session = Depends(get_db)):
    today = datetime.now().date()
    schedules = db.query(Schedule).filter(
        Schedule.dean_group == dean_group,
        Schedule.date_time >= today,
        Schedule.date_time < today + timedelta(days=1)
    ).all()
    return [ScheduleBase.from_orm(schedule) for schedule in schedules]

@schedule_router.get('/schedules/by_group/{dean_group}/on_date/', response_model=List[ScheduleBase])
def get_schedules_by_dean_group_and_date(dean_group: str, date_str: str = Query(...), db: Session = Depends(get_db)):
    try:
        date = datetime.strptime(date_str, "%Y-%m-%d").date()
    except ValueError:
        raise HTTPException(status_code=400, detail="Niepoprawny format daty, oczekiwany format: RRRR-MM-DD")

    start_of_day = datetime.combine(date, datetime.min.time())
    end_of_day = datetime.combine(date, datetime.max.time())

    schedules = db.query(Schedule).filter(
        Schedule.dean_group == dean_group,
        Schedule.date_time >= start_of_day,
        Schedule.date_time <= end_of_day
    ).all()

    return [ScheduleBase.from_orm(schedule) for schedule in schedules]

@schedule_router.put('/update_schedule/{schedule_id}', response_model=ScheduleDB)
def update_schedule(schedule_id: int, schedule: ScheduleCreate, db: Session = Depends(get_db)):
    db_schedule = db.query(Schedule).filter(Schedule.id == schedule_id).first()
    if db_schedule is None:
        raise HTTPException(status_code=404, detail="Plan zajęć nie znaleziony")

    for key, value in schedule.dict().items():
        setattr(db_schedule, key, value)

    db.commit()
    db.refresh(db_schedule)
    return db_schedule

@schedule_router.delete('/delete_schedule/{schedule_id}', status_code=204)
def delete_schedule(schedule_id: int, db: Session = Depends(get_db)):
    db_schedule = db.query(Schedule).filter(Schedule.id == schedule_id).first()
    if db_schedule is None:
        raise HTTPException(status_code=404, detail="Plan zajęć nie znaleziony")

    db.delete(db_schedule)
    db.commit()
    return {"message": "Plan zajęć usunięty pomyślnie"}