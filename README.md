# College Event Management Platform

A Spring Boot-based backend for managing college events, registrations, and authentication for students and colleges.

## Features
- Student and College registration & login
- College event creation, update, deletion
- Student event registration
- View events and registrations
- JWT-based authentication (structure ready)
- H2 in-memory database for development

## Tech Stack
- Java 11+
- Spring Boot
- Spring Data JPA
- Spring Security (configurable)
- H2 Database (for development)
- Maven

## Project Structure
```
backend/dev2dare/
├── src/main/java/com/shourya/dev2dare/
│   ├── controller/         # REST controllers (Auth, College, Student, Event)
│   ├── service/            # Service layer (skeleton)
│   ├── repository/         # Spring Data JPA repositories
│   ├── model/              # JPA entities (Student, College, Event, Registration)
│   ├── dto/                # Data Transfer Objects (DTOs)
│   ├── config/             # Security and JWT config (skeleton)
│   └── utils/              # Utility classes
├── src/main/resources/
│   └── application.properties # Spring Boot config
└── pom.xml
```

## Key Endpoints

### AuthController
- `POST /auth/student/register` — Register a student
- `POST /auth/college/register` — Register a college
- `POST /auth/login` — Login (student/college)

### CollegeController
- `POST /college/events` — Create event
- `PUT /college/events/{id}` — Update event
- `DELETE /college/events/{id}` — Delete event
- `GET /college/events/{id}/registrations` — View event registrations

### StudentController
- `GET /student/events` — View registered events
- `POST /events/{id}/register` — Register for event

### EventController
- `GET /events` — List all events
- `GET /events/{id}` — Event details

## Main Entities & DTOs
- **Student**: id, name, email, password, collegeName, createdAt
- **College**: id, name, email, password, description, createdAt
- **Event**: id, title, description, category, isPaid, startDate, endDate, college, createdAt
- **Registration**: id, student, event, registrationDate, status
- **DTOs**: StudentSignupRequest, CollegeSignupRequest, LoginRequest, EventRequest, EventResponse

## Getting Started

### Prerequisites
- Java 11 or higher
- Maven

### Setup
1. Clone the repository:
   ```sh
   git clone <repo-url>
   cd DevToDare/backend/dev2dare
   ```
2. Build the project:
   ```sh
   mvn clean install
   ```
3. Run the application:
   ```sh
   mvn spring-boot:run
   ```
4. Access H2 Console (for dev):
   - URL: `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:testdb`

## Configuration
- All configs are in `src/main/resources/application.properties`.
- H2 in-memory DB is enabled for development.

## Notes
- The project currently contains only base structure and method signatures (no business logic).
- Extend the service and repository layers to implement business logic as needed.
- Security config is present but set to permit all for development.

## License
MIT 