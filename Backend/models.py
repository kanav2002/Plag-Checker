from sqlalchemy import Column, Integer, String, ForeignKey
from sqlalchemy.orm import relationship
from database import Base

class Professor(Base):
    __tablename__ = "professors"
    id = Column(Integer, primary_key=True, index=True)
    username = Column(String, unique=True, index=True)
    password = Column(String, nullable=False, unique=True)
    name = Column(String, nullable=False)
    email = Column(String, nullable=False, unique=True)

    courses = relationship("Course", back_populates="professor", cascade="all, delete-orphan")

class Course(Base):
    __tablename__ = "courses"
    id = Column(Integer, primary_key=True, index=True)
    code = Column(String, unique=True, index=True)
    name = Column(String, nullable=False)
    professor_id = Column(Integer, ForeignKey("professors.id"))

    professor = relationship("Professor", back_populates="courses")
    exams = relationship("Exam", back_populates="course", cascade="all, delete-orphan")

class Exam(Base):
    __tablename__ = "exams"
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, nullable=False)
    course_id = Column(Integer, ForeignKey("courses.id"))

    course = relationship("Course", back_populates="exams")
    # TODO: STATS (plag)