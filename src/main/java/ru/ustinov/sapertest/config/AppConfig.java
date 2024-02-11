package ru.ustinov.sapertest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.ustinov.sapertest.json.CharArraySerializer;

/**
 * //TODO add comments.
 *
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 11.02.2024
 */
@Configuration
@Slf4j
public class AppConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(char[].class, new CharArraySerializer());
        return new ObjectMapper().registerModule(module);
    }


}
