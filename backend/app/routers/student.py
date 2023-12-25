from fastapi import APIRouter, HTTPException, Depends
from sqlalchemy.orm import Session
from typing import List
from schemas import StudentSchema, StudentCreateSchema
from database import Student, get_db, SessionLocal

student_router = APIRouter()

@student_router.post("/add_student", response_model=StudentSchema)
def add_student(student: StudentCreateSchema, db: Session = Depends(get_db)):
    db_student = Student(first_name=student.first_name, last_name=student.last_name, 
                         album_number=student.album_number, dean_group=student.dean_group)
    db.add(db_student)
    db.commit()
    db.refresh(db_student)
    return db_student

@student_router.get("/get_students", response_model=List[StudentSchema])
def get_students(db: SessionLocal = Depends(get_db)):
    students = db.query(Student).all()
    return students

@student_router.get("/get_student/{album_number}", response_model=StudentSchema)
def get_student_by_album_number(album_number: str, db: Session = Depends(get_db)):
    student = db.query(Student).filter(Student.album_number == album_number).first()
    if student is None:
        raise HTTPException(status_code=404, detail="Student not found")
    return student