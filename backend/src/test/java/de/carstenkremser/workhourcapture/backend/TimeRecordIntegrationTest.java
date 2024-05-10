package de.carstenkremser.workhourcapture.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.carstenkremser.workhourcapture.backend.dto.TimeBookingDto;
import de.carstenkremser.workhourcapture.backend.repository.TimeRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class TimeRecordIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TimeRecordRepository timeRecordRepository;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DirtiesContext
    void add_whenCalledWithoutTime_insertsDataWithGeneratedTime() throws Exception {
        final String userId = "ThisIsMyId";
        final String recordType = "workstart";
        final String timeZone = "Europe/Berlin";
        final int timeZoneOffsetInMinutes = -120;
        final TimeBookingDto bookingDto = new TimeBookingDto(userId, recordType, null, timeZoneOffsetInMinutes, timeZone);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/timerecord/add")
                        .content(asJsonString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.time").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeZone").value(timeZone))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeZoneOffset").value(timeZoneOffsetInMinutes * -1))
        ;
        assertEquals(1, timeRecordRepository.findAll().size());
        assertEquals(Long.valueOf(1), timeRecordRepository.findAll()
                .stream()
                .filter((timeRecord) -> timeRecord.userId().equals(userId))
                .count());
        assertEquals(Long.valueOf(1), timeRecordRepository.findAll()
                .stream()
                .filter((timeRecord) -> (timeRecord.time() != null))
                .count());

    }

    @Test
    @DirtiesContext
    void add_whenCalledWithTime_insertsDataWithProvidedTime() throws Exception {
        final String userId = "ThisIsMyId";
        final String recordType = "workstart";
        final Instant now = Instant.now();
        final String timeZone = "Europe/Berlin";
        final int timeZoneOffsetInMinutes = -120;
        final TimeBookingDto bookingDto = new TimeBookingDto(userId, recordType, now, timeZoneOffsetInMinutes, timeZone);
        System.out.println(bookingDto.recordTime().toString());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/timerecord/add")
                        .content("{"+
                                        "\"userId\": \"" + bookingDto.userId() + "\"," +
                                        "\"recordType\": \"" + bookingDto.recordType() + "\"," +
                                        "\"recordTime\": \"" + bookingDto.recordTime() + "\"," +
                                        "\"timezoneOffset\": \"" + bookingDto.timezoneOffset() + "\"," +
                                        "\"timezoneName\": \"" + bookingDto.timezoneName() + "\"" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.time").value(now.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeZone").value(timeZone))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeZoneOffset").value(timeZoneOffsetInMinutes * -1))
        ;
        assertEquals(1, timeRecordRepository.findAll().size());
        assertEquals(Long.valueOf(1), timeRecordRepository.findAll()
                .stream()
                .filter((timeRecord) -> timeRecord.userId().equals(userId))
                .count());
        assertEquals(Long.valueOf(1), timeRecordRepository.findAll()
                .stream()
                .filter((timeRecord) -> (timeRecord.time() != null))
                .count());
    }

}