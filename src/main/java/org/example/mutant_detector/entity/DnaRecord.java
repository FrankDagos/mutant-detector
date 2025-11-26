package org.example.mutant_detector.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "dna_records", indexes = {
        @Index(name = "idx_dna_hash", columnList = "dna_hash")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DnaRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dna_hash", unique = true, nullable = false)
    private String dnaHash;

    @Column(name = "is_mutant", nullable = false)
    private boolean isMutant;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}