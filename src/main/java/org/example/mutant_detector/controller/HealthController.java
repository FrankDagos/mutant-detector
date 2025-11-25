package org.example.mutant_detector.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "Health Check", description = "Endpoint para verificar el estado del sistema")
public class HealthController {

    @Operation(summary = "Verificar estado", description = "Devuelve estado UP si la API está funcionando correctamente.")
    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
                "status", "UP",
                "service", "Mutant Detector API",
                "version", "1.0.0"
        );
    }
}
