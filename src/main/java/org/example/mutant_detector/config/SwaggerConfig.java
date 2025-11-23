package org.example.mutant_detector.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(new Server().url("/")))
                .info(new Info()
                        .title("Mutant Detector API")
                        .version("1.0")
                        .description("API para detectar mutantes basada en análisis de secuencias de ADN.")
                        .contact(new Contact()
                                .name("Franco")
                                .email("francodagostino20@gmail.com")));
    }
}