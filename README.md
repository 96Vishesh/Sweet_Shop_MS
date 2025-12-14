# Sweet Shop Management System 🍬

A modern, full-stack **Sweet Shop Management System** that enables users to browse, search, and purchase sweets while providing administrators with comprehensive inventory management capabilities. Built with Angular 19 for a dynamic frontend experience and Spring Boot 4.0 for a robust, secure backend with JWT-based authentication.

## 📋 Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Setup Instructions](#setup-instructions)
  - [Prerequisites](#prerequisites)
  - [Backend Setup](#backend-setup)
  - [Frontend Setup](#frontend-setup)
- [Screenshots](#screenshots)
- [My AI Usage](#my-ai-usage)

---

## 🎯 Project Overview

The Sweet Shop Management System is a comprehensive web application designed to digitize and streamline sweet shop operations. The system provides:

- **Customer-facing features**: Browse sweet catalog, search and filter products, view details, and make purchases
- **Admin capabilities**: Add, update, delete sweets, manage inventory, and restock products
- **Secure authentication**: JWT-based authentication system with role-based access control (User/Admin)
- **Real-time inventory**: Automatic quantity updates upon purchases with low-stock management

The application follows modern software architecture principles with a clear separation between frontend and backend, RESTful API design, and secure data handling practices.

---

## ✨ Features

### User Features
- 🔐 **User Authentication**: Secure signup and login with JWT tokens
- 🍭 **Browse Sweets**: View all available sweets with details (name, category, price, quantity)
- 🔍 **Search & Filter**: Search sweets by name, category, or price range
- 🛒 **Purchase System**: Buy sweets with automatic inventory updates
- 📱 **Responsive Design**: Modern, mobile-friendly interface

### Admin Features
- ➕ **Add Sweets**: Create new sweet products with details
- ✏️ **Update Products**: Modify existing sweet information
- 🗑️ **Delete Sweets**: Remove products from inventory
- 📦 **Restock Management**: Replenish inventory quantities
- 👥 **User Management**: Role-based access control

### Technical Features
- 🔒 **JWT Authentication**: Secure token-based authentication
- 🛡️ **Spring Security**: Protected endpoints with role-based authorization
- 💾 **PostgreSQL Database**: Persistent data storage with JPA/Hibernate
- 🌐 **CORS Configuration**: Proper cross-origin resource sharing setup
- ⚡ **RESTful API**: Well-structured API endpoints following REST principles

---

## 🛠 Tech Stack

### Frontend
- **Angular 19.2.0** - Modern web framework
- **TypeScript 5.7.2** - Type-safe JavaScript
- **RxJS 7.8.0** - Reactive programming
- **Angular Forms** - Reactive form handling
- **Angular Router** - Client-side routing

### Backend
- **Java 17** - Programming language
- **Spring Boot 4.0.0** - Application framework
- **Spring Security** - Authentication & authorization
- **Spring Data JPA** - Database abstraction with Hibernate
- **PostgreSQL** - Relational database
- **JWT (jjwt 0.13.0)** - JSON Web Token authentication
- **Lombok** - Boilerplate code reduction
- **Maven** - Dependency management

### Development Tools
- **dotenv-java** - Environment variable management
- **H2 Database** - In-memory database for testing
- **JUnit 5** - Unit testing framework

---

## 🚀 Setup Instructions

### Prerequisites

Before running this project, ensure you have the following installed:

- **Java 17** or higher ([Download](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html))
- **Node.js 18+** and **npm** ([Download](https://nodejs.org/))
- **Angular CLI 19+** (install via `npm install -g @angular/cli`)
- **Maven 3.6+** ([Download](https://maven.apache.org/download.cgi))
- **PostgreSQL 12+** ([Download](https://www.postgresql.org/download/))
- **Git** (for cloning the repository)

---

### Backend Setup

#### 1. Clone the Repository

```bash
git clone https://github.com/96Vishesh/Sweet_Shop_MS.git
cd Sweet_Shop_MS/SweetShop-Backend/com.inn.SweetShop
```

#### 2. Configure PostgreSQL Database

**Create the database:**

```sql
CREATE DATABASE sweetshopdb;
```

You can create this database using pgAdmin or the PostgreSQL command line:

```bash
psql -U postgres
CREATE DATABASE sweetshopdb;
\q
```

#### 3. Set Up Environment Variables

Create a `.env` file in the backend root directory (`SweetShop-Backend/com.inn.SweetShop/`):

```bash
cp .env.example .env
```

Edit the `.env` file with your PostgreSQL credentials:

```properties
DB_URL=jdbc:postgresql://localhost:5432/sweetshopdb
DB_USERNAME=postgres
DB_PASSWORD=your_password_here
```

**Important**: Replace `your_password_here` with your actual PostgreSQL password.

#### 4. Build the Backend

Navigate to the backend directory and build the project:

```bash
mvn clean install
```

This will:
- Download all dependencies
- Compile the Java code
- Run tests
- Create the executable JAR file

#### 5. Run the Backend Server

Start the Spring Boot application:

```bash
mvn spring-boot:run
```

The backend server will start on **http://localhost:8080**

You should see output similar to:
```
Started SweetShopApplication in X.XXX seconds
```

**Backend is now running!** ✅

---

### Frontend Setup

#### 1. Navigate to Frontend Directory

```bash
cd ../../SweetShop-Frontend/sweetshop-ui
```

Or if starting from the project root:

```bash
cd SweetShop-Frontend/sweetshop-ui
```

#### 2. Install Dependencies

Install all required npm packages:

```bash
npm install
```

This will install:
- Angular framework and dependencies
- RxJS for reactive programming
- TypeScript compiler
- Development tools and testing frameworks

#### 3. Run the Frontend Application

Start the Angular development server:

```bash
npm start
```

Or alternatively:

```bash
ng serve
```

The frontend application will start on **http://localhost:4200**

You should see output like:
```
✔ Browser application bundle generation complete.
Initial Chunk Files | Names         |  Raw Size
main.js             | main          | XXX.XX kB |

Application bundle generation complete. [X.XXX seconds]
Watch mode enabled. Watching for file changes...
  ➜  Local:   http://localhost:4200/
```

#### 4. Access the Application

Open your web browser and navigate to:

```
http://localhost:4200
```

**Frontend is now running!** ✅

---

### Quick Start Summary

**Backend:**
```bash
# Navigate to backend
cd SweetShop-Backend/com.inn.SweetShop

# Configure .env file with your database credentials
cp .env.example .env

# Build and run
mvn clean install
mvn spring-boot:run
```

**Frontend:**
```bash
# Navigate to frontend
cd SweetShop-Frontend/sweetshop-ui

# Install and run
npm install
npm start
```

**Access:** Open http://localhost:4200 in your browser

---

## 📸 Screenshots

### Application Interface

> **Note**: Add screenshots of your application here. Recommended screenshots:

1. **Login/Signup Page**
   - User authentication interface
   
2. **Sweet Catalog/Dashboard**
   - Main page showing all available sweets
   
3. **Sweet Details**
   - Detailed view of a single sweet product
   
4. **Search & Filter**
   - Demonstration of search and filtering functionality
   
5. **Admin Panel**
   - Admin interface for managing sweets
   
6. **Add/Edit Sweet Form**
   - Form for creating or updating sweet products
   
7. **Purchase Confirmation**
   - Purchase flow and confirmation

---

## 🤖 My AI Usage

Throughout the development of this Sweet Shop Management System, I leveraged AI tools to enhance productivity, accelerate development, and improve code quality. Here's a detailed account of how AI assisted in building this project:

### AI Tools Used

1. **WindSurf** - Frontend Development
2. **Claude AI** - Backend Development  
3. **ChatGPT** - Backend Development

---

### 🌊 WindSurf - Frontend Development

**WindSurf was used extensively for the Angular frontend development:**

#### Component Generation & Scaffolding
- Generated Angular components for authentication (login/signup pages)
- Created dashboard component for displaying sweet catalog
- Built form components for adding and editing sweet products
- Scaffolded service layer for HTTP communication with backend

#### UI/UX Implementation
- Designed responsive HTML templates with modern CSS styling
- Implemented reactive forms with proper validation rules
- Created navigation components and routing configuration
- Built reusable UI components for consistent design

#### TypeScript & Angular Best Practices
- Generated TypeScript interfaces for type-safe data models
- Implemented RxJS observables for asynchronous operations
- Set up Angular interceptors for JWT token management
- Created route guards for protecting authenticated pages

**Impact**: WindSurf accelerated frontend development by approximately 50%, providing well-structured component templates and handling boilerplate code generation. It helped maintain Angular best practices and consistent code patterns throughout the application.

---

### 🔷 Claude AI - Backend Development

**Claude AI assisted with backend architecture and Spring Boot implementation:**

#### Authentication & Security
- Designed JWT-based authentication flow and token management
- Implemented Spring Security configuration with custom filters
- Created user registration and login endpoints with proper password hashing
- Set up role-based access control (RBAC) for admin and user roles

#### Service Layer Architecture
- Structured service and repository layers following best practices
- Implemented business logic for sweet inventory management
- Created exception handling strategies with custom error responses
- Designed purchase and restock operations with transaction management

#### Database Design
- Modeled entity relationships (User, Sweet)
- Configured JPA/Hibernate for PostgreSQL integration
- Implemented data validation and constraints
- Set up database schema auto-generation

#### Code Review & Optimization
- Reviewed complex business logic for edge cases
- Suggested improvements for inventory management algorithms
- Optimized database queries and entity relationships
- Ensured proper HTTP status codes and REST conventions

**Impact**: Claude AI provided architectural guidance and helped implement robust, secure backend services. It ensured proper separation of concerns and following Spring Boot best practices, which improved code maintainability and scalability.

---

### 💬 ChatGPT - Backend Development

**ChatGPT was instrumental in backend API development and integration:**

#### API Development
- Generated RESTful API endpoint structures
- Created request and response DTO (Data Transfer Object) classes
- Implemented CRUD operations for sweet management
- Developed search and filter functionality with query parameters

#### CORS Configuration
- Set up CORS configuration to allow frontend-backend communication
- Configured allowed origins (http://localhost:4200) and methods
- Implemented proper headers for cross-origin requests
- Resolved CORS-related issues during development

#### Integration Support
- Generated boilerplate code for controller and service classes
- Created utility classes for common operations
- Implemented validation logic for API requests
- Provided code snippets for error handling and logging

#### Testing Assistance
- Suggested test cases for critical functionality
- Provided examples of JUnit test structure
- Helped with Spring Boot test configuration
- Generated test data for unit and integration tests

**Impact**: ChatGPT streamlined API development by providing reliable boilerplate code and solving integration challenges. It significantly reduced development time for repetitive tasks and helped maintain consistent API design patterns.

---

### Reflection on AI Impact

#### Productivity Gains
The combination of WindSurf, Claude AI, and ChatGPT resulted in:
- **60-70% faster development** compared to manual coding
- **Reduced debugging time** through AI-suggested best practices
- **Faster learning curve** for new technologies and frameworks
- **Improved code quality** with AI-recommended patterns

#### Learning Enhancement
Rather than replacing learning, AI tools acted as:
- **Intelligent tutors** explaining concepts and patterns
- **Code reviewers** catching potential issues early
- **Documentation assistants** providing context and examples
- **Best practice guides** for modern development standards

#### Critical Evaluation
While AI was incredibly helpful, I maintained:
- **Code ownership** by reviewing and understanding all AI-generated code
- **Critical thinking** to evaluate suggestions and adapt to specific requirements
- **Testing responsibility** with thorough manual and automated testing
- **Customization** to fit business logic beyond generic patterns

#### Balanced Approach
My workflow integrated AI effectively:
1. **Plan** architecture independently
2. **Consult** AI for implementation patterns
3. **Review** and customize generated code
4. **Test** thoroughly
5. **Iterate** with AI for optimization

**Key Takeaway**: AI tools are powerful force multipliers that enhance productivity and code quality when used thoughtfully. They excel at reducing repetitive work and providing rapid feedback, but human judgment, domain knowledge, and thorough testing remain essential for building robust applications.

---

## 📝 API Endpoints

### Authentication
- `POST /api/user/signup` - Register new user
- `POST /api/user/login` - User login (returns JWT token)

### Sweet Management
- `GET /api/sweets/all` - Get all sweets
- `GET /api/sweets/search` - Search sweets
- `POST /api/sweets/add` - Add new sweet (requires authentication)
- `PUT /api/sweets/update/{id}` - Update sweet (requires authentication)
- `DELETE /api/sweets/delete/{id}` - Delete sweet (admin only)
- `POST /api/sweets/purchase/{id}` - Purchase sweet (requires authentication)
- `POST /api/sweets/restock/{id}` - Restock sweet (admin only)

---

## 👤 Author

**Vishesh Kumar**

- GitHub: [@96Vishesh](https://github.com/96Vishesh)
- Project Repository: [Sweet_Shop_MS](https://github.com/96Vishesh/Sweet_Shop_MS)

---

## 📄 License

This project was developed as part of the **Incubyte TDD Kata** assessment.

---

**Made with ❤️ using Angular, Spring Boot, and AI-assisted development**
