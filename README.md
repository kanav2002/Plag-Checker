# Plag Checker

## Project Overview
Plag Checker is a full stack Java web application that provides a simple interface to check for plagiarism. The application consists of a backend built with Spring Boot and a frontend developed using React.

## Project Structure
```
plag-checker
├── pom.xml
├── backend
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── example
│       │   │           └── plagchecker
│       │   │               ├── PlagCheckerApplication.java
│       │   │               └── controller
│       │   │                   └── HelloController.java
│       │   └── resources
│       │       └── application.properties
│       └── test
│           └── java
│               └── com
│                   └── example
│                       └── plagchecker
│                           └── PlagCheckerApplicationTests.java
├── frontend
│   ├── package.json
│   ├── public
│   │   └── index.html
│   └── src
│       ├── index.js
│       └── App.js
└── README.md
```

## Backend Setup
1. Navigate to the `backend` directory.
2. Build the backend using Maven:
   ```
   mvn clean install
   ```
3. Run the Spring Boot application:
   ```
   mvn spring-boot:run
   ```
4. The backend will be available at `http://localhost:8080`.

## Frontend Setup
1. Navigate to the `frontend` directory.
2. Install the dependencies:
   ```
   npm install
   ```
3. Start the React application:
   ```
   npm start
   ```
4. The frontend will be available at `http://localhost:3000`.

## Features
- Simple REST API that returns "Hello World" when accessed at the root URL.
- Basic React application structure to display the message.

## Contributing
Feel free to fork the repository and submit pull requests for any improvements or features.