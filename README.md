# Password-Manager

# 🔐 Password Manager Project (Spring Boot + PostgreSQL + Spring Security)

A secure, user-friendly password manager REST API built using **Spring Boot**, **Spring Security**, and **PostgreSQL**. It allows users to register, unlock their vault (login), manage saved site credentials, and change their master password.

---

## 🚀 Features

- 🔐 User Registration and Login
- 🔏 Vault Unlock using Master Password (with session-based authentication)
- 🔒 Add, View, and Delete Saved Passwords
- 📄 View all saved entries
- 🔁 Change Master Password (with re-login enforcement)
- 🧹 Clear entire password vault
- 🛡️ Secured with Spring Security session-based authentication
- 🌐 CORS enabled for frontend integration (e.g., browser extension)

---

## 📦 Tech Stack

| Layer              | Tech                                |
|-------------------|-------------------------------------|
| Language           | Java 21                            |
| Framework          | Spring Boot                         |
| Security           | Spring Security (Session-based)     |
| Database           | PostgreSQL                          |
| Build Tool         | Gradle                              |
| Validation         | Jakarta Bean Validation             |
| API Style          | RESTful                             |
| Authentication     | Manual session + SecurityContext    |

---

## 📁 API Endpoints

### 🔐 Authentication

| Method | Endpoint         | Description                      |
|--------|------------------|----------------------------------|
| POST   | `/api/register`  | Register new user                |
| POST   | `/api/unlock`    | Log in and unlock the vault      |
| POST   | `/api/logout`    | Log out and invalidate session   |

### 🔏 Password Management (Authenticated)

| Method | Endpoint              | Description                      |
|--------|-----------------------|----------------------------------|
| POST   | `/api/passwords`      | Save a new site password         |
| GET    | `/api/passwords`      | Get password for a site (by name)|
| GET    | `/api/passwords/all`  | Get all saved passwords          |
| DELETE | `/api/passwords`      | Delete password by site name     |
| DELETE | `/api/passwords/all`  | Delete all saved passwords       |

### 🔁 Master Password Management

| Method | Endpoint               | Description                    |
|--------|------------------------|--------------------------------|
| POST   | `/api/change-password` | Change master password         |

---

## 🛠️ Project Setup

### ✅ Prerequisites

- Java 21+
- PostgreSQL installed and running
- Gradle installed (or use `./gradlew` wrapper)

### ⚙️ Configure PostgreSQL

Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/passwordmanager
spring.datasource.username=your_pg_username
spring.datasource.password=your_pg_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
▶️ Run the App
🖥️ Run via IDE
Open the project in IntelliJ or Eclipse

Run PasswordManagerProjectApplication.java

📦 Run via Terminal
bash
Copy
Edit
./gradlew bootRun
🧪 Sample JSON Payloads
📝 Register
json
Copy
Edit
POST /api/register
{
  "username": "john_doe",
  "password": "masterPassword123"
}
🔐 Unlock Vault (Login)
json
Copy
Edit
POST /api/unlock
{
  "username": "john_doe",
  "password": "masterPassword123"
}
🔏 Add Password
json
Copy
Edit
POST /api/passwords
{
  "site": "gmail.com",
  "siteUsername": "john.doe@gmail.com",
  "sitePassword": "mySecurePassword"
}
🔁 Change Password
json
Copy
Edit
POST /api/change-password
{
  "username": "john_doe",
  "oldPassword": "masterPassword123",
  "newPassword": "newSecurePassword456"
}
✅ To Do
✅ Secure session with HTTPS (for production)

⬜ Add browser extension support

⬜ Encrypt database fields (e.g., using JCE or Jasypt)

⬜ Add JWT-based stateless auth (alternative)

⬜ Add password strength validation & generation

📄 License
This project is open-source and free to use under the MIT License.

🙋‍♂️ Author
Syed Wajahat Husain Abdi
📧 swhabdi@gmail.com
🔗 LinkedIn
