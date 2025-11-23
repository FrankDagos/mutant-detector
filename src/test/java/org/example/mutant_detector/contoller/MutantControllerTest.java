package org.example.mutant_detector.contoller;

import org.example.mutant_detector.service.MutantService;
import org.example.mutant_detector.dto.StatsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MutantService mutantService;

    @Test
    void testMutant_Returns200() throws Exception {
        when(mutantService.analyzeDna(any())).thenReturn(true);

        String json = "{\"dna\":[\"AAAA\",\"CCCC\",\"TCAG\",\"GGTC\"]}";

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testHuman_Returns403() throws Exception {
        when(mutantService.analyzeDna(any())).thenReturn(false);

        String json = "{\"dna\":[\"AAAT\",\"CCCG\",\"TCAG\",\"GGTC\"]}";

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void testInvalidDna_Returns400() throws Exception {
        // Matriz no cuadrada o caracteres inválidos
        String json = "{\"dna\":[\"AAAX\",\"CCCG\"]}";

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testStats_Returns200() throws Exception {
        StatsResponse stats = new StatsResponse(40, 100, 0.4);
        when(mutantService.getStats()).thenReturn(stats);

        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").value(40))
                .andExpect(jsonPath("$.count_human_dna").value(100))
                .andExpect(jsonPath("$.ratio").value(0.4));
    }
}