package org.example.mutant_detector.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class DnaValidator implements ConstraintValidator<ValidDna, String[]> {

    private static final Pattern VALID_CHARACTERS = Pattern.compile("^[ATCG]+$");
    // Límite de seguridad: Matrices mayores a 1000x1000 serán rechazadas
    private static final int MAX_DNA_SIZE = 1000;

    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {
        if (dna == null) return false;
        if (dna.length == 0) return false;

        // Validar tamaño máximo (Anti-DDoS)
        if (dna.length > MAX_DNA_SIZE) {
            return false;
        }

        int n = dna.length;
        for (String row : dna) {
            // Validar nulidad
            if (row == null) return false;
            // Validar NxN
            if (row.length() != n) return false;
            // Validar caracteres
            if (!VALID_CHARACTERS.matcher(row).matches()) return false;
        }
        return true;
    }
}