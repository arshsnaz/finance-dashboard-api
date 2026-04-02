# Finance Dashboard Backend

## 1. Project Overview
Finance Dashboard Backend is a RESTful backend service built for Zorvyn's assessment submission to power a personal and organizational finance dashboard. It centralizes authentication, user management, transaction management, and dashboard analytics in one secure Spring Boot application. The system solves the need for structured financial tracking by providing APIs for recording income and expenses, filtering records, and generating summary insights for decision-making. It enforces role-based access control with three roles: VIEWER, ANALYST, and ADMIN, so each user can access only what they are authorized to use. In addition to full transaction lifecycle management with soft delete, it provides analytics endpoints for totals, category-wise breakdowns, monthly trends, and recent activity.

## 2. Tech Stack
| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Core programming language for backend development |
| Spring Boot | 3.2.0 | Application framework for building REST APIs and service configuration |
| Spring Security | 6.x (via Spring Boot 3.2.0) | Authentication, authorization, JWT filter chain, and method-level access control |
| MySQL | 8.x | Relational database for persistent storage of users and transactions |
| JWT (jjwt) | 0.11.5 | Stateless token-based authentication and request authorization |
| Lombok | Latest compatible | Reduces boilerplate for DTOs and entities (getters, setters, builders, constructors) |
| Maven | 3.9+ | Dependency management and build lifecycle |

## 3. Architecture Overview
The project follows a layered architecture where HTTP requests enter the Controller layer, business rules are handled in the Service layer, persistence is managed through the Repository layer, and data is stored in MySQL. This separation keeps responsibilities clear, improves maintainability, and makes testing and future extension simpler.

```java
src/main/java/com/zorvyn/finance/
├── config/          -> SecurityConfig, DataSeeder
├── controller/      -> AuthController, TransactionController,
│                       UserController, DashboardController
├── dto/
│   ├── request/     -> RegisterRequest, LoginRequest, TransactionRequest
│   └── response/    -> AuthResponse, ApiResponse, TransactionResponse,
│                       DashboardSummaryResponse, CategorySummaryResponse,
│                       MonthlyTrendResponse, RecentTransactionResponse
├── enums/           -> Role, TransactionType
├── exception/       -> GlobalExceptionHandler, ResourceNotFoundException
├── model/           -> User, Transaction
├── repository/      -> UserRepository, TransactionRepository
├── security/        -> JwtUtil, JwtAuthFilter, CustomUserDetailsService
└── service/         -> AuthService, TransactionService,
                        UserService, DashboardService
```

## 4. Database Setup
1. Make sure MySQL is installed and running on port 3306.
2. Open your MySQL client and run the following command:
```java
CREATE DATABASE finance_db;
```
3. Start the application. Because Hibernate is configured with `ddl-auto=update`, all required tables are created and updated automatically on first run.
4. No manual SQL migration scripts are required for this assessment submission.

## 5. Configuration
Use the following application configuration in `src/main/resources/application.properties`:

```java
server.port=8080
spring.application.name=finance-dashboard

spring.datasource.url=jdbc:mysql://localhost:3306/finance_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true

app.jwt.secret=ZorvynFinanceSecretKey2024SuperSecureLongStringForHMACSHA256
app.jwt.expiration=86400000
```

## 6. How to Run
1. Clone the repository and move into the project directory.
```java
git clone https://github.com/zorvyn/finance-dashboard.git
cd finance-dashboard
```
2. Create the database using the Database Setup section.
3. Run the application.
```java
mvn spring-boot:run
```
4. The service starts at:
```java
http://localhost:8080
```
5. On first successful startup, default users are seeded automatically. Use them to test role-based access.

## 7. Default Users (Auto-seeded on Startup)
| Name | Email | Password | Role |
|---|---|---|---|
| Super Admin | admin@zorvyn.com | Admin@1234 | ADMIN |
| Test Viewer | viewer@zorvyn.com | Viewer@1234 | VIEWER |
| Test Analyst | analyst@zorvyn.com | Analyst@1234 | ANALYST |

These users are created automatically when the application starts for the first time. Use the Admin credentials to get a JWT token and test all endpoints, including admin-only operations.

## 8. Role Permissions Matrix
| Action | VIEWER | ANALYST | ADMIN |
|-------------------------------|--------|---------|-------|
| Login / Register | ✅ | ✅ | ✅ |
| View Transactions | ✅ | ✅ | ✅ |
| Filter Transactions | ✅ | ✅ | ✅ |
| View Dashboard Summary | ✅ | ✅ | ✅ |
| View Category Breakdown | ✅ | ✅ | ✅ |
| View Monthly Trends | ✅ | ✅ | ✅ |
| Create Transaction | ❌ | ❌ | ✅ |
| Update Transaction | ❌ | ❌ | ✅ |
| Delete Transaction (soft) | ❌ | ❌ | ✅ |
| View All Users | ❌ | ❌ | ✅ |
| Update User Role | ❌ | ❌ | ✅ |
| Update User Status | ❌ | ❌ | ✅ |

