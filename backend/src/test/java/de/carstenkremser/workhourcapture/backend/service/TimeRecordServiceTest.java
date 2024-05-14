package de.carstenkremser.workhourcapture.backend.service;

import de.carstenkremser.workhourcapture.backend.dto.TimeBookingDto;
import de.carstenkremser.workhourcapture.backend.model.TimeRecord;
import de.carstenkremser.workhourcapture.backend.repository.TimeRecordRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TimeRecordServiceTest {

    private final IdGenerator mockIdGenerator = mock(IdGenerator.class);
    private final TimeGenerator mockTimeGenerator = mock(TimeGenerator.class);
    private final TimeRecordRepository mockTimeRecordRepository = mock(TimeRecordRepository.class);

    @Test
    void addTimeRecord_insertProvidedValues_whenCalledWithTime() {

        final String aTimeZone = "Europe/Berlin";
        final Instant aTime = Instant.now();
        final LocalDateTime aLocalDateTime = LocalDateTime.ofInstant(aTime, ZoneId.systemDefault());
        final ZonedDateTime aZonedDateTime = aLocalDateTime.atZone(ZoneId.of(aTimeZone));
        final String aRecordType = "workstart";
        final String anId = "id-xx-xx-xx";
        final String anUserId = "userId-xyz";
        final int anOffset = -120;

        final TimeBookingDto bookingDto = new TimeBookingDto(anUserId, aRecordType, aTime, anOffset, aTimeZone);
        final TimeRecord timeRecord = new TimeRecord(anId, aRecordType, aLocalDateTime, anUserId, aTimeZone, (anOffset * -1));
        when(mockIdGenerator.createId()).thenReturn(anId);
        when(mockTimeGenerator.createLocalDateTimeNow()).thenReturn(aLocalDateTime);
        when(mockTimeRecordRepository.insert(timeRecord)).thenReturn(timeRecord);

        TimeRecordService timeRecordService = new TimeRecordService(
                mockTimeRecordRepository, mockIdGenerator, mockTimeGenerator);

        TimeRecord actual = timeRecordService.addTimeRecord(bookingDto);

        assertNotNull(actual);
        assertEquals(anId, actual.id());
        assertEquals(aRecordType, actual.recordType());
        assertEquals(aLocalDateTime, actual.dateTime());
        assertEquals(anUserId, actual.userId());
        assertEquals(aTimeZone, actual.timeZone());
        assertEquals(anOffset * -1, actual.timeZoneOffset());
    }
    @Test
    void addTimeRecord_insertProvidedAndGeneratedValues_whenCalledWithoutTime() {

        final String aTimeZone = "Europe/Berlin";
        final Instant aTime = Instant.now();
        final LocalDateTime aLocalDateTime = LocalDateTime.ofInstant(aTime, ZoneId.of(aTimeZone));
        final ZonedDateTime aZonedDateTime = aLocalDateTime.atZone(ZoneId.of(aTimeZone));
        final String aRecordType = "workstart";
        final String anId = "id-xx-xx-xx";
        final String anUserId = "userId-xyz";
        final int anOffset = -120;

        final TimeBookingDto bookingDto = new TimeBookingDto(anUserId, aRecordType, null, anOffset, aTimeZone);
        final TimeRecord timeRecord = new TimeRecord(anId, aRecordType, aLocalDateTime, anUserId, aTimeZone, (anOffset * -1));
        when(mockIdGenerator.createId()).thenReturn(anId);
        when(mockTimeGenerator.createLocalDateTimeNow()).thenReturn(aLocalDateTime);
        when(mockTimeRecordRepository.insert(timeRecord)).thenReturn(timeRecord);

        TimeRecordService timeRecordService = new TimeRecordService(
                mockTimeRecordRepository, mockIdGenerator, mockTimeGenerator);

        TimeRecord actual = timeRecordService.addTimeRecord(bookingDto);

        assertNotNull(actual);
        assertEquals(anId, actual.id());
        assertEquals(aRecordType, actual.recordType());
        assertEquals(aLocalDateTime, actual.dateTime());
        assertEquals(anUserId, actual.userId());
        assertEquals(aTimeZone, actual.timeZone());
        assertEquals(anOffset * -1, actual.timeZoneOffset());
    }
}