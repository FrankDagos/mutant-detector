package org.example.mutant_detector.service;

import lombok.extern.slf4j.Slf4j; // Importar Lombok Log
import org.springframework.stereotype.Component;

@Component
@Slf4j // Habilita la variable 'log'
public class MutantDetector {

    private static final int SEQUENCE_LENGTH = 4;

    public boolean isMutant(String[] dna) {
        if (dna == null || dna.length == 0) return false;

        int n = dna.length;
        int sequenceCount = 0;

        // Convertimos a char[][] para mayor rendimiento
        char[][] matrix = new char[n][n];
        for (int i = 0; i < n; i++) {
            matrix[i] = dna[i].toCharArray();
        }

        // Single Pass: Recorremos la matriz una sola vez
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {

                // 1. Horizontal
                if (col <= n - SEQUENCE_LENGTH) {
                    if (checkHorizontal(matrix, row, col)) {
                        sequenceCount++;
                        log.info("🧬 Secuencia Horizontal encontrada en fila {} col {}", row, col);
                        if (sequenceCount > 1) return true;
                    }
                }

                // 2. Vertical
                if (row <= n - SEQUENCE_LENGTH) {
                    if (checkVertical(matrix, row, col)) {
                        sequenceCount++;
                        log.info("🧬 Secuencia Vertical encontrada en fila {} col {}", row, col);
                        if (sequenceCount > 1) return true;
                    }
                }

                // 3. Diagonal Descendente (↘)
                if (row <= n - SEQUENCE_LENGTH && col <= n - SEQUENCE_LENGTH) {
                    if (checkDiagonal(matrix, row, col)) {
                        sequenceCount++;
                        log.info("🧬 Secuencia Diagonal (↘) encontrada en fila {} col {}", row, col);
                        if (sequenceCount > 1) return true;
                    }
                }

                // 4. Diagonal Ascendente (↗)
                if (row >= SEQUENCE_LENGTH - 1 && col <= n - SEQUENCE_LENGTH) {
                    if (checkCounterDiagonal(matrix, row, col)) {
                        sequenceCount++;
                        log.info("🧬 Secuencia Diagonal Inversa (↗) encontrada en fila {} col {}", row, col);
                        if (sequenceCount > 1) return true;
                    }
                }
            }
        }
        return false;
    }

    // Métodos auxiliares privados (se mantienen igual, pero los incluyo por completitud)
    private boolean checkHorizontal(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return base == matrix[row][col+1] && base == matrix[row][col+2] && base == matrix[row][col+3];
    }
    private boolean checkVertical(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return base == matrix[row+1][col] && base == matrix[row+2][col] && base == matrix[row+3][col];
    }
    private boolean checkDiagonal(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return base == matrix[row+1][col+1] && base == matrix[row+2][col+2] && base == matrix[row+3][col+3];
    }
    private boolean checkCounterDiagonal(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return base == matrix[row-1][col+1] && base == matrix[row-2][col+2] && base == matrix[row-3][col+3];
    }
}