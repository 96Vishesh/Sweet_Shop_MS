# Sweet Shop Management System 🍬

A modern **Sweet Shop Management System** built with Spring Boot and JWT-based authentication. This application allows users to manage sweet inventory, handle purchases, and perform CRUD operations securely with role-based access control.

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation \& Setup](#installation--setup)
- [API Endpoints](#api-endpoints)
- [Authentication](#authentication)
- [Database Schema](#database-schema)
- [My AI Usage](#my-ai-usage)
- [Testing](#testing)
- [Contributing](#contributing)

---

## ✨ Features

- **User Authentication \& Authorization**: JWT-based secure login with role-based access control (Admin/User)
- **Sweet Inventory Management**: CRUD operations for sweet items
- **Search \& Filter**: Search sweets by name, category, and price range
- **Purchase System**: Handle sweet purchases with automatic quantity updates
- **Restock Management**: Admin-only feature to restock inventory
- **Secure Endpoints**: Protected routes with JWT token validation
- **PostgreSQL Database**: Persistent data storage with JPA/Hibernate
- **Environment Configuration**: Secure credential management using `.env` files

---

## 🛠 Tech Stack

### Backend
- **Java 17**
- **Spring Boot 4.0.0**
- **Spring Security** with JWT (JSON Web Tokens)
- **Spring Data JPA** (Hibernate)
- **PostgreSQL** database
- **Lombok** for boilerplate reduction
- **Maven** for dependency management

### Security
- **JWT (jjwt 0.13.0)** for authentication
- **BCrypt** password hashing
- **Role-based access control** (ADMIN/USER)

### Development Tools
- **dotenv-java** for environment variable management
- **H2 Database** for testing

---

## 📁 Project Structure

```
com.inn.SweetShop/
├── src/
│   ├── main/
│   │   ├── java/com/inn/SweetShop/
│   │   │   ├── config/          # Configuration classes (Security, CORS, DotEnv)
│   │   │   ├── Constants/       # Application constants
│   │   │   ├── Dao/             # Data Access Objects (Repositories)
│   │   │   ├── JWT/             # JWT Filter and utilities
│   │   │   ├── POJO/            # Plain Old Java Objects (Entities)
│   │   │   │   ├── Sweet.java
│   │   │   │   └── User.java
│   │   │   ├── Rest/            # REST controller interfaces
│   │   │   ├── RestImpl/        # REST controller implementations
│   │   │   ├── Service/         # Service layer interfaces
│   │   │   ├── ServiceImpl/     # Service layer implementations
│   │   │   ├── utils/           # Utility classes
│   │   │   └── Wrapper/         # Response wrapper classes
│   │   └── resources/
│   │       └── application.properties
│   └── test/                    # Unit and integration tests
├── .env                         # Environment variables (not committed)
├── .env.example                 # Example environment variables
├── pom.xml                      # Maven configuration
└── README.md
```

---

## ⚙️ Prerequisites

Before running this project, ensure you have the following installed:

- **Java 17** or higher
- **Maven 3.6+**
- **PostgreSQL 12+** (or compatible version)
- **Git** (for cloning the repository)

---

## 🚀 Installation \& Setup

### 1. Clone the Repository

```bash
git clone https://github.com/96Vishesh/Sweet_Shop_MS.git
cd SweetShop-Backend/com.inn.SweetShop
```

### 2. Configure Database

Create a PostgreSQL database:

```sql
CREATE DATABASE sweetshopdb;
```

### 3. Environment Variables

Create a `.env` file in the project root by copying `.env.example`:

```bash
cp .env.example .env
```

Edit the `.env` file with your database credentials:

```properties
DB_URL=jdbc:postgresql://localhost:5432/sweetshopdb
DB_USERNAME=postgres
DB_PASSWORD=your_password_here
```

### 4. Build the Project

```bash
mvn clean install
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

The application will start on **http://localhost:8080**

---

## 🔌 API Endpoints

### Authentication Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| `POST` | `/api/user/signup` | Register new user | Public |
| `POST` | `/api/user/login` | User login | Public |

### Sweet Management Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| `POST` | `/api/sweets/add` | Add new sweet | Authenticated |
| `GET` | `/api/sweets/all` | Get all sweets | Public |
| `GET` | `/api/sweets/search` | Search sweets | Public |
| `PUT` | `/api/sweets/update/{id}` | Update sweet | Authenticated |
| `DELETE` | `/api/sweets/delete/{id}` | Delete sweet | Admin only |
| `POST` | `/api/sweets/purchase/{id}` | Purchase sweet | Authenticated |
| `POST` | `/api/sweets/restock/{id}` | Restock sweet | Admin only |

### Example Request: Add Sweet

```bash
POST /api/sweets/add
Content-Type: application/json
Authorization: Bearer <your_jwt_token>

{
  "name": "Gulab Jamun",
  "category": "Traditional",
  "price": "250.00",
  "quantity": "50",
  "description": "Soft and delicious traditional sweet"
}
```

---

## 🔐 Authentication

This application uses **JWT (JSON Web Tokens)** for authentication:

1. **Register** a new user via `/api/user/signup`
2. **Login** with credentials at `/api/user/login` to receive a JWT token
3. **Include the token** in the `Authorization` header for protected endpoints:
   ```
   Authorization: Bearer <your_jwt_token>
   ```

### Roles
- **USER**: Can view, add, update, and purchase sweets
- **ADMIN**: Full access including delete and restock operations

---

## 🗄 Database Schema

### Sweet Entity

| Field | Type | Constraints |
|-------|------|-------------|
| `id` | String | Primary Key |
| `name` | String | NOT NULL, UNIQUE |
| `category` | String | NOT NULL |
| `price` | BigDecimal | NOT NULL (precision: 19, scale: 2) |
| `quantity` | Integer | NOT NULL |
| `description` | String | Optional (max 500 chars) |

### User Entity

| Field | Type | Constraints |
|-------|------|-------------|
| `id` | Long | Primary Key, Auto-generated |
| `name` | String | NOT NULL |
| `email` | String | NOT NULL, UNIQUE |
| `password` | String | NOT NULL (BCrypt hashed) |
| `role` | String | NOT NULL (USER/ADMIN) |

---

## 🤖 My AI Usage

Throughout the development of this Sweet Shop Management System, I leveraged various AI tools to enhance productivity, streamline development workflows, and ensure code quality. Below is a detailed breakdown of the AI tools used and their impact on my development process:

### Which AI Tools I Used

1. **Claude (Anthropic)** - Advanced code generation and architecture planning
2. **ChatGPT (OpenAI)** - Frontend development and API integration boilerplates
3. **WindSurf** - Frontend UI/UX development and component scaffolding

### How I Used Them

#### 🔷 Claude
- **Authentication System Design**: I used Claude to brainstorm and design the JWT-based authentication flow, including token generation, validation, and refresh mechanisms. Claude helped me structure the security configuration and understand Spring Security filter chains.
  
- **Service Layer Architecture**: Asked Claude to suggest best practices for separating business logic from controller logic, which led to the creation of dedicated Service and ServiceImpl layers for better code organization.

- **Exception Handling Strategy**: Consulted Claude for implementing comprehensive exception handling patterns across the application, ensuring consistent error responses and proper HTTP status codes.

- **Code Review & Optimization**: Used Claude to review complex business logic in the purchase and restock methods, identifying potential edge cases and suggesting improvements for inventory management.

#### 💬 ChatGPT
- **API Integration Boilerplates**: ChatGPT was instrumental in generating boilerplate code for API integration between the Angular frontend and Spring Boot backend. It provided template code for HTTP services, interceptors, and error handling.

- **CORS Configuration**: Asked ChatGPT to generate the CORS configuration setup for allowing cross-origin requests from the Angular frontend (`http://localhost:4200`) to the backend (`http://localhost:8080`).

- **Frontend Service Layer**: Used ChatGPT to create Angular service templates for sweet management, user authentication, and HTTP request handling with proper TypeScript typing.

- **Request/Response DTOs**: ChatGPT helped generate the data transfer object structures for API requests and responses, ensuring type safety and consistency between frontend and backend.

#### 🌊 WindSurf
- **Angular Component Scaffolding**: WindSurf was used extensively for generating Angular components for the frontend, including sweet listing, sweet detail forms, login/signup pages, and navigation components.

- **UI Component Templates**: Used WindSurf to create responsive HTML templates with Angular Material components, ensuring a modern and user-friendly interface.

- **Form Validation Logic**: WindSurf assisted in implementing reactive forms with validation rules for sweet creation, user registration, and login forms.

- **Routing Configuration**: Generated Angular routing modules with WindSurf, implementing route guards for protected pages and navigation flows.

### Your Reflection on AI Impact

The integration of AI tools into my development workflow had a **transformative impact** on both productivity and code quality:

#### ✅ Positive Impacts

1. **Accelerated Development**: AI tools reduced development time by approximately 40-50%. Tasks that would typically take hours (like setting up authentication, writing boilerplate code, or configuring CORS) were completed in minutes.

2. **Learning Catalyst**: Rather than replacing learning, AI tools acted as intelligent tutors. When Claude explained JWT implementation or ChatGPT walked me through CORS configuration, I gained deeper understanding of underlying concepts.

3. **Code Quality Improvement**: AI-suggested patterns and best practices elevated my code quality. The separation of concerns, error handling strategies, and security configurations were more robust than what I might have implemented independently.

4. **Reduced Context Switching**: Having AI assistants provide immediate answers reduced the need to constantly switch between documentation, Stack Overflow, and my IDE, maintaining flow state and focus.

5. **Confidence in Architecture**: AI tools validated my architectural decisions and suggested alternatives I hadn't considered, giving me confidence that the solution was well-designed.

#### ⚠️ Challenges & Learnings

1. **Critical Evaluation Required**: Not all AI suggestions were perfect. I learned to critically evaluate AI-generated code rather than blindly accepting it. For example, initial CORS configurations were too permissive and needed tightening for production.

2. **Understanding Over Automation**: I made a conscious effort to understand AI-generated code rather than just copying it. This approach ensured I could debug issues and modify the code when requirements changed.

3. **Testing Responsibility**: While AI could generate code, thorough testing remained my responsibility. I learned that AI-assisted development still requires rigorous manual and automated testing.

4. **Customization Needed**: Boilerplate code from AI required customization to fit specific business requirements. The purchase and restock logic, for instance, needed manual refinement beyond initial AI suggestions.

#### 🎯 Workflow Integration

My typical workflow evolved to:

1. **Plan** architecture and approach independently
2. **Consult** AI for implementation patterns and boilerplates
3. **Review** and customize generated code
4. **Test** thoroughly
5. **Iterate** with AI assistance for optimization

This balanced approach ensured I leveraged AI's productivity benefits while maintaining code ownership and understanding.

#### 🌟 Key Takeaway

AI tools are **force multipliers**, not replacements for engineering skills. They excel at reducing repetitive work, suggesting patterns, and providing rapid feedback. However, critical thinking, domain knowledge, and thorough testing remain irreplaceable human contributions. The most effective approach is **collaborative intelligence**: combining AI's speed and breadth with human creativity, judgment, and contextual understanding.

---

## 🧪 Testing

The project includes unit and integration tests using:

- **JUnit 5** for unit testing
- **Spring Boot Test** for integration testing
- **H2 in-memory database** for test isolation
- **Spring Security Test** for authentication testing

Run tests with:

```bash
mvn test
```

---

## 🤝 Contributing

Contributions are welcome! To contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📝 License

This project was developed as part of the **Incubyte TDD Kata** assessment.

---

## 👤 Author

Vishesh Srivastava

- GitHub: [@96Vishesh](https://github.com/96Vishesh)
- Project: [Sweet_Shop_MS](https://github.com/96Vishesh/Sweet_Shop_MS)

---

## 📞 Support

For questions or issues, please open an issue on the GitHub repository.

---

**Made with ❤️ using Spring Boot and AI-assisted development**

