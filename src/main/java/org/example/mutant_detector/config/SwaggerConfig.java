package org.example.mutant_detector.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server; // <--- Importante
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(new Server().url("/").description("Default Server URL")))
                .info(new Info()
                        .title("Mutant Detector API")
                        .version("1.0")
                        .description("API para detectar mutantes basada en análisis de secuencias de ADN. Permite identificar si una secuencia de ADN pertenece a un mutante o a un humano, y proporciona estadísticas sobre las detecciones realizadas.")
                        .contact(new Contact()
                                .name("Franco")
                                .email("francodagostino20@gmail.com")));
    }
}
