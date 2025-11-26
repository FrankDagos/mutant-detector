package org.example.mutant_detector.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DnaRecordTest {

    @Test
    void testDnaRecordEntity() {
        // Testear Constructor Vacío y Setters
        DnaRecord record1 = new DnaRecord();
        record1.setId(1L);
        record1.setDnaHash("hash123");
        record1.setMutant(true);

        // Testear Getters
        assertEquals(1L, record1.getId());
        assertEquals("hash123", record1.getDnaHash());
        assertTrue(record1.isMutant());

        // Testear onCreate (@PrePersist no se ejecuta directo sin JPA, pero podemos simularlo o ignorarlo)
        // Lo importante es la estructura de datos

        // Testear Equals y HashCode (Lombok @Data)
        DnaRecord record2 = new DnaRecord();
        record2.setId(1L);
        record2.setDnaHash("hash123");
        record2.setMutant(true);

        assertEquals(record1, record2);
        assertEquals(record1.hashCode(), record2.hashCode());

        // Testear Not Equals
        DnaRecord record3 = new DnaRecord();
        record3.setId(2L);

        assertNotEquals(record1, record3);

        // Testear toString (Lombok @Data)
        assertNotNull(record1.toString());
    }
}