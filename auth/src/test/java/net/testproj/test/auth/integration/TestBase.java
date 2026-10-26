package net.testproj.test.auth.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.testproj.db.auth.AuthUserDS;
import net.testproj.db.auth.OAuth2AccountDS;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

@SpringBootTest(classes = IntegrationTestApplication.class)
@ActiveProfiles("integration-test")
@AutoConfigureMockMvc
public abstract class TestBase {

    @Autowired protected MockMvc mockMvc;
    @Autowired protected OAuth2AccountDS oAuth2AccountDS;
    @Autowired protected AuthUserDS authUserDS;
    protected final ObjectMapper objectMapper = new ObjectMapper();


    @SuppressWarnings("resource")
    static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("testproj_db")
                    .withUsername("adm")
                    .withPassword("qwerty");

    // One PostgreSQL container is shared by all integration tests in this JVM process.
    // Data persists across test classes: use unique test data and do not rely on test execution order.
    static {
        postgres.start();
        migrateSchema("auth", "classpath:db/migration/auth");
    }

    private static void migrateSchema(String schema, String location) {
        Flyway.configure()
                .dataSource(postgres.getJdbcUrl(),postgres.getUsername(), postgres.getPassword())
                .defaultSchema(schema)
                .schemas(schema)
                .locations(location)
                .createSchemas(true)
                .cleanDisabled(true)
                .load().migrate();
    }

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.flyway.enabled", () -> false);
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);
    }
}