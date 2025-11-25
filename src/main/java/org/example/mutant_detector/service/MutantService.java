package org.example.mutant_detector.service;

import lombok.RequiredArgsConstructor;
import org.example.mutant_detector.dto.StatsResponse;
import org.example.mutant_detector.entity.DnaRecord;
import org.example.mutant_detector.exception.DnaHashCalculationException;
import org.example.mutant_detector.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MutantService {

    private final DnaRecordRepository dnaRecordRepository;
    private final MutantDetector mutantDetector;

    public boolean analyzeDna(String[] dna) {
        // 1. Calcular Hash para identificar el ADN unívocamente
        String dnaHash = calculateHash(dna);

        // 2. Buscar en BD si ya existe (Caché)
        Optional<DnaRecord> existingRecord = dnaRecordRepository.findByDnaHash(dnaHash);
        if (existingRecord.isPresent()) {
            return existingRecord.get().isMutant();
        }

        // 3. Si no existe, calculamos con el algoritmo
        boolean isMutant = mutantDetector.isMutant(dna);

        // 4. Guardamos el resultado en BD
        DnaRecord newRecord = new DnaRecord();
        newRecord.setDnaHash(dnaHash);
        newRecord.setMutant(isMutant);
        dnaRecordRepository.save(newRecord);

        return isMutant;
    }

    public StatsResponse getStats(LocalDate startDate, LocalDate endDate) {
        long countMutant;
        long countHuman;

        if (startDate != null && endDate != null) {
            // Si hay fechas, filtramos (Desde el inicio del día start hasta el final del día end)
            LocalDateTime start = startDate.atStartOfDay();
            LocalDateTime end = endDate.atTime(LocalTime.MAX);

            countMutant = dnaRecordRepository.countByIsMutantAndCreatedAtBetween(true, start, end);
            countHuman = dnaRecordRepository.countByIsMutantAndCreatedAtBetween(false, start, end);
        } else {
            // Trae todo el histórico
            countMutant = dnaRecordRepository.countByIsMutant(true);
            countHuman = dnaRecordRepository.countByIsMutant(false);
        }

        double ratio = countHuman == 0 ? 0 : (double) countMutant / countHuman;
        return new StatsResponse(countMutant, countHuman, ratio);
    }

    // Método auxiliar para generar SHA-256
    private String calculateHash(String[] dna) {
        try {
            // Unimos el array en un solo String para hashear
            String joinedDna = String.join("", dna);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(joinedDna.getBytes());

            // Convertir bytes a Hexadecimal
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new DnaHashCalculationException("Error al calcular el hash del ADN", e);
        }
    }

    @Transactional // Importante para operaciones DELETE
    public boolean deleteMutant(String hash) {
        Optional<DnaRecord> existing = dnaRecordRepository.findByDnaHash(hash);
        if (existing.isPresent()) {
            dnaRecordRepository.deleteByDnaHash(hash);
            return true; // Borrado exitoso
        }
        return false; // No existía
    }
}