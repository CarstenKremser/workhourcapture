package de.carstenkremser.workhourcapture.backend;

import de.carstenkremser.workhourcapture.backend.repository.TimeRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class TimeRecordIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TimeRecordRepository timeRecordRepository;

    @Test
    @DirtiesContext
    void addNow() throws Exception {
        final String userId = "ThisIsMyId";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/timerecord/addnow/" + userId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.time").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(userId));
        assertEquals(timeRecordRepository.findAll().size(), 1);
        assertEquals(timeRecordRepository.findAll().stream()
                        .filter((timeRecord) -> timeRecord.userId().equals(userId))
                        .count(),
                Long.valueOf(1));

    }
}