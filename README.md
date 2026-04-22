# e-commerce-backend
> Backend for E-commerce System - Built with Spring Boot &amp; MySQL. 
> Focuses on Order Processing, Inventory Integrity, and Async Tracking Logs.

[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.x-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?logo=mysql)](https://www.mysql.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](./LICENSE)

---

## Table of Contents

- [Overview](#-overview)
- [Tech Stack](#-tech-stack)
- [Features](#-features)
- [System Architecture](#-system-architecture)
- [Database Design](#-database-design)
- [API Endpoints](#-api-endpoints)
- [Getting Started](#-getting-started)
- [Environment Variables](#-environment-variables)
- [Project Structure](#-project-structure)
- [What I Learned](#-what-i-learned)
- [Roadmap](#-roadmap)
- [Contact](#-contact)

---

## Overview



## Tech Stack

| Layer         | Technology                       |
|---------------|----------------------------------|
| Language      | Java 21                          |
| Framework     | Spring Boot 3.2, Spring MVC      |
| Security      | Spring Security, JWT (jjwt 0.11) |
| Database      | MySQL 8.0                        |
| ORM           | Spring Data JPA / Hibernate      |
| Build Tool    | Maven                            |
| Documentation | Swagger / OpenAPI 3 (springdoc)  |
| Testing       | JUnit 5, Mockito                 |
| Others        | Lombok, MapStruct                |

---

## Features

**Authentication & Authorization**
- [x] Register / Login with JWT
- [x] Role-based access (ADMIN, USER)
- [x] Token refresh

**Product Management**
- [x] CRUD for products and categories
- [x] Image upload (local storage)
- [x] Search, filter by category/price
- [x] Pagination & sorting

**Order Management**
- [x] Add to cart, update quantity, remove item
- [x] Place order, view order history
- [x] Order status flow: `PENDING → CONFIRMED → SHIPPED → DELIVERED`

**User**
- [x] View and update profile
- [x] Change password

---

## System Architecture

```
Client (Postman / Frontend)
        │
        ▼
  Controller Layer   
        │
        ▼
  Service Layer      
        │
        ▼
  Repository Layer   
        │
        ▼
     MySQL DB
```

> Follows a standard **3-layer architecture** (Controller → Service → Repository) for separation of concerns.

---

## Database Design

**Core tables:** `user`, `role`, `product`, `category`, `order`, `order_item`, `cart`, `cart_item`

ER Diagram: copy diagram from `docs` location and paste to drawdb.app

---

## API Endpoints

Base URL: `http://localhost:8080/api/v1`

Full docs available at: `http://localhost:8080/swagger-ui.html`

## Getting Started

### Prerequisites

- Java 17+
- MySQL 8.0+
- Maven 3.8+

### Steps

```bash
# 1. Clone the repo
git clone https://github.com/kimhieu-dev/e-commerce-backend.git
cd ecommerce-springboot

# 2. Create the database
mysql -u root -p
CREATE DATABASE ecommerce_db;

# 3. Configure environment variables
cp .env.example .env
# Edit .env with your DB credentials and JWT secret

# 4. Run the application
mvn spring-boot:run
```

The server starts at `http://localhost:8080`

---

## Environment Variables

Create a `.env` file in the root directory (see `.env.example`):

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=ecommerce_db
DB_USERNAME=root
DB_PASSWORD=your_password

JWT_SECRET=your_jwt_secret_key_here
JWT_EXPIRATION=86400000

SERVER_PORT=8080
```

> **Note:** Never commit `.env` to Git. It is listed in `.gitignore`.

---

## What I Learned

This project helped me practice:

- Designing RESTful APIs following naming conventions
- Implementing JWT authentication with Spring Security
- Using Spring Data JPA with pagination and custom `@Query`
- Handling exceptions globally with `@RestControllerAdvice`
- Writing unit tests with JUnit 5 and Mockito
- Structuring a Spring Boot project with layered architecture

---

## Roadmap

- [ ] Add Redis for caching product list
- [ ] Integrate VNPay / Stripe for payment
- [ ] Write integration tests
- [ ] Dockerize the application
- [ ] Deploy to Railway / Render

---

## Contact

**[Nguyen Kim Hieu]**  
📧 nguyenkimhieu.dev@gmail.com 
🔗 [LinkedIn](https://www.linkedin.com/in/hi%E1%BA%BFu-nguy%E1%BB%85n-kim-7a87913b8/)  
🐙 [GitHub](https://github.com/kimhieu-dev)

---
