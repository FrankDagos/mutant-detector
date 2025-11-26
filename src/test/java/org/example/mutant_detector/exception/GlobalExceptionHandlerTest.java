package org.example.mutant_detector.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleDnaHashCalculationException() {
        // Crear la excepción custom
        String errorMsg = "Error simulado de hash";
        DnaHashCalculationException exception = new DnaHashCalculationException(errorMsg, new RuntimeException("Causa raíz"));

        // Invocar al manejador directamente
        ResponseEntity<Map<String, Object>> response = handler.handleHashException(exception);

        // Verificar respuesta
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().get("status"));
        assertEquals(errorMsg, response.getBody().get("message"));
    }
}