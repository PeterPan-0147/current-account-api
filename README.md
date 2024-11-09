# Current Account API

This Spring Boot application provides REST APIs for managing current accounts for existing customers. It follows enterprise-level architecture principles and includes comprehensive test coverage.

## Features

- Create new current accounts for existing customers
- Initialize accounts with optional initial credit
- Retrieve customer details including account balance and transactions
- Swagger UI documentation
- In-memory data storage
- Comprehensive test coverage

## Technical Stack

- Java 17
- Spring Boot 3.6.5
- SpringDoc OpenAPI (Swagger UI)
- JUnit 5
- Mockito
- Lombok
- Maven

## Prerequisites

- Java 17
- Maven 3.6 

## Getting Started

1. Clone the repository:
```bash
git clone https://github.com/yourusername/current-account-api.git
cd current-account-api
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Documentation

Once the application is running, you can access the Swagger UI documentation at:
```
http://localhost:8080/swagger-ui/index.html
```
## Test Data

The application is pre-populated with a test customer that you can use for testing:
1. Customer ID: 3fa85f64-5717-4562-b3fc-2c963f66afa6
2. Name: John
3. Surname: Doe
