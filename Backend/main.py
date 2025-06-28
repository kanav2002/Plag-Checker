# backend/main.py

from typing import List
from fastapi import FastAPI, Depends
from pydantic import BaseModel
import json
from pathlib import Path
from sqlalchemy.orm import Session
from fastapi import HTTPException

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
class ProfessorName(BaseModel):
    name: str

    class Config:
        orm_mode = True

# Endpoint to get all professor names
@app.get("/professors/", response_model=List[ProfessorName])
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


# # @app.post("{username}/create_course")