## 9. API Documentation

### 9.1 Authentication APIs (no token required)
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | /api/auth/register | Public | Register new user |
| POST | /api/auth/login | Public | Login and receive JWT token |

### 9.2 Transaction APIs (token required)
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | /api/transactions | ADMIN | Create transaction |
| GET | /api/transactions | All | Get all (with optional filters) |
| GET | /api/transactions?type=INCOME | All | Filter by type |
| GET | /api/transactions?category=Food | All | Filter by category |
| GET | /api/transactions?startDate=2024-01-01&endDate=2024-12-31 | All | Filter by date range |
| GET | /api/transactions/paged?page=0&size=10 | All | Paginated results |
| GET | /api/transactions/{id} | All | Get single transaction |
| PUT | /api/transactions/{id} | ADMIN | Update transaction |
| DELETE | /api/transactions/{id} | ADMIN | Soft delete transaction |

### 9.3 Dashboard APIs (token required)
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | /api/dashboard/summary | All | Full summary with all analytics |
| GET | /api/dashboard/summary/range | All | Summary filtered by date range |
| GET | /api/dashboard/categories | All | Category-wise income/expense totals |
| GET | /api/dashboard/trends/monthly | All | Month-wise income/expense breakdown |
| GET | /api/dashboard/recent | All | Recent transactions (default 10) |
| GET | /api/dashboard/recent?limit=5 | All | Recent transactions with custom limit |

### 9.4 User Management APIs (token required, ADMIN only)
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | /api/users | ADMIN | Get all users |
| PUT | /api/users/{id}/role?role=ANALYST | ADMIN | Update user role |
| PUT | /api/users/{id}/status?isActive=false | ADMIN | Activate or deactivate user |

## 10. Request and Response Examples

### Register
Request:
```json
{
  "method": "POST",
  "url": "/api/auth/register",
  "headers": {
    "Content-Type": "application/json"
  },
  "body": {
    "name": "John Doe",
    "email": "john@example.com",
    "password": "pass123",
    "role": "VIEWER"
  }
}
```

Response:
```json
{
  "status": 200,
  "message": "User registered successfully",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwiaWF0IjoxNzE0NTYwMDAwLCJleHAiOjE3MTQ2NDY0MDB9.signature",
    "email": "john@example.com",
    "name": "John Doe",
    "role": "VIEWER"
  }
}
```

### Login
Request:
```json
{
  "method": "POST",
  "url": "/api/auth/login",
  "headers": {
    "Content-Type": "application/json"
  },
  "body": {
    "email": "admin@zorvyn.com",
    "password": "Admin@1234"
  }
}
```

Response:
```json
{
  "status": 200,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkB6b3J2eW4uY29tIiwiaWF0IjoxNzE0NTYwMDAwLCJleHAiOjE3MTQ2NDY0MDB9.signature",
    "email": "admin@zorvyn.com",
    "name": "Super Admin",
    "role": "ADMIN"
  }
}
```

### Create Transaction
Request:
```json
{
  "method": "POST",
  "url": "/api/transactions",
  "headers": {
    "Authorization": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkB6b3J2eW4uY29tIiwiaWF0IjoxNzE0NTYwMDAwLCJleHAiOjE3MTQ2NDY0MDB9.signature",
    "Content-Type": "application/json"
  },
  "body": {
    "amount": 50000,
    "type": "INCOME",
    "category": "Salary",
    "date": "2024-03-01",
    "notes": "March salary"
  }
}
```

Response:
```json
{
  "status": 201,
  "message": "Transaction created",
  "data": {
    "id": 1,
    "amount": 50000,
    "type": "INCOME",
    "category": "Salary",
    "date": "2024-03-01",
    "notes": "March salary",
    "createdBy": "Super Admin",
    "createdAt": "2024-03-01T10:00:00"
  }
}
```

### Dashboard Summary
Request:
```json
{
  "method": "GET",
  "url": "/api/dashboard/summary",
  "headers": {
    "Authorization": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkB6b3J2eW4uY29tIiwiaWF0IjoxNzE0NTYwMDAwLCJleHAiOjE3MTQ2NDY0MDB9.signature"
  }
}
```

