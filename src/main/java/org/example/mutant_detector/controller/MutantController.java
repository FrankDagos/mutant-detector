package org.example.mutant_detector.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.mutant_detector.dto.DnaRequest;
import org.example.mutant_detector.dto.StatsResponse;
import org.example.mutant_detector.service.MutantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MutantController {

    private final MutantService mutantService; // Ahora inyectamos el Service, no el Detector directo

    @PostMapping("/mutant")
    public ResponseEntity<Void> checkMutant(@Valid @RequestBody DnaRequest dnaRequest) {
        boolean isMutant = mutantService.analyzeDna(dnaRequest.getDna());
        if (isMutant) {
            return ResponseEntity.ok().build(); // 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats() {
        return ResponseEntity.ok(mutantService.getStats()); // 200 OK con stats
    }
}
