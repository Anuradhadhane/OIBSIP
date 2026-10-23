# Digital Library Management System

**Oasis Infobyte SIP — Java Development — Task 5**

A REST API built with Java, Spring Boot, Spring Data JPA, and MySQL for managing library books, members, and book issue/return transactions.

## Features

- Add, view, search, update, and delete books
- Register and view library members
- Issue available books to members
- Return issued books and update availability
- View all loans, active loans, and a member's loan history
- Request validation and readable API error responses
- MySQL persistence through Spring Data JPA

## Tech stack

- Java 17
- Spring Boot 3.4.5
- Spring Web
- Spring Data JPA / Hibernate
- MySQL 8+
- Maven
- Postman for API testing

## Prerequisites

Install Java 17+, Maven (or use an IDE's Maven support), MySQL 8+, and Postman.

## 1. Create the database

Run this in MySQL:

```sql
CREATE DATABASE digital_library;
```

## 2. Configure database credentials

The application reads these environment variables:

- `DB_URL` (default: `jdbc:mysql://localhost:3306/digital_library`)
- `DB_USERNAME` (default: `root`)
- `DB_PASSWORD` (default: empty)

Set the password to your local MySQL password. Do not commit real credentials to GitHub.

## 3. Run the application

From this project directory:

```bash
mvn spring-boot:run
```

Or run `DigitalLibraryApplication.java` from your IDE.

Base URL: `http://localhost:8080`

On first startup, Hibernate creates/updates the tables. For a real production deployment, use managed database migrations instead of `ddl-auto=update`.

## API endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/books` | List books |
| GET | `/api/books?search=java` | Search by title or author |
| GET | `/api/books/{id}` | Get one book |
| POST | `/api/books` | Add a book |
| PUT | `/api/books/{id}` | Update a book |
| DELETE | `/api/books/{id}` | Delete a book (blocked while currently issued) |
| GET | `/api/members` | List members |
| GET | `/api/members/{id}` | Get one member |
| POST | `/api/members` | Register a member |
| GET | `/api/loans` | List all loans |
| GET | `/api/loans/active` | List unreturned loans |
| GET | `/api/loans/member/{memberId}` | Member's loan history |
| POST | `/api/loans/issue` | Issue a book |
| PUT | `/api/loans/{loanId}/return` | Return a book |

## Sample requests

### Add a book

`POST http://localhost:8080/api/books`

```json
{
  "title": "Effective Java",
  "author": "Joshua Bloch",
  "isbn": "9780134685991",
  "totalCopies": 3,
  "availableCopies": 3
}
```

### Register a member

`POST http://localhost:8080/api/members`

```json
{
  "fullName": "Aarav Patil",
  "email": "aarav.patil@example.com"
}
```

### Issue a book

`POST http://localhost:8080/api/loans/issue`

```json
{
  "bookId": 1,
  "memberId": 1
}
```

### Return a book

`PUT http://localhost:8080/api/loans/1/return`

No request body is required.

## Testing checklist

1. Start the app and confirm it starts without errors.
2. Add a book and confirm the response status is `201 Created`.
3. List books and verify the book is present.
4. Search by a word from the title or author.
5. Register a member and verify the response.
6. Issue a book and verify available copies decrease by one.
7. Return the loan and verify available copies increase by one.
8. Try issuing a nonexistent book/member and expect a `404 Not Found`.
9. Try adding a duplicate ISBN or email and expect a client error.
10. Try issuing a book with no available copies and expect `400 Bad Request`.
11. Run automated tests with `mvn test`.

Capture screenshots of successful requests and important error cases for your internship submission.

## Project structure

```text
Java-Task5-DigitalLibraryManagementSystem/
├── src/
│   ├── main/
│   │   ├── java/com/oasisinfobyte/library/
│   │   │   ├── controller/
│   │   │   ├── exception/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── DigitalLibraryApplication.java
│   │   └── resources/application.properties
│   └── test/
├── .gitignore
├── pom.xml
└── README.md
```

## Limitations and future improvements

This is an internship demonstration project. It does not currently implement login/roles, due dates, late fees, pagination, or a web frontend. These can be added as future enhancements.

## Internship submission checklist

- [ ] Verify the code runs locally and `mvn test` passes.
- [ ] Add real screenshots to a `screenshots/` folder.
- [ ] Record a demo video with a 2-second title card showing your full name, assigned track, and task title.
- [ ] Push the project under the public `OIBSIP` GitHub repository using the folder name required by your internship instructions.
- [ ] Publish the demo on LinkedIn, tag Oasis Infobyte, and use `#oasisinfobyte`.
- [ ] Comment substantively on two other interns' demo posts.
- [ ] Submit the official form shared by Oasis Infobyte.

**Note:** Do not include database passwords, API keys, or private information in the repository.
