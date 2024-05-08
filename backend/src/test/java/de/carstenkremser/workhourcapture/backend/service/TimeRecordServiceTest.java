package de.carstenkremser.workhourcapture.backend.service;

import de.carstenkremser.workhourcapture.backend.model.TimeRecord;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TimeRecordServiceTest {

    private IdGenerator mockIdGenerator = mock(IdGenerator.class);
    private TimeGenerator mockTimeGenerator = mock(TimeGenerator.class);

    @Test
    void addTimeRecordForNow() {
        Instant aTime = Instant.now();
        when(mockIdGenerator.createId()).thenReturn("id-xx-xx-xx");
        when(mockTimeGenerator.createInstantNow()).thenReturn(aTime);

        TimeRecordService timeRecordService = new TimeRecordService(mockIdGenerator, mockTimeGenerator);

        TimeRecord actual = timeRecordService.addTimeRecordForNow("dummyUser");

        assertNotNull(actual);
        assertEquals("id-xx-xx-xx", actual.id());
        assertEquals(aTime, actual.time());
        assertEquals("dummyUser",actual.userId());
    }
}