# backend/main.py

from fastapi import FastAPI
from pydantic import BaseModel
import json
from pathlib import Path

app = FastAPI()

class Item(BaseModel):
    message: str

class User(BaseModel):
    username: str
    password: str
    first_name: str
    last_name: str
    department: str
    email: str

USER_DATA_FILE = Path("user_data.json")

# Ensure file exists with empty list initially
if not USER_DATA_FILE.exists():
    USER_DATA_FILE.write_text("[]")

@app.get("/")
def read_root():
    return {"message": "Hello FastAPI"}

@app.get("/get_users")
def get_users():
    # Load existing users
    try:
        with USER_DATA_FILE.open("r") as file:
            users = json.load(file)
    except json.JSONDecodeError:
        users = []
    except FileNotFoundError:
        users = []

    return {"users": users}

@app.post("/create_user")
def create_user(user: User):
    # TODO: check for duplicacies and incorrect formats
    # Load existing users
    try:
        with USER_DATA_FILE.open("r") as file:
            users = json.load(file)
    except json.JSONDecodeError:
        users = []
    except FileNotFoundError:
        users = []

    users.append(user.model_dump())

    # Save updated users back to file
    with USER_DATA_FILE.open("w") as file:
        json.dump(users, file, indent=2)

    return {
        "message": f"User {user.username} created successfully",
        "user": user
    }


@app.get("/{username}/info")
def get_user_info(username: str):
    # Load existing users
    try:
        with USER_DATA_FILE.open("r") as file:
            users = json.load(file)
    except json.JSONDecodeError:
        users = []
    except FileNotFoundError:
        users = []

    for user in users:
        if user["username"] == username:
            return {"user": user}

    return {"message": "User not found"}


@app.post("/{username}/change_password")
def change_password(username: str, new_password: str):
    # Load existing users
    try:
        with USER_DATA_FILE.open("r") as file:
            users = json.load(file)
    except json.JSONDecodeError:
        users = []
    except FileNotFoundError:
        users = []

    for user in users:
        if user["username"] == username:
            user["password"] = new_password
            # Save updated users back to file
            with USER_DATA_FILE.open("w") as file:
                json.dump(users, file, indent=2)
            return {"message": "Password changed successfully"}

    return {"message": "User not found"}


@app.delete("/delete/{username}")
def delete_user(username: str):
    # Load existing users
    try:
        with USER_DATA_FILE.open("r") as file:
            users = json.load(file)
    except json.JSONDecodeError:
        users = []
    except FileNotFoundError:
        users = []

    for i, user in enumerate(users):
        if user["username"] == username:
            del users[i]
            # Save updated users back to file
            with USER_DATA_FILE.open("w") as file:
                json.dump(users, file, indent=2)
            return {"message": "User deleted successfully"}

    return {"message": "User not found"}
