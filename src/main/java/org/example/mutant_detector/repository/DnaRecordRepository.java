package org.example.mutant_detector.repository;

import org.example.mutant_detector.entity.DnaRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface DnaRecordRepository extends JpaRepository<DnaRecord, Long> {

    // Busca por hash (para el caché)
    Optional<DnaRecord> findByDnaHash(String dnaHash);

    // Cuenta mutantes o humanos (para stats)
    long countByIsMutant(boolean isMutant);

    // Método para borrar por Hash
    void deleteByDnaHash(String dnaHash);

    // Contar con rango de fechas
    long countByIsMutantAndCreatedAtBetween(boolean isMutant, LocalDateTime start, LocalDateTime end);
}

