package org.example.mutant_detector.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DnaValidatorTest {

    private DnaValidator dnaValidator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dnaValidator = new DnaValidator();
    }

    @Test
    void testValidDna() {
        String[] dna = {"ATGC", "CAGT", "TTAT", "AGAC"};
        assertTrue(dnaValidator.isValid(dna, context));
    }

    @Test
    void testNullDna() {
        assertFalse(dnaValidator.isValid(null, context));
    }

    @Test
    void testEmptyDna() {
        assertFalse(dnaValidator.isValid(new String[]{}, context));
    }

    @Test
    void testNonSquareDna() {
        // 3 filas, pero longitud 4
        String[] dna = {"ATGC", "CAGT", "TTAT"};
        assertFalse(dnaValidator.isValid(dna, context));
    }

    @Test
    void testInvalidCharacters() {
        String[] dna = {"ATGX", "CAGT", "TTAT", "AGAC"}; // 'X' no es válida
        assertFalse(dnaValidator.isValid(dna, context));
    }

    @Test
    void testNullRowInDna() {
        String[] dna = {"ATGC", null, "TTAT", "AGAC"};
        assertFalse(dnaValidator.isValid(dna, context));
    }

    @Test
    void testNumbersInDna() {
        String[] dna = {"ATGC", "1234", "TTAT", "AGAC"};
        assertFalse(dnaValidator.isValid(dna, context));
    }
}