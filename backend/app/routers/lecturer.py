from fastapi import APIRouter, HTTPException, Depends
from sqlalchemy.orm import Session
from schemas import LecturerCreate, LecturerUpdate
from database import Lecturer, get_db

lecturer_router = APIRouter()

@lecturer_router.post("/create_lecturer/")
def create_lecturer(lecturer: LecturerCreate, db: Session = Depends(get_db)):
    db_lecturer = Lecturer(**lecturer.dict())
    db.add(db_lecturer)
    db.commit()
    db.refresh(db_lecturer)
    return db_lecturer

@lecturer_router.get("/get_lecturers/")
def read_all_lecturers(db: Session = Depends(get_db)):
    db_lecturers = db.query(Lecturer).all()
    return db_lecturers

@lecturer_router.get("/get_lecturer_by_id/{lecturer_id}")
def read_lecturer(lecturer_id: int, db: Session = Depends(get_db)):
    db_lecturer = db.query(Lecturer).filter(Lecturer.id == lecturer_id).first()
    if db_lecturer is None:
        raise HTTPException(status_code=404, detail="Lecturer not found")
    return db_lecturer

@lecturer_router.put("/update_lecturer/{lecturer_id}")
def update_lecturer(lecturer_id: int, lecturer: LecturerUpdate, db: Session = Depends(get_db)):
    db_lecturer = db.query(Lecturer).filter(Lecturer.id == lecturer_id).first()
    if db_lecturer is None:
        raise HTTPException(status_code=404, detail="Lecturer not found")
    for var, value in vars(lecturer).items():
        if value is not None:
            setattr(db_lecturer, var, value)
    db.commit()
    return db_lecturer

@lecturer_router.delete("/delete_lecturer/{lecturer_id}")
def delete_lecturer(lecturer_id: int, db: Session = Depends(get_db)):
    db_lecturer = db.query(Lecturer).filter(Lecturer.id == lecturer_id).first()
    if db_lecturer is None:
        raise HTTPException(status_code=404, detail="Lecturer not found")
    db.delete(db_lecturer)
    db.commit()
    return {"message": "Lecturer deleted successfully"}
