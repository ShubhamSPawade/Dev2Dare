# API Endpoint Testing & Feature Overview

## How to Test API Endpoints

You can test the API endpoints using tools like **curl**, **Postman**, or any REST client. Below are example requests for each major endpoint.

---

### **Authentication**

#### Register College
```bash
curl -X POST http://localhost:8081/register/college \
  -H 'Content-Type: application/json' \
  -d '{"name":"ABC College","email":"college@example.com","password":"pass123","description":"A top college"}'
```

#### Register Student
```bash
curl -X POST http://localhost:8081/register/student \
  -H 'Content-Type: application/json' \
  -d '{"name":"John Doe","email":"student@example.com","password":"pass123","collegeName":"ABC College"}'
```

#### Login
```bash
curl -X POST http://localhost:8081/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"student@example.com","password":"pass123","role":"STUDENT"}'
```

---

### **Student Endpoints**

#### Register for Event
```bash
curl -X POST http://localhost:8081/student/register-event?eventId=1 \
  -H 'Authorization: Bearer <JWT_TOKEN>'
```

#### Cancel Registration
```bash
curl -X DELETE http://localhost:8081/student/cancel-registration?eventId=1 \
  -H 'Authorization: Bearer <JWT_TOKEN>'
```

#### List All Events
```bash
curl -X GET http://localhost:8081/student/events \
  -H 'Authorization: Bearer <JWT_TOKEN>'
```

#### List My Events (with waitlist status)
```bash
curl -X GET http://localhost:8081/student/my-events \
  -H 'Authorization: Bearer <JWT_TOKEN>'
```

---

### **College Endpoints**

#### Create Event
```bash
curl -X POST http://localhost:8081/college/events \
  -H 'Authorization: Bearer <JWT_TOKEN>' \
  -H 'Content-Type: application/json' \
  -d '{"title":"Tech Fest","description":"Annual tech event","eventDateTime":"2024-07-01T10:00:00","location":"Auditorium","capacity":100}'
```

#### Update Event
```bash
curl -X PUT http://localhost:8081/college/events/1 \
  -H 'Authorization: Bearer <JWT_TOKEN>' \
  -H 'Content-Type: application/json' \
  -d '{"title":"Tech Fest Updated","description":"Updated desc","eventDateTime":"2024-07-01T10:00:00","location":"Main Hall","capacity":120}'
```

#### Delete Event
```bash
curl -X DELETE http://localhost:8081/college/events/1 \
  -H 'Authorization: Bearer <JWT_TOKEN>'
```

#### List My Events
```bash
curl -X GET http://localhost:8081/college/events \
  -H 'Authorization: Bearer <JWT_TOKEN>'
```

#### List Registered Students
```bash
curl -X GET http://localhost:8081/college/events/1/registrations \
  -H 'Authorization: Bearer <JWT_TOKEN>'
```

#### List Waitlisted Students
```bash
curl -X GET http://localhost:8081/college/events/1/waitlist \
  -H 'Authorization: Bearer <JWT_TOKEN>'
```

---

## Database Schema (Summary)

### **student**
- id (PK)
- name
- email (unique)
- password
- college_id (FK)
- role
- createdAt
- updatedAt

### **college**
- id (PK)
- name
- email (unique)
- password
- description
- role
- createdAt
- updatedAt

### **event**
- id (PK)
- title
- description
- eventDateTime
- location
- capacity
- createdBy (college_id, FK)
- status (UPCOMING, ONGOING, COMPLETED)
- createdAt
- updatedAt

### **student_event_registration**
- id (PK)
- student_id (FK)
- event_id (FK)
- registeredAt
- waitlisted (boolean)

---

## Feature List & Logic/Approach

### 1. **User Registration & Authentication**
- JWT-based login for both students and colleges. Registration endpoints create users and return a JWT.

### 2. **Event Creation/Management (College)**
- Colleges can create, update, delete, and list their own events. Each event has a capacity and status.

### 3. **Event Registration (Student)**
- Students can register for events. If the event is full, they are added to a waitlist. If a spot opens, the earliest waitlisted student is promoted.

### 4. **Event Participation & Waitlist**
- Students can view all events and their own registrations, including waitlist status. Colleges can view registered and waitlisted students for each event.

### 5. **Event Status**
- Events have a status (UPCOMING, ONGOING, COMPLETED) for lifecycle tracking.

### 6. **Email Notifications**
- Students are notified by email when a new event is created (can be extended for other notifications).

### 7. **Security**
- All endpoints are protected by JWT and role-based access control. Only authorized users can access/modify their data.

### 8. **Audit & Timestamps**
- All entities have createdAt/updatedAt fields for auditing. Registrations have a registeredAt timestamp.

---

**For more details, see the per-folder README files and the main project README.** 