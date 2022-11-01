# System Supporting Student Internships

## Table of Consents
1. [Description](#description)
2. [Technologies and system architecture](#technologies-and-system-architecture)
  - [Backend](#backend)
  - [Database](#database)
  - [Frontend](#frontend)
3. [Addresses](#addresses)
4. [Documentation](#documentation)

## Description
The engineer thesis purpose is to design and implement system which will support process of student internships. The main goal of the system is to simplifie and digitize whole process.

System as Web Application, allows:
- registration of the students
- adding companies that organise internships 
- adding documents
- approving documents by companies and universities supervisors
- communicating with integrated communication system

## Technologies and system architecture
- **Java 11 + Spring Boot** - Backend
- **Vue.js 3.0** - Frontend
- **MySQL** - Relational Database Management System
- **Hibernate** - Object-Relational Mapping framework
- **Swagger** – Automated documentation for describing RESTful APIs (expressed using JSON)

<img width="1030" alt="136632722-f6ed5408-2462-434d-bbaf-05d9d6d1cd29" src="https://user-images.githubusercontent.com/82596765/199352966-dfe6907f-f0fe-491d-b9ec-f76b8ace4dc2.png">

## Backend
API based on REST architectural style.

JWT mechanism is used for authorization and autentification:
- At the begining client sends HTTP request (POST) with login and password.
- If credentials are correct - server generates JWT token with encoded user role and sends it to the client.
- Client saves the token and attaches it in every request as Authorization header.

### Most important dependencies from pom.xml:
- `spring-boot-starter-web` - tools essential for creating web app.
- `mysql-connector-java` - JDBC driver for MySQL.
- `spring-boot-starter-data-jpa` - connecting application with relational database.
- `spring-boot-starter-security` - set of tools for application security (for example: JWT mechanism).

### Swagger
API documentation which helps for visualition of server resources, divided into controllers:
- improve work efficiency
- testing facilitation
- saving time on using external applications (Postman / Insomnia)

Dependecies from `pom.xml`:
```xml
<dependency>
  <groupId>io.springfox</groupId>
  <artifactId>springfox-boot-starter</artifactId>
  <version>3.0.0</version>
</dependency>

<dependency>
  <groupId>io.springfox</groupId>
  <artifactId>springfox-swagger-ui</artifactId>
  <version>3.0.0</version>
</dependency>
```

Configuration is included in SwaggerConfig class. It has additional functionality to contain automatically the JWT token in each request made to API.
```java
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("agh.studentInternshipSupportSystem"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("The system supporting student internships")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }

}
```

### Project Lombok
Java library which automatically plugs into your editor and build tools. It replaces boilerplate code with easy to use annotations (constructors, getters, setters etc.).

Dependencies from `pom.xml` file:
```xml
<dependency>
  <groupId>org.projectlombok</groupId>
  <artifactId>lombok</artifactId>
  <optional>true</optional>
</dependency>
```

## Database
Dependencies from `pom.xml` file:
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <scope>runtime</scope>
</dependency>
```

Configuration file which is supported by Spring Boot - `application.properties`:
```properties
server.port = 9090
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/db_studentIntershipSupport?serverTimezone=UTC
spring.datasource.username=springuser
spring.datasource.password=ThePassword
```

## Frontend
Axios library is used for communication with server - as a HTTP client, unambiguously defines the way of information exchange.

From UI perspective - it is used one of the most common components library for Vue.js 3.0, [Element Plus](https://element-plus.org/#/en-US).

### Setting up
`npm install` - installing all modules which are defined in package.json.

`npm run serve` - launching the application (returns what is the local address).

### Addresses
- **Backend:** http://localhost:9090
- **Swagger:** http://localhost:9090/swagger-ui/index.html#/
- **Frontend:** https://localhost:8080

## Documentation
General documentation in Polish:
- [Design documentation](https://github.com/igordzierwa/student-internships-system-project/blob/main/documentation/doc-design.pdf)
- [Specification documentation](https://github.com/igordzierwa/student-internships-system-project/blob/main/documentation/doc-specification.pdf)
- [Process documentation](https://github.com/igordzierwa/student-internships-system-project/blob/main/documentation/doc-process.pdf)
- [Development documentation](https://github.com/igordzierwa/student-internships-system-project/blob/main/documentation/doc-development.pdf)
- [User documentation](https://github.com/igordzierwa/student-internships-system-project/blob/main/documentation/doc-user.pdf)

## Authors:
- Igor Dzierwa
- Adrian Nędza
- Konrad Makuch
