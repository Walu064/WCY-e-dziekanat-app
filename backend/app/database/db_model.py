from sqlalchemy import create_engine, Column, Integer, String, ForeignKey
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

class Teacher(Base):
    __tablename__ = "teachers"
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, index=True)

class Course(Base):
    __tablename__ = "courses"
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, index=True)

class DeanGroup(Base):
    __tablename__ = "dean_groups"
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, index=True)

Base.metadata.create_all(bind=engine)
