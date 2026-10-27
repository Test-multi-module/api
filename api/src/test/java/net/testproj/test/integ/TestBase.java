package net.testproj.test.integ;

import net.testproj.api.ApiApplication;
import org.flywaydb.core.Flyway;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("integ-test")
public abstract class TestBase {

    @SuppressWarnings("resource")
    static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("testproj_db")
                    .withUsername("adm")
                    .withPassword("qwerty");

    // Shared by all subclasses of this base in one test JVM; data persists across test classes.
    static {
        postgres.start();
        migrateSchema("auth", "classpath:db/migration/auth");
        migrateSchema("api", "classpath:db/migration/api");
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
