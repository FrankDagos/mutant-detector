package org.example.mutant_detector.exception;

public class DnaHashCalculationException extends RuntimeException {
    public DnaHashCalculationException(String message, Throwable cause) {
        super(message, cause);
    }
}