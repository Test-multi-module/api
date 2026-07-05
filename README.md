# TestProj Server
>Note to Reviewers: This project is currently in an active development stage.
A comprehensive README with detailed local setup instructions and an architecture deep-dive is being prepared. 
Currently, the project serves as a technical showcase for:
Multi-module Spring Boot architecture: Clear separation of concerns between API, Auth, and DB layers.
Secure Authentication: Implementation of JWT and OAuth2 (Google) authentication flows.
Advanced Persistence: Usage of jOOQ for type-safe SQL and Flyway for database versioning and migrations.
Cloud Native & CI/CD: Fully automated build and deployment pipelines to Azure via GitHub Actions.
---

### Technical Stack
* **Core**: Java, Spring Boot 4.0.x
* **Security**: Spring Security, JWT (jjwt), OAuth2
* **Database**: PostgreSQL (Azure Flexible Server)
* **Persistence Layer**: jOOQ (Type-safe SQL generator)
* **Migrations**: Flyway
* **DevOps**: Docker & Docker Compose, GitHub Actions
* **Infrastructure**: Azure App Service
---

### Project Structure
* `api`: Main REST API entry point and controllers.
* `auth`: Authentication logic, JWT handling, and OAuth2 success handlers.
* `db`: Database access layer (Data Services).
* `db/schema`: Database schema definitions, Flyway migrations, and jOOQ code generation configuration.

## Java version
The project uses Java 21.
Recommended local setup:
- Project SDK: Java 21
- Maven Runner JRE: Java 21
Using the same JDK version for the project and Maven is recommended to avoid local build inconsistencies.

## Local development: settings and recommendations
Currently, local development is configured and available out of the box for Windows users working in IntelliJ IDEA.
Details: [local-development/README-local-dev.md](local-development/README-local-dev.md)
Main flow:
1. Run `maven_standard_flow.run.xml`
2. Run `ApiApplication.run.xml`