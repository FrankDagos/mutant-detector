package org.example.mutant_detector.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.mutant_detector.dto.DnaRequest;
import org.example.mutant_detector.dto.StatsResponse;
import org.example.mutant_detector.service.MutantService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Tag(name = "Mutant Detector", description = "API para la detección de mutantes mediante análisis de ADN")
public class MutantController {

    private final MutantService mutantService;

    @Operation(
            summary = "Detectar si un humano es mutante",
            description = "Analiza una secuencia de ADN. Devuelve 200 OK si es mutante, 403 Forbidden si es humano. Guarda el resultado en Base de Datos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Es Mutante", content = @Content),
            @ApiResponse(responseCode = "403", description = "Es Humano", content = @Content),
            @ApiResponse(responseCode = "400", description = "ADN Inválido (No cuadrado o caracteres erróneos)", content = @Content)
    })
    @PostMapping("/mutant")
    public ResponseEntity<Void> checkMutant(@Valid @RequestBody DnaRequest dnaRequest) {
        boolean isMutant = mutantService.analyzeDna(dnaRequest.getDna());
        if (isMutant) {
            return ResponseEntity.ok().build(); // 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
    }

    @Operation(summary = "Obtener estadísticas", description = "Devuelve estadísticas. Opcionalmente acepta rango de fechas (YYYY-MM-DD).")
    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(mutantService.getStats(startDate, endDate));
    }

    @Operation(summary = "Eliminar registro", description = "Elimina un registro de ADN de la base de datos usando su Hash.")
    @ApiResponse(responseCode = "200", description = "Registro eliminado")
    @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    @DeleteMapping("/mutant/{hash}")
    public ResponseEntity<Void> deleteMutant(@PathVariable String hash) {
        boolean deleted = mutantService.deleteMutant(hash);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}