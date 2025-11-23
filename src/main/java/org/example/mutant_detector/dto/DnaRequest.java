package org.example.mutant_detector.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.mutant_detector.validation.ValidDna;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class DnaRequest implements Serializable {

    @ValidDna
    private String[] dna;
}