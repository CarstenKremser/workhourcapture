package de.carstenkremser.workhourcapture.backend.service;

import de.carstenkremser.workhourcapture.backend.dto.TimeBookingDto;
import de.carstenkremser.workhourcapture.backend.model.TimeRecord;
import de.carstenkremser.workhourcapture.backend.repository.TimeRecordRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TimeRecordServiceTest {

    private final IdGenerator mockIdGenerator = mock(IdGenerator.class);
    private final TimeGenerator mockTimeGenerator = mock(TimeGenerator.class);
    private final TimeRecordRepository mockTimeRecordRepository = mock(TimeRecordRepository.class);

    @Test
    void addTimeRecord_insertProvidedValues_whenCalledWithTime() {

        final Instant aTime = Instant.now();
        final String aRecordType = "workstart";
        final String anId = "id-xx-xx-xx";
        final String anUserId = "userId-xyz";
        final String aTimeZone = "Europe/Berlin";
        final int anOffset = -120;

        final TimeBookingDto bookingDto = new TimeBookingDto(anUserId, aRecordType, aTime, anOffset, aTimeZone);
        final TimeRecord timeRecord = new TimeRecord(anId, aTime, anUserId, aTimeZone, (anOffset * -1));
        when(mockIdGenerator.createId()).thenReturn(anId);
        when(mockTimeGenerator.createInstantNow()).thenReturn(aTime);
        when(mockTimeRecordRepository.insert(timeRecord)).thenReturn(timeRecord);

        TimeRecordService timeRecordService = new TimeRecordService(
                mockTimeRecordRepository, mockIdGenerator, mockTimeGenerator);

        TimeRecord actual = timeRecordService.addTimeRecord(bookingDto);

        assertNotNull(actual);
        assertEquals(anId, actual.id());
        assertEquals(aTime, actual.time());
        assertEquals(anUserId, actual.userId());
        assertEquals(aTimeZone, actual.timeZone());
        assertEquals(anOffset * -1, actual.timeZoneOffset());
    }
    @Test
    void addTimeRecord_insertProvidedAndGeneratedValues_whenCalledWithoutTime() {

        final Instant aTime = Instant.now();
        final String aRecordType = "workstart";
        final String anId = "id-xx-xx-xx";
        final String anUserId = "userId-xyz";
        final String aTimeZone = "Europe/Berlin";
        final int anOffset = -120;

        final TimeBookingDto bookingDto = new TimeBookingDto(anUserId, aRecordType, null, anOffset, aTimeZone);
        final TimeRecord timeRecord = new TimeRecord(anId, aTime, anUserId, aTimeZone, (anOffset * -1));
        when(mockIdGenerator.createId()).thenReturn(anId);
        when(mockTimeGenerator.createInstantNow()).thenReturn(aTime);
        when(mockTimeRecordRepository.insert(timeRecord)).thenReturn(timeRecord);

        TimeRecordService timeRecordService = new TimeRecordService(
                mockTimeRecordRepository, mockIdGenerator, mockTimeGenerator);

        TimeRecord actual = timeRecordService.addTimeRecord(bookingDto);

        assertNotNull(actual);
        assertEquals(anId, actual.id());
        assertEquals(aTime, actual.time());
        assertEquals(anUserId, actual.userId());
        assertEquals(aTimeZone, actual.timeZone());
        assertEquals(anOffset * -1, actual.timeZoneOffset());
    }
}