Response:
```json
{
  "status": 200,
  "message": "Dashboard summary fetched",
  "data": {
    "totalIncome": 50000,
    "totalExpense": 9500,
    "netBalance": 40500,
    "totalTransactions": 3,
    "totalIncomeCount": 1,
    "totalExpenseCount": 2,
    "categoryBreakdown": [
      { "category": "Salary", "type": "INCOME", "total": 50000 },
      { "category": "Rent", "type": "EXPENSE", "total": 8000 },
      { "category": "Food", "type": "EXPENSE", "total": 1500 }
    ],
    "monthlyTrends": [
      { "year": 2024, "month": 3, "monthName": "March", "type": "INCOME", "total": 50000 },
      { "year": 2024, "month": 3, "monthName": "March", "type": "EXPENSE", "total": 9500 }
    ],
    "recentTransactions": [
      {
        "id": 3,
        "amount": 1500,
        "type": "EXPENSE",
        "category": "Food",
        "date": "2024-03-12",
        "notes": "Groceries",
        "createdBy": "Super Admin",
        "createdAt": "2024-03-12T18:25:40"
      }
    ]
  }
}
```

## 11. Error Handling
The API uses a centralized exception strategy through `GlobalExceptionHandler` to keep error responses consistent and easy to consume from frontend clients. Validation errors, missing resources, authentication issues, and unexpected failures are converted into a predictable JSON response shape with an HTTP status code, a clear message, and `data: null`. This approach reduces controller-level error duplication and ensures clients can implement a single error-handling workflow. Authentication and authorization errors are handled by Spring Security and naturally return 401/403 when token or role checks fail.

| Scenario | HTTP Status | Message Example |
|---|---:|---|
| Resource not found | 404 | Transaction not found with id: 5 |
| Validation failed | 400 | amount must not be null |
| Unauthenticated request | 401 | Unauthorized |
| Insufficient role | 403 | Forbidden |
| Duplicate email | 400 | Email already registered: john@example.com |
| Server error | 500 | Internal server error: Unexpected exception |

All error responses follow this format:

```json
{
  "status": 400,
  "message": "Validation failed",
  "data": null
}
```

## 12. Key Design Decisions
1. **Soft Delete**
Transactions are never permanently removed from storage. Instead, the `isDeleted` flag is set to `true`, and all business queries explicitly filter deleted records out. This preserves financial history, enables auditability, and prevents accidental data loss from destructive operations.

2. **DTO Pattern**
Controllers return DTOs rather than exposing JPA entities directly. This keeps API contracts stable even when persistence models evolve and avoids leaking internal fields that should not be exposed externally. It also enables response shaping for analytics endpoints without changing core entities.

3. **Constructor Injection**
All components use constructor-based dependency injection so dependencies are explicit and immutable after creation. This improves readability and testability because services can be instantiated with mocked collaborators in isolation. It also aligns with Spring best practices and avoids reflection-based field injection pitfalls.

4. **Role-based Access via @PreAuthorize**
Authorization rules are enforced at method level using `@PreAuthorize` with `hasAuthority` and `hasAnyAuthority`. This keeps access control declarative and close to endpoint definitions, making policy review straightforward. It also avoids scattering role checks inside business logic methods.

5. **Smart Filter Routing in TransactionService**
The `getAllTransactions` flow uses a deterministic if-else chain to route requests to the most specific repository query based on provided filters. This reduces unnecessary broad queries and improves query clarity and intent. It also keeps filter behavior predictable for API consumers.

6. **Auto-seeded Test Users**
The startup `DataSeeder` creates one user per role to make review and functional testing immediately possible. This removes setup friction and ensures role-specific endpoint behavior can be validated quickly. It is especially useful in an assessment context where evaluators need zero-friction access.

## 13. Assumptions Made
1. Authentication is JWT-based with a 24-hour token expiry, and refresh-token flow is intentionally out of scope for this assessment.
2. All monetary values use `BigDecimal` to ensure financial precision and avoid floating point rounding errors.
3. Soft delete is the only deletion strategy for transactions to preserve historical records and audit continuity.
4. Role is assigned at registration and can be changed afterward only through Admin-restricted user management endpoints.
5. Transaction category is intentionally modeled as free text instead of a normalized category table to keep implementation simple and flexible.
6. Email verification is not implemented because this submission focuses on backend API design and business flow validation.

## 14. What Could Be Added in Production
1. Refresh token support and server-side token revocation for stronger session control.
2. Email verification and password reset workflows for account lifecycle security.
3. A dedicated audit log to track who changed which resource and at what timestamp.
4. Rate limiting and brute-force protection on authentication endpoints.
5. Pagination and sorting support across all list-based endpoints for consistent scalability.
6. Full-text search across notes and optional transaction descriptions.
7. Export features for CSV and PDF statements suitable for reporting and sharing.
8. Automated unit and integration testing with JUnit, Mockito, and Testcontainers.
9. Swagger/OpenAPI documentation with request/response schemas and bearer auth support.
10. Docker and docker-compose packaging for reproducible local and deployment environments.
