package net.testproj.test.api;

import net.testproj.api.ApiApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(
        basePackages = {
                "net.testproj.api",
                "net.testproj.common",
                "net.testproj.db"
        },
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = ApiApplication.class
        )
)
@ConfigurationPropertiesScan(basePackages = {
        "net.testproj.api.properties"
})
public class ApiTestApplication {
}
