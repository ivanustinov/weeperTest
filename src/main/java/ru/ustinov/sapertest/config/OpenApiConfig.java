package ru.ustinov.sapertest.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 12.02.2024
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Игра Сапёр (Minesweeper)",
                version = "1.0",
                description = "Игра Сапёр (Minesweeper)",
                contact = @Contact(url = "https://github.com/ivanustinov", name = "Ivan Ustinov",
                        email = "ivanustinov1985@yandex.ru")
        )
)
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("REST API")
                .pathsToMatch("/**")
                .build();
    }
}
