package org.example.mutant_detector.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MutantDetectorTest {

    private MutantDetector mutantDetector;

    @BeforeEach
    void setUp() {
        mutantDetector = new MutantDetector();
    }

    // --- CASOS MUTANTES (Debe retornar TRUE) ---

    @Test
    void testMutantHorizontal() {
        String[] dna = {
                "AAAAAA",
                "CAGTGC",
                "CCCCGT",
                "AGAAGG",
                "CACCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna), "Debería detectar mutante por secuencias horizontales");
    }

    @Test
    void testMutantVertical() {
        String[] dna = {
                "GATCGG",
                "GCTCGG",
                "GCTCGG",
                "GCTCGG",
                "ACATCA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna), "Debería detectar mutante por secuencias verticales");
    }

    @Test
    void testMutantDiagonal() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna), "Debería detectar mutante por secuencias diagonales");
    }

    @Test
    void testMutantCounterDiagonal() {
        String[] dna = {
                "ATGCGA",
                "CAGTAC",
                "TTAAGT",
                "AGAAGG",
                "TTTTTA", // Agregada horizontal para asegurar >1 secuencia
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna), "Debería detectar mutante por diagonal inversa");
    }

    @Test
    void testMutantMixed() {
        String[] dnaMutant = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dnaMutant), "Debería detectar mutante mixto");
    }

    // --- CASOS HUMANOS (Debe retornar FALSE) ---

    @Test
    void testHumanNoSequence() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATTT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna), "Humano sin secuencias debe ser false");
    }

    @Test
    void testHumanOneSequenceOnly() {
        // "AAAAAA" tiene 3 secuencias de 4 superpuestas (0-3, 1-4, 2-5).
        String[] dna = {
                "AAAAGA",
                "CAGTGC",
                "GTATTT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna), "Humano con SOLO UNA secuencia debe ser false");
    }

    // --- CASOS DE VALIDACIÓN Y ERRORES ---

    @Test
    void testNullDna() {
        assertFalse(mutantDetector.isMutant(null));
    }

    @Test
    void testEmptyDna() {
        assertFalse(mutantDetector.isMutant(new String[]{}));
    }

    @Test
    void testSmallMatrix() {
        String[] dna = {
                "ATG",
                "CAG",
                "TTA"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void testMutant4x4() {
        // Caso borde: Matriz mínima (4x4) con secuencias
        String[] dna = {
                "AAAA", // Horizontal
                "CCCC", // Horizontal
                "TCAG",
                "GGTC"
        };
        assertTrue(mutantDetector.isMutant(dna), "Debe detectar mutante en matriz mínima 4x4");
    }

    @Test
    void testHuman4x4() {
        // Caso borde: Matriz mínima (4x4) humana
        String[] dna = {
                "AAAT",
                "CCCG",
                "TCAG",
                "GGTC"
        };
        assertFalse(mutantDetector.isMutant(dna), "No debe detectar mutante en matriz mínima 4x4 sin secuencias suficientes");
    }

    @Test
    void testMutantCross() {
        // Cruz de secuencias (comparte una letra central)
        String[] dna = {
                "GTATG",
                "GTATG",
                "AAAAA", // Horizontal
                "GTATG",
                "GTATG"
        };
        // Hay horizontal en fila 2 y verticales en col 0, 2, 4... es mutante
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void testInvalidNumbers() {
        // Números en vez de letras
        String[] dna = {
                "1234",
                "CAGT",
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna), "Debe rechazar números");
    }

    @Test
    void testInvalidLowerCase() {
        
        String[] dna = {
                "acgt",
                "tgca",
                "cagt",
                "gtca"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void testMutantDiagonalReverseShort() {

        String[] dna = {
                "ATGCA",
                "CAGTA",
                "TTGTA",
                "AGAAG",
                "CCCCT"
        };

        assertFalse(mutantDetector.isMutant(dna));
    }
}