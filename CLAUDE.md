# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Codebase Architecture

Multi-module Spring Boot project with 4 distinct applications:

- **app-vod** (port 8080): Main video-on-demand application with REST APIs, Thymeleaf UI, Spring Security, JPA/MySQL, and Kafka consumer
- **app-rating** (port 9090): Rating microservice using MongoDB and Kafka producer for event publishing  
- **first-boot** (port 9090): Simple web application demonstrating basic Spring Boot setup with Undertow
- **travel**: Console application showcasing Spring AOP and event handling patterns

Key architectural patterns: Layered architecture (Controller → Service → Repository), multiple DAO implementations (JPA/JDBC/Memory), DTO pattern with mappers, event-driven communication via Kafka.

## Development Commands

### Build & Run Applications
```bash
# Build specific application
mvn clean compile -f app-vod/pom.xml
mvn clean compile -f app-rating/pom.xml

# Run applications
mvn spring-boot:run -f app-vod/pom.xml
mvn spring-boot:run -f app-rating/pom.xml

# Package applications
mvn clean package -f app-vod/pom.xml
```

### Testing
```bash
# Run all tests for an application
mvn test -f app-vod/pom.xml
mvn test -f app-rating/pom.xml

# Run single test class
mvn test -f app-vod/pom.xml -Dtest=CinemaServiceBeanTest

# Run specific test method
mvn test -f app-vod/pom.xml -Dtest=CinemaServiceBeanTest#test_method_name

# Run tests with H2 profile (integration tests)
mvn test -f app-vod/pom.xml -Dspring.profiles.active=h2
```

### Code Quality Checks
```bash
# Verify compilation (type checking)
mvn clean compile -f app-vod/pom.xml
mvn clean compile -f app-rating/pom.xml

# Note: No linting tools are configured in this project
```

### Infrastructure Setup
```bash
# Start supporting services (required for app-vod and app-rating)
cd docker-vod
docker-compose up -d

# Stop services
docker-compose down

# View Kafka topics (Kafdrop UI)
# http://localhost:9000
```

## Database Configuration

- **app-vod**: MySQL (localhost:3306/vod, root/mysql) - JPA entities with multiple DAO implementations
- **app-rating**: MongoDB (localhost:27017/lab, admin/admin) - Spring Data MongoDB
- **Testing**: H2 in-memory database with `-Dspring.profiles.active=h2`

## Application Endpoints

- **app-vod**: http://localhost:8080
  - REST APIs: `/movies`, `/cinemas`, `/directors`
  - UI: `/ui/movies`, `/ui/cinemas`
  - HATEOAS: `/hateoas`
  - Actuator: `/actuator`
  
- **app-rating**: http://localhost:9090/ratings

## Test Requirements

- Use snake_case for all test method names
- Use AssertJ for assertions (`assertThat()`)
- For testing multiple fields in a class, create custom AssertJ assertions
- Always verify tests pass before completing tasks
- Integration tests use H2 profile and Rest Assured

## Configuration Notes

- DAO implementation controlled by `vod.dao` property (jpa/jdbc/mem)
- Kafka configured for event streaming between app-vod and app-rating
- Security enabled on app-vod with basic authentication
- Internationalization support (messages.properties, messages_pl.properties, messages_it.properties)

## Project Dependencies

- Java 17
- Spring Boot 3.4.2 (parent POM)
- Maven build system
- Lombok for reducing boilerplate code
- Infrastructure services via Docker Compose in `docker-vod/`