To run an embedded database with Liquibase in a Spring Boot test, you typically need to configure your test environment so that it uses an in-memory database, such as H2, and ensure that Liquibase runs its migrations before your tests are executed. Here’s how you can do this:

### 1. **Add Dependencies**
Ensure that you have the necessary dependencies in your `pom.xml` or `build.gradle` file:

#### Maven:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

#### Gradle:
```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    implementation 'org.liquibase:liquibase-core'
    testImplementation 'com.h2database:h2'
}
```

### 2. **Configure Application Properties**
Create a `application-test.yml` or `application-test.properties` file (depending on your preferred format) in the `src/test/resources` directory to configure the test database.

#### `application-test.yml`:
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
    username: sa
    password:
  liquibase:
    change-log: classpath:/db/changelog/db.changelog-master.xml
```

#### `application-test.properties`:
```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.liquibase.change-log=classpath:/db/changelog/db.changelog-master.xml
```

### 3. **Create a Test Class**
Annotate your test class with `@SpringBootTest` to load the full application context and `@ActiveProfiles("test")` to activate the test profile. Spring Boot will automatically set up the embedded H2 database and run Liquibase migrations before your tests are executed.

```java
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class YourSpringBootTest {

    @Test
    void contextLoads() {
        // Your test code here
    }
}
```

### 4. **Run Your Test**
When you run your tests, Spring Boot will use the in-memory H2 database, and Liquibase will apply the migrations defined in your changelog file.

### Optional: **Liquibase Rollback Test**
If you want to test rollback scenarios, you can configure Liquibase to rollback changes after each test:

```java
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import liquibase.integration.spring.SpringLiquibase;

@SpringBootTest
@ActiveProfiles("test")
class LiquibaseRollbackTest {

    @Autowired
    private SpringLiquibase liquibase;

    @AfterEach
    void rollback() throws Exception {
        liquibase.getDatabase().getConnection().rollback();
    }

    @Test
    void testWithLiquibase() {
        // Your test code here
    }
}
```

This setup allows you to run an embedded database with Liquibase in a Spring Boot test environment, ensuring your database schema is correctly initialized before your tests run.