# backend/main.py

from typing import List
import zipfile
from fastapi import FastAPI, UploadFile, File, HTTPException, Depends
from pydantic import BaseModel
import json
from pathlib import Path
from sqlalchemy.orm import Session
import os

# from models import models
import models
from database import SessionLocal, engine

models.Base.metadata.create_all(bind=engine)

app = FastAPI()

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


class ProfessorCreate(BaseModel):
    username: str
    password: str
    name: str
    email: str

@app.post("/professors/")
def create_professor(prof: ProfessorCreate, db: Session = Depends(get_db)):
    db_prof = models.Professor(
        username=prof.username,
        password=prof.password,
        name=prof.name,
        email=prof.email
    )
    db.add(db_prof)
    db.commit()
    db.refresh(db_prof)
    return {"message": "Professor created", "professor_id": db_prof.id}

# Response schema
class ProfessorSummary(BaseModel):
    id: int
    username: str
    name: str

    class Config:
        orm_mode = True

# Endpoint to get all professor names
@app.get("/professors/", response_model=List[ProfessorSummary])
def get_all_professors(db: Session = Depends(get_db)):
    professors = db.query(models.Professor).all()
    return professors

class ProfessorInfo(BaseModel):
    id: int
    username: str
    name: str
    email: str

    class Config:
        orm_mode = True


@app.get("/professors/{username}", response_model=ProfessorInfo)
def get_professor(username: str, db: Session = Depends(get_db)):
    professor = db.query(models.Professor).filter(models.Professor.username == username).first()
    if professor is None:
        raise HTTPException(status_code=404, detail="Professor not found")
    return professor


@app.delete("/professors/{username}")
def delete_professor(username: str, db: Session = Depends(get_db)):
    professor = db.query(models.Professor).filter(models.Professor.username == username).first()
    if professor is None:
        raise HTTPException(status_code=404, detail="Professor not found")
    
    db.delete(professor)
    db.commit()
    return {"message": f"Professor '{username}' deleted successfully."}


class CourseCreate(BaseModel):
    code: str
    name: str
    professor_id: int  # ID of the professor teaching it


@app.post("/courses/")
def create_course(course: CourseCreate, db: Session = Depends(get_db)):
    # Check if professor exists
    professor = db.query(models.Professor).filter(models.Professor.id == course.professor_id).first()
    if not professor:
        raise HTTPException(status_code=404, detail="Professor not found")

    db_course = models.Course(
        code=course.code,
        name=course.name,
        professor_id=course.professor_id
    )
    db.add(db_course)
    db.commit()
    db.refresh(db_course)
    return {"message": "Course created", "course_id": db_course.id}


class ProfessorBasic(BaseModel):
    id: int
    name: str
    username: str

    class Config:
        orm_mode = True

@app.get("/courses/{course_id}/professor", response_model=ProfessorBasic)
def get_course_professor(course_id: int, db: Session = Depends(get_db)):
    course = db.query(models.Course).filter(models.Course.code == course_id).first()
    if not course:
        raise HTTPException(status_code=404, detail="Course not found")

    return course.professor


class CourseInfo(BaseModel):
    id: int
    code: str
    name: str

    class Config:
        orm_mode = True

@app.get("/professors/{professor_id}/courses", response_model=List[CourseInfo])
def get_courses_by_professor(professor_id: int, db: Session = Depends(get_db)):
    professor = db.query(models.Professor).filter(models.Professor.id == professor_id).first()
    if not professor:
        raise HTTPException(status_code=404, detail="Professor not found")

    return professor.courses


# Response schema
class CourseWithProfessor(BaseModel):
    id: int
    code: str
    name: str
    professor_name: str

    class Config:
        orm_mode = True


@app.get("/courses/", response_model=List[CourseWithProfessor])
def get_all_courses(db: Session = Depends(get_db)):
    courses = db.query(models.Course).all()
    response = []
    for course in courses:
        response.append(
            CourseWithProfessor(
                id=course.id,
                code=course.code,
                name=course.name,
                professor_name=course.professor.name if course.professor else "Unknown"
            )
        )
    return response


