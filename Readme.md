# Finance Dashboard API

A role-based financial records management REST API built with Spring Boot, MongoDB, and Spring Security. Designed as a backend for a finance dashboard system where different users interact with financial data based on their assigned role.

## Live API
https://finance-dashboard-api-4cjc.onrender.com
> Note: Hosted on Render free tier. First request after inactivity may take 30-60 seconds to respond.
> All the endpoints have been tested on Postman.

## Tech Stack
- **Java 21** with **Spring Boot 3.5**
- **MongoDB** (Atlas in production, local in development)
- **Spring Security** with stateless **JWT authentication**
- **Maven** for dependency management
- **Docker** for containerized deployment

## Getting Started (Local Setup)

### Prerequisites
- Java 21+
- MongoDB running locally on port 27017
- Maven (or use the included `./mvnw` wrapper)

### Run Locally
```bash
git clone https://github.com/Sanket17052006/finance-dashboard-api.git
cd finance-dashboard-api
./mvnw spring-boot:run
```

Server starts on `http://localhost:8000`

### Default Admin (Auto-seeded on first boot)
Email:    `admin@financeapi.com`
Password: `Admin@123`

## Roles

| Role | Description |
|------|-------------|
| VIEWER | Can only view dashboard summary |
| ANALYST | Can view records and all dashboard analytics |
| ADMIN | Full access — manage records, users, and all data |

New users who sign up are assigned VIEWER by default. An Admin must upgrade their role.

## API Reference

### Authentication (Public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/signup` | Register new user (default role: VIEWER) |
| POST | `/api/auth/login` | Login and receive JWT token |

### User Management (Admin only)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | List all users (paginated) |
| PUT | `/api/users/{id}/role` | Change a user's role |
| PUT | `/api/users/{id}/status` | Activate or deactivate a user |

### Financial Records

| Method | Endpoint | Role Required | Description |
|--------|----------|---------------|-------------|
| POST | `/api/records` | ADMIN | Create a new record |
| GET | `/api/records` | ANALYST, ADMIN | List records with optional filters |
| GET | `/api/records/{id}` | ANALYST, ADMIN | Get a single record |
| PUT | `/api/records/{id}` | ADMIN | Update a record |
| DELETE | `/api/records/{id}` | ADMIN | Soft delete a record |

#### Record Filters (query params)
?type=INCOME|EXPENSE

?category=salary

?from=2026-01-01

?to=2026-03-31

?page=0&size=10

### Dashboard Analytics

| Method | Endpoint | Role Required | Description |
|--------|----------|---------------|-------------|
| GET | `/api/dashboard/summary` | ALL roles | Total income, expenses, net balance |
| GET | `/api/dashboard/breakdown` | ANALYST, ADMIN | Category-wise totals |
| GET | `/api/dashboard/trends` | ANALYST, ADMIN | Month-wise income vs expense (last 6 months) |

All dashboard endpoints use MongoDB aggregation pipelines — no application-level math.

## Authentication

All protected endpoints require a Bearer token in the Authorization header:
Authorization: Bearer <your_jwt_token>

Get a token by calling `POST /api/auth/login`.

## Request/Response Examples

### Login
```json
POST /api/auth/login
{
  "email": "admin@financeapi.com",
  "password": "Admin@123"
}

Response:
{
  "token": "eyJhbGci...",
  "email": "admin@financeapi.com",
  "role": "ADMIN"
}
```

### Create Record
```json
POST /api/records
Authorization: Bearer <token>
{
  "amount": 50000,
  "type": "INCOME",
  "category": "salary",
  "date": "2026-04-01",
  "notes": "April salary"
}
```

### Dashboard Summary
```json
GET /api/dashboard/summary
Authorization: Bearer <token>

Response:
{
  "totalIncome": 62000.0,
  "totalExpenses": 32000.0,
  "netBalance": 30000.0,
  "totalRecords": 4
}
```

## Error Responses

All errors return a consistent JSON structure:
```json
{
  "status": 403,
  "error": "Forbidden",
  "message": "You do not have permission to perform this action",
  "timestamp": "2026-04-03T14:22:00"
}
```

## Design Decisions & Assumptions

- **Default role is VIEWER** — new signups cannot access records until an Admin upgrades them. This is intentional for a finance system where data sensitivity matters.
- **Soft deletes** — records are never permanently deleted. A `isDeleted` flag is set to true. All queries automatically filter these out.
- **createdBy from JWT** — the identity of who created a record is always pulled from the authenticated token, never from the request body. This prevents impersonation.
- **Category normalization** — categories are stored and filtered in lowercase to prevent duplicates like "Rent" vs "rent".
- **MongoDB aggregation for dashboard** — all summary calculations (totals, trends, breakdowns) are performed inside MongoDB using aggregation pipelines, not in application code.
- **Date filter behavior** — MongoDB's `Between` operator is exclusive on both bounds by default. A `plusDays(1)` and `minusDays(1)` adjustment is applied to make filters inclusive. This is documented behavior of Spring Data MongoDB.
- **Trends timezone** — dashboard trends use MongoDB's date operators which process dates in UTC. Records created on the 1st of a month in IST (UTC+5:30) may appear in the previous month's trend due to timezone offset.

## Tradeoffs

- **MongoDB over relational DB** — chosen for its flexible document model and native aggregation framework, which makes the dashboard queries clean and efficient.
- **Swagger UI skipped** — springdoc-openapi has a compatibility issue with Spring Boot 3.5.x. API is fully documented in this README instead.
- **Free tier deployment** — Render free instances spin down after inactivity. First request may be slow. Atlas free tier has connection limits but is sufficient for assessment purposes.

## Project Structure
src/main/java/com/sanket/financedashboardapi/
├── config/          # Security, JWT, DataSeeder
├── controller/      # REST controllers
├── service/         # Business logic
├── repository/      # MongoDB repositories
├── model/           # MongoDB documents
├── dto/             # Request and response DTOs
├── exception/       # Custom exceptions and global handler
├── security/        # JWT filter and UserDetails
└── enums/           # Role, TransactionType, UserStatus
