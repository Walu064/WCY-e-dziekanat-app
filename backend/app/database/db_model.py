from sqlalchemy import create_engine, Column, Integer, String, DateTime, ForeignKey, LargeBinary
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker, relationship

SQLALCHEMY_DATABASE_URL = "sqlite:///./test.db"

engine = create_engine(SQLALCHEMY_DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base = declarative_base()

class User(Base):
    __tablename__ = "users"
    id = Column(Integer, primary_key=True, index=True)
    album_number = Column(String, unique=True, index=True)
    hashed_password = Column(String)
    first_name = Column(String)
    second_name = Column(String)
    dean_group = Column(String)
    email_address = Column(String, unique=True)
    telephone = Column(String)

class Lecturer(Base):
    __tablename__ = 'lecturers'
    id = Column(Integer, primary_key=True)
    first_name = Column(String, nullable=False)
    last_name = Column(String, nullable=False)
    office = Column(String)
    telephone = Column(String)
    email_address = Column(String, unique=True)

class Course(Base):
    __tablename__ = 'courses'
    id = Column(Integer, primary_key=True)
    name = Column(String, nullable=False)
    lecturer = Column(Integer, ForeignKey('lecturers.id'), nullable=False)
    type = Column(String, nullable=False)
    semester = Column(String)
    lecturer_info = relationship("Lecturer")

class Schedule(Base):
    __tablename__ = 'schedules'
    id = Column(Integer, primary_key=True)
    course_id = Column(Integer, ForeignKey('courses.id'), nullable=False)
    date_time = Column(DateTime, nullable=False)
    classroom = Column(String, nullable=False)
    dean_group = Column(String, nullable=False)
    course = relationship("Course")

Base.metadata.create_all(bind=engine)
