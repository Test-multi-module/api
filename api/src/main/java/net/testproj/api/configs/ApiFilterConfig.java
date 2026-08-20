package net.testproj.api.configs;

import net.testproj.api.filters.ProfileCompletedFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiFilterConfig {
    @Bean
    public FilterRegistrationBean<ProfileCompletedFilter> profileCompletedFilterRegistration(
            ProfileCompletedFilter filter) {
        FilterRegistrationBean<ProfileCompletedFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }
}