class ExamCreate(BaseModel):
    name: str

@app.post("/professors/{prof_id}/courses/{course_id}/exams")
def create_exam_under_course(
    prof_id: int,
    course_id: str,
    exam: ExamCreate,
    db: Session = Depends(get_db)
):
    course = db.query(models.Course).filter(
        models.Course.code == course_id,
        models.Course.professor_id == prof_id
    ).first()

    if not course:
        raise HTTPException(status_code=404, detail="Course not found or professor does not teach this course")

    db_exam = models.Exam(name=exam.name, course_id=course.id)
    db.add(db_exam)
    db.commit()
    db.refresh(db_exam)
    return {"message": "Exam created", "exam_id": db_exam.id}


class ExamInfo(BaseModel):
    id: int
    name: str

    class Config:
        orm_mode = True

@app.get("/professors/{prof_id}/courses/{course_id}/exams", response_model=List[ExamInfo])
def get_exams_for_course(
    prof_id: int,
    course_id: str,
    db: Session = Depends(get_db)
):
    course = db.query(models.Course).filter(
        models.Course.code == course_id,
        models.Course.professor_id == prof_id
    ).first()

    if not course:
        raise HTTPException(status_code=404, detail="Course not found or professor not authorized")

    return course.exams


@app.delete("/professors/{prof_id}/courses/{course_id}/exams")
def delete_all_exams_under_course(
    prof_id: int,
    course_id: str,  # Assuming this is the course *code*
    db: Session = Depends(get_db)
):
    # Verify course belongs to professor
    course = db.query(models.Course).filter(
        models.Course.code == course_id,
        models.Course.professor_id == prof_id
    ).first()

    if not course:
        raise HTTPException(status_code=404, detail="Course not found or unauthorized")

    # Delete all exams for the course
    deleted_count = db.query(models.Exam).filter(models.Exam.course_id == course.id).delete()
    db.commit()

    return {"message": f"Deleted {deleted_count} exam(s) under course '{course_id}'"}


@app.post("/professors/{prof_id}/courses/{course_code}/exams/{exam_id}/upload")
def upload_zip(
    prof_id: int,
    course_code: str,
    exam_id: int,
    file: UploadFile = File(...),
    db: Session = Depends(get_db)
):
    exam = (
        db.query(models.Exam)
        .join(models.Course)
        .filter(
            models.Exam.id == exam_id,
            models.Course.code == course_code,
            models.Course.professor_id == prof_id
        )
        .first()
    )

    if not exam:
        raise HTTPException(status_code=404, detail="Exam or ownership not valid")

    upload_dir = f"uploads/p{prof_id}/c{course_code}/e{exam_id}"
    os.makedirs(upload_dir, exist_ok=True)

    zip_path = f"{upload_dir}/upload.zip"
    with open(zip_path, "wb") as buffer:
        buffer.write(file.file.read())

    with zipfile.ZipFile(zip_path, 'r') as zip_ref:
        zip_ref.extractall(f"{upload_dir}/unzipped")

    return {"message": "ZIP uploaded and extracted"}


@app.get("/professors/{prof_id}/courses/{course_code}/exams/{exam_id}/files")
def list_uploaded_files(
    prof_id: int,
    course_code: str,
    exam_id: int
):
    unzip_dir = Path(f"uploads/p{prof_id}/c{course_code}/e{exam_id}/unzipped")

    if not unzip_dir.exists() or not unzip_dir.is_dir():
        raise HTTPException(status_code=404, detail="No extracted files found")

    # List all files recursively
    file_list = [
        str(path.relative_to(unzip_dir))  # Show paths relative to unzip_dir
        for path in unzip_dir.rglob("*")
        if path.is_file()
    ]

    return {"files": file_list}
