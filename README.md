# Skynet

Skynet is a modern e-commerce backend application built with Spring Boot, providing a robust RESTful API for managing products, orders, users, and categories.

## 🚀 Technology Stack

- **Java 21** - Latest LTS version
- **Spring Boot 3.5.7** - Main application framework
- **Spring Data MongoDB** - Database integration
- **Spring Security** - Authentication and authorization
- **MongoDB** - NoSQL database
- **Lombok** - Reduce boilerplate code
- **Jakarta Validation** - Bean validation
- **Hibernate Validator** - Validation implementation
- **Maven** - Dependency management and build tool

## 📋 Features

### Core Domain Models

- **Users** - User management with roles, authentication, and address management
- **Products** - Product catalog with categories, ratings, tags, and stock control
- **Categories** - Product categorization with slug-based URLs
- **Orders** - Order processing with status tracking and payment methods
- **Addresses** - Delivery address management

### Key Capabilities

- 🔐 **Security** - Spring Security integration for authentication and authorization
- 📦 **Product Management** - Complete CRUD operations with stock tracking
- 🛒 **Order Management** - Order creation, tracking, and status management
- 👤 **User Management** - User registration, profile management, and role-based access
- 🏷️ **Category Management** - Organized product categorization
- 💰 **Payment Processing** - Multiple payment method support
- ⭐ **Product Ratings** - Rating and review system
- 🔍 **Search & Filtering** - Tag-based product search
- 🔄 **Soft Delete** - Logical deletion for data integrity
- ⚡ **Optimistic Locking** - Concurrency control for products

## 🛠️ Prerequisites

- Java Development Kit (JDK) 21 or higher
- MongoDB 4.0 or higher
- Maven 3.6 or higher

## 📦 Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/alejanf2885/skynet.git
   cd skynet
   ```

2. **Configure MongoDB connection**
   
   Edit `src/main/resources/application.properties` and add your MongoDB configuration:
   ```properties
   spring.application.name=skynet
   spring.data.mongodb.uri=mongodb://localhost:27017/skynet
   spring.data.mongodb.database=skynet
   ```

3. **Build the project**
   ```bash
   ./mvnw clean install
   ```

## 🚀 Running the Application

### Development Mode

Run with Spring Boot DevTools for auto-reload:

```bash
./mvnw spring-boot:run
```

### Production Mode

Build and run the JAR:

```bash
./mvnw clean package
java -jar target/skynet-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080` by default.

## 📁 Project Structure

```
skynet/
├── src/
│   ├── main/
│   │   ├── java/com/alejanf/skynet/
│   │   │   ├── model/           # Domain entities
│   │   │   │   ├── User.java
│   │   │   │   ├── Product.java
│   │   │   │   ├── Order.java
│   │   │   │   ├── Category.java
│   │   │   │   ├── OrderProduct.java
│   │   │   │   ├── Address.java
│   │   │   │   ├── Role.java
│   │   │   │   ├── OrderStatus.java
│   │   │   │   └── PaymentMethod.java
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   │   ├── UserDTO.java
│   │   │   │   ├── OrderDTO.java
│   │   │   │   ├── CategoryDTO.java
│   │   │   │   ├── CreateOrderDTO.java
│   │   │   │   ├── CreateCategoryDTO.java
│   │   │   │   ├── CreateOrderProductDTO.java
│   │   │   │   └── OrderProductDTO.java
│   │   │   ├── repository/      # Data access layer
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── ProductRepository.java
│   │   │   │   ├── OrderRepository.java
│   │   │   │   └── CategoryRepository.java
│   │   │   └── SkynetApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/alejanf/skynet/
│           └── SkynetApplicationTests.java
├── pom.xml
└── README.md
```

## 💾 Data Models

### User
- User authentication and profile management
- Role-based access control (USER, ADMIN)
- Multiple delivery addresses support
- Order history tracking
- Account security features (failed login attempts, last login)

### Product
- Product information (name, description, price)
- Stock management
- Category association
- Tag-based search
- Rating system
- SEO-friendly slugs
- Version control for concurrent updates

### Order
- Order creation and tracking
- Multiple products per order
- Order status management (PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED)
- Payment method support
- Delivery address
- Total price and quantity calculations

### Category
- Product categorization
- SEO-friendly slugs
- Category images
- Active/inactive status

## 🧪 Testing

Run the test suite:

```bash
./mvnw test
```

## 🔧 Development

### Build the project

```bash
./mvnw clean install
```

### Run tests

```bash
./mvnw test
```

### Package for production

```bash
./mvnw clean package
```

## 📝 Configuration

### Application Properties

Key configuration options in `application.properties`:

```properties
# Application Name
spring.application.name=skynet

# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/skynet
spring.data.mongodb.database=skynet

# Server Port (optional)
server.port=8080
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the terms specified in the `pom.xml` file.

## 👤 Author

**alejanf2885**

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- MongoDB for the flexible NoSQL database
- Project Lombok for reducing boilerplate code
