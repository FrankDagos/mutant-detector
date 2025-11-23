package org.example.mutant_detector.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class DnaValidator implements ConstraintValidator<ValidDna, String[]> {

    private static final Pattern VALID_CHARACTERS = Pattern.compile("^[ATCG]+$");

    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {
        if (dna == null) return false;
        if (dna.length == 0) return false;

        int n = dna.length;
        for (String row : dna) {
            // Verifica que no sea null
            if (row == null) return false;
            // Verifica que sea cuadrada (NxN)
            if (row.length() != n) return false;
            // Verifica caracteres válidos (Regex)
            if (!VALID_CHARACTERS.matcher(row).matches()) return false;
        }
        return true;
    }
}