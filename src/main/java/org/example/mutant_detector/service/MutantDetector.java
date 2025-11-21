package org.example.mutant_detector.service;

import org.springframework.stereotype.Component;

@Component
public class MutantDetector {

    // Constante para la longitud de la secuencia requerida
    private static final int SEQUENCE_LENGTH = 4;

    public boolean isMutant(String[] dna) {
        // Validaciones básicas (se reforzarán en el DTO, pero el core debe ser defensivo)
        if (dna == null || dna.length == 0) return false;

        int n = dna.length;
        int sequenceCount = 0;

        // Optimización 1: Convertir a char[][] para acceso rápido O(1)
        char[][] matrix = new char[n][n];
        for (int i = 0; i < n; i++) {
            matrix[i] = dna[i].toCharArray();
        }

        // Recorremos la matriz UNA sola vez (Single Pass)
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {

                // Optimización 2: Boundary Checking (Solo buscar si hay espacio)

                // Búsqueda Horizontal
                if (col <= n - SEQUENCE_LENGTH) {
                    if (checkHorizontal(matrix, row, col)) {
                        sequenceCount++;
                        // Optimización 3: Early Termination (Si ya encontramos más de 1, salir)
                        if (sequenceCount > 1) return true;
                    }
                }

                // Búsqueda Vertical
                if (row <= n - SEQUENCE_LENGTH) {
                    if (checkVertical(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                    }
                }

                // Búsqueda Diagonal (↘)
                if (row <= n - SEQUENCE_LENGTH && col <= n - SEQUENCE_LENGTH) {
                    if (checkDiagonal(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                    }
                }

                // Búsqueda Diagonal Inversa (↗)
                if (row >= SEQUENCE_LENGTH - 1 && col <= n - SEQUENCE_LENGTH) {
                    if (checkCounterDiagonal(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                    }
                }
            }
        }
        return false;
    }

    // Optimización 4: Comparación directa sin bucles internos para máxima velocidad
    private boolean checkHorizontal(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return base == matrix[row][col+1] &&
                base == matrix[row][col+2] &&
                base == matrix[row][col+3];
    }

    private boolean checkVertical(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return base == matrix[row+1][col] &&
                base == matrix[row+2][col] &&
                base == matrix[row+3][col];
    }

    private boolean checkDiagonal(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return base == matrix[row+1][col+1] &&
                base == matrix[row+2][col+2] &&
                base == matrix[row+3][col+3];
    }

    private boolean checkCounterDiagonal(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return base == matrix[row-1][col+1] &&
                base == matrix[row-2][col+2] &&
                base == matrix[row-3][col+3];
    }
}