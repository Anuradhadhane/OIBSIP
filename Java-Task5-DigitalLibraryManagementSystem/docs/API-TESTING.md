# Postman API Testing Guide

Set a Postman environment variable `baseUrl` to `http://localhost:8080`.

Run the requests in this order:

1. **Create book** — `POST {{baseUrl}}/api/books`
   ```json
   {
     "title": "Effective Java",
     "author": "Joshua Bloch",
     "isbn": "9780134685991",
     "totalCopies": 2,
     "availableCopies": 2
   }
   ```
   Expected: `201 Created`. Note the returned `id`.

2. **List books** — `GET {{baseUrl}}/api/books`
   Expected: `200 OK`.

3. **Search books** — `GET {{baseUrl}}/api/books?search=effective`
   Expected: matching book in response.

4. **Create member** — `POST {{baseUrl}}/api/members`
   ```json
   {
     "fullName": "Aarav Patil",
     "email": "aarav.patil@example.com"
   }
   ```
   Expected: `201 Created`. Note the returned `id`.

5. **Issue book** — `POST {{baseUrl}}/api/loans/issue`
   ```json
   {
     "bookId": 1,
     "memberId": 1
   }
   ```
   Replace IDs with the IDs from your responses. Expected: `201 Created`; available copies decreases by one.

6. **View active loans** — `GET {{baseUrl}}/api/loans/active`
   Expected: the issued loan is listed.

7. **Return book** — `PUT {{baseUrl}}/api/loans/1/return`
   Replace `1` with the loan ID returned when issuing. Expected: `200 OK`; book availability increases.

8. **Negative test: unknown book** — `GET {{baseUrl}}/api/books/999999`
   Expected: `404 Not Found`.

9. **Negative test: duplicate ISBN** — repeat the create-book request with the same ISBN.
   Expected: client error (normally `400 Bad Request`).

Use actual returned IDs rather than assuming they will always be 1. Save screenshots only after you run the requests successfully.
