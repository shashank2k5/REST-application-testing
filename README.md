# Student Management REST Application

A simple Student Management system built with **Spring Boot** that exposes a RESTful CRUD API for managing student records. It uses an in-memory **H2** database, **Spring Data JPA** for persistence, **Bean Validation** for input checks, and ships with a lightweight HTML/CSS/JavaScript front end for interacting with the API.

## Features

- Create, read, update, and delete student records (full CRUD)
- Search students by name (case-insensitive, partial match)
- Request/response DTOs to keep the API contract separate from the entity
- Input validation (`@NotBlank`, `@Email`, `@Min`)
- Centralized error handling with a consistent JSON error payload
- In-memory H2 database with the H2 web console enabled
- Bundled browser UI served from `src/main/resources/static`
- Postman workspace assets under `postman/`

## Tech Stack

| Layer          | Technology                                   |
| -------------- | -------------------------------------------- |
| Language       | Java 27                                      |
| Framework      | Spring Boot 4.2.0-RC2                        |
| Web            | Spring Web MVC                               |
| Persistence    | Spring Data JPA / Hibernate                  |
| Database       | H2 (in-memory)                               |
| Validation     | Jakarta Bean Validation                      |
| Build Tool     | Maven (with Maven Wrapper)                   |
| Front End      | HTML, CSS, Vanilla JavaScript (Fetch API)    |

## Project Structure

```
student-management/
├── pom.xml
├── mvnw / mvnw.cmd
├── postman/                         # Postman workspace assets
└── src/
    ├── main/
    │   ├── java/com/example/student_management/
    │   │   ├── StudentManagementApplication.java
    │   │   ├── controller/StudentController.java
    │   │   ├── service/StudentService.java
    │   │   ├── repository/StudentRepository.java
    │   │   ├── model/Student.java
    │   │   ├── dto/
    │   │   │   ├── StudentRequestDTO.java
    │   │   │   └── StudentResponseDTO.java
    │   │   └── exception/
    │   │       ├── StudentNotFoundException.java
    │   │       └── GlobalExceptionHandler.java
    │   └── resources/
    │       ├── application.properties
    │       └── static/
    │           ├── index.html
    │           ├── style.css
    │           └── script.js
    └── test/java/com/example/student_management/
        └── StudentManagementApplicationTests.java
```

## Prerequisites

- **JDK 27** or newer
- No local database required (H2 runs in-memory)

The Maven Wrapper is included, so a separate Maven install is optional.

## Getting Started

### Run the application

```bash
# macOS / Linux
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

The app starts on **http://localhost:8080**.

### Build and run the JAR

```bash
./mvnw clean package
java -jar target/student-management-0.0.1-SNAPSHOT.jar
```

### Run tests

```bash
./mvnw test
```

### Web UI

Open http://localhost:8080 in a browser to use the bundled front end, which supports adding, editing, deleting, and listing students.

### H2 Console

The in-memory database console is available at http://localhost:8080/h2-console.

| Setting  | Value                            |
| -------- | -------------------------------- |
| JDBC URL | `jdbc:h2:mem:studentdb`          |
| Username | `sa`                             |
| Password | *(empty)*                        |

> The database is in-memory, so data is reset every time the application restarts.

## API Reference

**Base URL:** `http://localhost:8080/api/students`

| Method   | Endpoint                | Description                     | Success Status |
| -------- | ----------------------- | ------------------------------- | -------------- |
| `POST`   | `/api/students`         | Create a new student            | `201 Created`  |
| `GET`    | `/api/students`         | List all students               | `200 OK`       |
| `GET`    | `/api/students/{id}`    | Get a student by ID             | `200 OK`       |
| `PUT`    | `/api/students/{id}`    | Update an existing student      | `200 OK`       |
| `DELETE` | `/api/students/{id}`    | Delete a student                | `204 No Content` |
| `GET`    | `/api/students/search?name=` | Search students by name    | `200 OK`       |

### Student Fields

| Field        | Type   | Constraints                          |
| ------------ | ------ | ------------------------------------ |
| `id`         | Long   | Auto-generated (read-only)           |
| `name`       | String | Required, not blank                  |
| `email`      | String | Required, valid email, unique        |
| `department` | String | Required, not blank                  |
| `age`        | int    | Required, minimum `1`                |

### Examples

#### Create a student

```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
        "name": "Jane Doe",
        "email": "jane.doe@example.com",
        "department": "Computer Science",
        "age": 20
      }'
```

**Response `201 Created`:**

```json
{
  "id": 1,
  "name": "Jane Doe",
  "email": "jane.doe@example.com",
  "department": "Computer Science",
  "age": 20
}
```

#### Get all students

```bash
curl http://localhost:8080/api/students
```

```json
[
  {
    "id": 1,
    "name": "Jane Doe",
    "email": "jane.doe@example.com",
    "department": "Computer Science",
    "age": 20
  }
]
```

#### Get a student by ID

```bash
curl http://localhost:8080/api/students/1
```

#### Update a student

```bash
curl -X PUT http://localhost:8080/api/students/1 \
  -H "Content-Type: application/json" \
  -d '{
        "name": "Jane Smith",
        "email": "jane.smith@example.com",
        "department": "Mathematics",
        "age": 21
      }'
```

#### Delete a student

```bash
curl -X DELETE http://localhost:8080/api/students/1
```

Returns `204 No Content` on success.

#### Search students by name

```bash
curl "http://localhost:8080/api/students/search?name=jane"
```

Returns an array of students whose name contains the query (case-insensitive).

## Validation

Incoming request bodies are validated before reaching the service layer. Invalid requests are rejected automatically by Spring with a `400 Bad Request` response.

## Error Handling

A `GlobalExceptionHandler` maps `StudentNotFoundException` to a structured `404 Not Found` response:

```json
{
  "timestamp": "2026-11-11T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Student with ID 99 not found"
}
```

## Configuration

Key settings in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:studentdb
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
server.port=8080
```

## Postman

The `postman/` directory contains workspace assets for testing the API in Postman. Import the workspace to get a pre-configured environment for the endpoints above.
