# Plag Checker

## Project Overview
Plag Checker is a full stack Java web application that provides a simple interface to check for plagiarism. The application consists of a backend built with Spring Boot and a frontend developed using React. The application now includes instructor management functionality with a database layer.

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
│       │   │               ├── config
│       │   │               │   ├── WebConfig.java
│       │   │               ├── controller
│       │   │               │   ├── HelloController.java
│       │   │               │   └── InstructorController.java
│       │   │               ├── model
│       │   │               │   └── Instructor.java
│       │   │               ├── repository
│       │   │               │   └── InstructorRepository.java
│       │   │               └── service
│       │   │                   └── InstructorService.java
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

## Database Configuration

### H2 In-Memory Database
The application uses H2 database for development and testing purposes:
- **Type**: In-memory database (data is lost when application stops)
- **Database Name**: `testdb`
- **Username**: `sa`
- **Password**: `password`

### Accessing H2 Database Console
1. Ensure the backend application is running
2. Open your browser and navigate to: `http://localhost:8080/h2-console`
3. Use the following connection settings:
   - **JDBC URL**: `jdbc:h2:mem:testdb`
   - **Username**: `sa`
   - **Password**: `password`
4. Click "Connect" to access the database
5. You can view tables, run SQL queries, and inspect data

### Database Tables
The application automatically creates the following tables:
- **instructors**: Stores instructor information (id, username, password, firstName, lastName)

## API Endpoints

### Hello World
- **GET** `/api/hello` - Returns "Hello World" message

### Instructor Management
- **POST** `/api/instructors` - Create a new instructor
- **GET** `/api/instructors` - Get all instructors
- **GET** `/api/instructors/{id}` - Get instructor by ID
- **GET** `/api/instructors/username/{username}` - Get instructor by username

### Example API Usage

#### Create Instructor
```bash
curl -X POST http://localhost:8080/api/instructors \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

#### Get All Instructors
```bash
curl http://localhost:8080/api/instructors
```

#### Get Instructor by ID
```bash
curl http://localhost:8080/api/instructors/1
```

#### Get Instructor by Username
```bash
curl http://localhost:8080/api/instructors/username/john_doe
```

## Features
- Simple REST API that returns "Hello World" when accessed at the root URL
- Basic React application structure to display messages
- Instructor management with full CRUD operations
- In-memory H2 database for data persistence during runtime
- Web-based database console for development and debugging
- JPA/Hibernate integration for object-relational mapping

## Technology Stack

### Backend
- **Spring Boot 3.2.2** - Main framework
- **Spring Data JPA** - Data access layer
- **H2 Database** - In-memory database for development
- **Maven** - Dependency management and build tool
- **Java 17** - Programming language

### Frontend
- **React** - Frontend framework
- **Node.js** - JavaScript runtime
- **npm** - Package manager

## Development Notes
- The H2 database is configured to run in-memory, meaning all data will be lost when the application stops
- Database schema is automatically created/updated based on JPA entity classes
- SQL queries are logged to the console for debugging purposes
- H2 console is enabled for development convenience

## Contributing
Feel free to fork the repository and submit pull requests for any improvements or features.

## Troubleshooting

### Common Issues
1. **Port 8080 already in use**: Change the port in `application.properties` by setting `server.port=8081`
2. **Database connection issues**: Verify H2 dependency is included and application.properties is configured correctly
3. **Maven build fails**: Ensure Java 17 is installed and JAVA_HOME is set correctly

### Viewing Application Logs
The application logs will show:
- Server startup information
- SQL queries (if `spring.jpa.show-sql=true`)
- HTTP request information
- Error messages and stack traces