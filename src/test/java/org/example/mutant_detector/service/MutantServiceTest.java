package org.example.mutant_detector.service;

import org.example.mutant_detector.dto.StatsResponse;
import org.example.mutant_detector.entity.DnaRecord;
import org.example.mutant_detector.repository.DnaRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MutantServiceTest {

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @Mock
    private MutantDetector mutantDetector;

    @InjectMocks
    private MutantService mutantService;

    @Test
    void analyzeDna_ExistingMutant_ReturnsTrueFromCache() {
        DnaRecord record = new DnaRecord();
        record.setMutant(true);
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.of(record));

        boolean result = mutantService.analyzeDna(new String[]{"AAAA", "CCCC", "TCAG", "GGTC"});

        assertTrue(result);
        verify(mutantDetector, never()).isMutant(any());
    }

    @Test
    void analyzeDna_NewMutant_SavesAndReturnsTrue() {
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(any())).thenReturn(true);

        boolean result = mutantService.analyzeDna(new String[]{"AAAA", "CCCC", "TCAG", "GGTC"});

        assertTrue(result);
        verify(dnaRecordRepository).save(any(DnaRecord.class));
    }

    @Test
    void analyzeDna_NewHuman_SavesAndReturnsFalse() {
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(any())).thenReturn(false);

        boolean result = mutantService.analyzeDna(new String[]{"AAAT", "CCCG", "TCAG", "GGTC"});

        assertFalse(result);
        verify(dnaRecordRepository).save(any(DnaRecord.class));
    }

    @Test
    void getStats_ReturnsCorrectRatio() {
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(40L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(100L);

        // CORREGIDO: Pasamos null, null para simular la llamada sin filtros de fecha
        StatsResponse stats = mutantService.getStats(null, null);

        assertEquals(40, stats.getCountMutantDna());
        assertEquals(100, stats.getCountHumanDna());
        assertEquals(0.4, stats.getRatio());
    }

    @Test
    void getStats_NoHumans_ReturnsZeroRatio_Or_MutantCount() {
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(10L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(0L);

        StatsResponse stats = mutantService.getStats(null, null);

        assertEquals(0, stats.getRatio());
    }
}