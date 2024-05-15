package de.carstenkremser.workhourcapture.backend.service;

import de.carstenkremser.workhourcapture.backend.dto.TimeBookingDto;
import de.carstenkremser.workhourcapture.backend.model.TimeRecord;
import de.carstenkremser.workhourcapture.backend.model.TimeRecordType;
import de.carstenkremser.workhourcapture.backend.model.WorkingTime;
import de.carstenkremser.workhourcapture.backend.repository.TimeRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
        final TimeRecordType aRecordType = TimeRecordType.WORKSTART;
        final String anId = "id-xx-xx-xx";
        final String anUserId = "userId-xyz";
        final int anOffset = -120;

        final TimeBookingDto bookingDto = new TimeBookingDto(anUserId, aRecordType.getValue(), aTime, anOffset, aTimeZone);
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
        final TimeRecordType aRecordType = TimeRecordType.WORKSTART;
        final String anId = "id-xx-xx-xx";
        final String anUserId = "userId-xyz";
        final int anOffset = -120;

        final TimeBookingDto bookingDto = new TimeBookingDto(anUserId, aRecordType.getValue(), null, anOffset, aTimeZone);
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
    void getTimeRecordsForMonth_havingTimeRecordsWithinAMonth() {
        List<TimeRecord> timeRecords = List.of(
                new TimeRecord("ID1", TimeRecordType.WORKSTART,
                        LocalDateTime.of(2023, 12, 4, 8, 17, 32),
                        "defaultUser", "Europe/Berlin", 120),
                new TimeRecord("ID2", TimeRecordType.WORKEND,
                        LocalDateTime.of(2023, 12, 4, 13, 21, 14),
                        "defaultUser", "Europe/Berlin", 120),
                new TimeRecord("ID3", TimeRecordType.WORKEND,
                        LocalDateTime.of(2023, 12, 5, 17, 2, 18),
                        "defaultUser", "Europe/Berlin", 120),
                new TimeRecord("ID4", TimeRecordType.WORKSTART,
                        LocalDateTime.of(2023, 12, 6, 9, 1, 7),
                        "defaultUser", "Europe/Berlin", 120)
        );
        when(mockTimeRecordRepository
                .findAllByUserIdAndDateTimeBetween(
                        anyString(),
                        any(LocalDateTime.class),
                        any(LocalDateTime.class)
                ))
                .thenReturn(timeRecords);

        TimeRecordService timeRecordService = new TimeRecordService(
                mockTimeRecordRepository, mockIdGenerator, mockTimeGenerator);
        List<TimeRecord> actual = timeRecordService.getTimeRecordsForMonth("defaultUser", YearMonth.of(2023, 12));

        assertNotNull(actual);
        assertEquals(4, actual.size());

    }

    @Test
    void getWorkingTimeForMonth_havingNoTimeRecords() {
        List<TimeRecord> timeRecords = new ArrayList<>();
        when(mockTimeRecordRepository
                .findAllByUserIdAndDateTimeBetween(
                        anyString(),
                        any(LocalDateTime.class),
                        any(LocalDateTime.class)
                ))
                .thenReturn(timeRecords);

        TimeRecordService timeRecordService = new TimeRecordService(
                mockTimeRecordRepository, mockIdGenerator, mockTimeGenerator);
        List<WorkingTime> actual = timeRecordService.getWorkingTimeForMonth("defaultUser", YearMonth.of(2023, 12));

        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    @Test
    void getWorkingTimeForMonth_havingTimeRecordsWithinAMonth() {
        List<TimeRecord> timeRecords = List.of(
                new TimeRecord("ID1", TimeRecordType.WORKSTART,
                        LocalDateTime.of(2023, 12, 4, 8, 17, 32),
                        "defaultUser", "Europe/Berlin", 120),
                new TimeRecord("ID2", TimeRecordType.WORKEND,
                        LocalDateTime.of(2023, 12, 4, 13, 21, 14),
                        "defaultUser", "Europe/Berlin", 120),
                new TimeRecord("ID3", TimeRecordType.WORKEND,
                        LocalDateTime.of(2023, 12, 5, 17, 2, 18),
                        "defaultUser", "Europe/Berlin", 120),
                new TimeRecord("ID4", TimeRecordType.WORKSTART,
                        LocalDateTime.of(2023, 12, 6, 9, 1, 7),
                        "defaultUser", "Europe/Berlin", 120),
                new TimeRecord("ID5", TimeRecordType.WORKSTART,
                        LocalDateTime.of(2023, 12, 7, 8, 55, 54),
                        "defaultUser", "Europe/Berlin", 120),
                new TimeRecord("ID6", TimeRecordType.WORKSTART,
                        LocalDateTime.of(2023, 12, 8, 8, 37, 45),
                        "defaultUser", "Europe/Berlin", 120),
                new TimeRecord("ID7", TimeRecordType.WORKEND,
                        LocalDateTime.of(2023, 12, 8, 14, 20, 29),
                        "defaultUser", "Europe/Berlin", 120)
        );
        when(mockTimeRecordRepository
                .findAllByUserIdAndDateTimeBetween(
                        anyString(),
                        any(LocalDateTime.class),
                        any(LocalDateTime.class)
                ))
                .thenReturn(timeRecords);

        TimeRecordService timeRecordService = new TimeRecordService(
                mockTimeRecordRepository, mockIdGenerator, mockTimeGenerator);
        List<WorkingTime> actual = timeRecordService.getWorkingTimeForMonth("defaultUser", YearMonth.of(2023, 12));

        assertNotNull(actual);
        assertEquals(5, actual.size());
        assertNotNull(actual.get(0).workStart());
        assertNotNull(actual.get(0).workEnd());
        assertNull(actual.get(1).workStart());
        assertNotNull(actual.get(1).workEnd());
        assertNotNull(actual.get(2).workStart());
        assertNull(actual.get(2).workEnd());
        assertNotNull(actual.get(3).workStart());
        assertNull(actual.get(3).workEnd());
        assertNotNull(actual.get(4).workStart());
        assertNotNull(actual.get(4).workEnd());
    }

    @Test
    void getWorkingTimeForMonth_havingCompleteTimeRecordsAtTheBoundariesOfTheCurrentMonth() {
        TimeRecord timeRecordBefore =
                new TimeRecord("IDB", TimeRecordType.WORKSTART,
                        LocalDateTime.of(2023, 11, 30, 22, 30, 0),
                        "defaultUser", "Europe/Berlin", 120);
        List<TimeRecord> timeRecords = List.of(
                new TimeRecord("ID0", TimeRecordType.WORKEND,
                        LocalDateTime.of(2023, 12, 1, 3, 30, 0),
                        "defaultUser", "Europe/Berlin", 120),

                new TimeRecord("ID3", TimeRecordType.WORKSTART,
                        LocalDateTime.of(2023, 12, 5, 9, 1, 7),
                        "defaultUser", "Europe/Berlin", 120),
                new TimeRecord("ID4", TimeRecordType.WORKEND,
                        LocalDateTime.of(2023, 12, 5, 17, 2, 18),
                        "defaultUser", "Europe/Berlin", 120),

                // Working time crossing month boundary:
                new TimeRecord("ID5", TimeRecordType.WORKSTART,
                        LocalDateTime.of(2023, 12, 31, 21, 0, 0),
                        "defaultUser", "Europe/Berlin", 120)
        );
        TimeRecord timeRecordAfter =
                new TimeRecord("IDA", TimeRecordType.WORKEND,
                        LocalDateTime.of(2024, 1, 1, 5, 0, 0),
                        "defaultUser", "Europe/Berlin", 120);

        when(mockTimeRecordRepository
                .findAllByUserIdAndDateTimeBeforeOrderByDateTimeDesc(
                        anyString(),
                        any(LocalDateTime.class),
                        any(Pageable.class)
                ))
                .thenReturn(List.of(timeRecordBefore));
        when(mockTimeRecordRepository
                .findAllByUserIdAndDateTimeAfterOrderByDateTimeAsc(
                        anyString(),
                        any(LocalDateTime.class),
                        any(Pageable.class)
                ))
                .thenReturn(List.of(timeRecordAfter));
        when(mockTimeRecordRepository
                .findAllByUserIdAndDateTimeBetween(
                        anyString(),
                        any(LocalDateTime.class),
                        any(LocalDateTime.class)
                ))
                .thenReturn(timeRecords);

        TimeRecordService timeRecordService = new TimeRecordService(
                mockTimeRecordRepository, mockIdGenerator, mockTimeGenerator);
        List<WorkingTime> actual = timeRecordService.getWorkingTimeForMonth("defaultUser", YearMonth.of(2023, 12));

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertNotNull(actual.get(0).workStart());
        assertNotNull(actual.get(0).workEnd());
        assertNotNull(actual.get(1).workStart());
        assertNotNull(actual.get(1).workEnd());
    }

    @Test
    void getWorkingTimeForMonth_havingIncompleteTimeRecordsAtTheBoundariesOfTheCurrentMonth() {
        TimeRecord timeRecordBefore =
                new TimeRecord("IDB", TimeRecordType.WORKEND,
                        LocalDateTime.of(2023, 11, 30, 22, 30, 0),
                        "defaultUser", "Europe/Berlin", 120);
        List<TimeRecord> timeRecords = List.of(
                new TimeRecord("ID0", TimeRecordType.WORKEND,
                        LocalDateTime.of(2023, 12, 1, 3, 30, 0),
                        "defaultUser", "Europe/Berlin", 120),

                new TimeRecord("ID3", TimeRecordType.WORKSTART,
                        LocalDateTime.of(2023, 12, 5, 9, 1, 7),
                        "defaultUser", "Europe/Berlin", 120),
                new TimeRecord("ID4", TimeRecordType.WORKEND,
                        LocalDateTime.of(2023, 12, 5, 17, 2, 18),
                        "defaultUser", "Europe/Berlin", 120),

                // Working time crossing month boundary:
                new TimeRecord("ID5", TimeRecordType.WORKSTART,
                        LocalDateTime.of(2023, 12, 31, 21, 0, 0),
                        "defaultUser", "Europe/Berlin", 120)
        );
        TimeRecord timeRecordAfter =
                new TimeRecord("IDA", TimeRecordType.WORKSTART,
                        LocalDateTime.of(2024, 1, 1, 5, 0, 0),
                        "defaultUser", "Europe/Berlin", 120);

        when(mockTimeRecordRepository
                .findAllByUserIdAndDateTimeBeforeOrderByDateTimeDesc(
                        anyString(),
                        any(LocalDateTime.class),
                        any(Pageable.class)
                ))
                .thenReturn(List.of(timeRecordBefore));
        when(mockTimeRecordRepository
                .findAllByUserIdAndDateTimeBetween(
                        anyString(),
                        any(LocalDateTime.class),
                        any(LocalDateTime.class)
                ))
                .thenReturn(timeRecords);
        when(mockTimeRecordRepository
                .findAllByUserIdAndDateTimeAfterOrderByDateTimeAsc(
                        anyString(),
                        any(LocalDateTime.class),
                        any(Pageable.class)
                ))
                .thenReturn(List.of(timeRecordAfter));

        TimeRecordService timeRecordService = new TimeRecordService(
                mockTimeRecordRepository, mockIdGenerator, mockTimeGenerator);
        List<WorkingTime> actual = timeRecordService.getWorkingTimeForMonth("defaultUser", YearMonth.of(2023, 12));

        assertNotNull(actual);
        assertEquals(3, actual.size());
        assertNull(actual.get(0).workStart());
        assertNull(actual.get(2).workEnd());
    }

    @Test
    void getTimeRecordLatestBefore() {
        TimeRecord timeRecordBefore =
                new TimeRecord("IDB", TimeRecordType.WORKSTART,
                        LocalDateTime.of(2023, 11, 30, 22, 30, 0),
                        "defaultUser", "Europe/Berlin", 120);
        when(mockTimeRecordRepository
                .findAllByUserIdAndDateTimeBeforeOrderByDateTimeDesc(
                        anyString(),
                        any(LocalDateTime.class),
                        any(Pageable.class)
                ))
                .thenReturn(List.of(timeRecordBefore));

        TimeRecordService timeRecordService = new TimeRecordService(
                mockTimeRecordRepository, mockIdGenerator, mockTimeGenerator);

        TimeRecord actual = timeRecordService.getTimeRecordLatestBefore(
                                "defaultUser",
                                LocalDateTime.of(
                                        2023,12,1,
                                        0,0, 0));

        assertNotNull(actual);
        assertEquals(timeRecordBefore, actual);
    }

    @Test
    void getTimeRecordFirstAfter() {
        TimeRecord timeRecordAfter =
                new TimeRecord("IDB", TimeRecordType.WORKEND,
                        LocalDateTime.of(2022, 2, 1, 2, 30, 0),
                        "defaultUser", "Europe/Berlin", 120);
        when(mockTimeRecordRepository
                .findAllByUserIdAndDateTimeAfterOrderByDateTimeAsc(
                        anyString(),
                        any(LocalDateTime.class),
                        any(Pageable.class)
                ))
                .thenReturn(List.of(timeRecordAfter));

        TimeRecordService timeRecordService = new TimeRecordService(
                mockTimeRecordRepository, mockIdGenerator, mockTimeGenerator);

        TimeRecord actual = timeRecordService.getTimeRecordFirstAfter(
                "defaultUser",
                LocalDateTime.of(
                        2024,1,1,
                        0,0, 0));

        assertNotNull(actual);
        assertEquals(timeRecordAfter, actual);
    }

    @Test
    void getTimeRecordsForInterval() {
        List<TimeRecord> timeRecords = List.of(
                new TimeRecord("ID0", TimeRecordType.WORKEND,
                        LocalDateTime.of(2023, 12, 1, 3, 30, 0),
                        "defaultUser", "Europe/Berlin", 120),
                new TimeRecord("ID1", TimeRecordType.WORKSTART,
                        LocalDateTime.of(2023, 12, 4, 8, 17, 32),
                        "defaultUser", "Europe/Berlin", 120),
                new TimeRecord("ID2", TimeRecordType.WORKEND,
                        LocalDateTime.of(2023, 12, 4, 13, 21, 14),
                        "defaultUser", "Europe/Berlin", 120),
                // WORKSTART "missing"
                new TimeRecord("ID3", TimeRecordType.WORKEND,
                        LocalDateTime.of(2023, 12, 5, 17, 2, 18),
                        "defaultUser", "Europe/Berlin", 120),
                new TimeRecord("ID4", TimeRecordType.WORKSTART,
                        LocalDateTime.of(2023, 12, 6, 9, 1, 7),
                        "defaultUser", "Europe/Berlin", 120),
                // WORKEND "missing"
                new TimeRecord("ID5", TimeRecordType.WORKSTART,
                        LocalDateTime.of(2023, 12, 31, 21, 0, 0),
                        "defaultUser", "Europe/Berlin", 120)
        );
        when(mockTimeRecordRepository
                .findAllByUserIdAndDateTimeBetween(
                        anyString(),
                        any(LocalDateTime.class),
                        any(LocalDateTime.class)
                ))
                .thenReturn(timeRecords);

        TimeRecordService timeRecordService = new TimeRecordService(
                mockTimeRecordRepository, mockIdGenerator, mockTimeGenerator);

        List<TimeRecord> actual = timeRecordService.getTimeRecordsForInterval(
                "defaultUser",
                LocalDateTime.of(
                        2023,12,1,
                        0,0, 0),
                LocalDateTime.of(
                        2024,1,1,
                        0,0, 0)
        );

        assertNotNull(actual);
        assertEquals(timeRecords, actual);
    }

    @Test
    void isWorktimeInterval() {
        TimeRecord startTimeRecord =
                new TimeRecord("ID1", TimeRecordType.WORKSTART,
                        LocalDateTime.of(2023, 12, 4, 8, 17, 32),
                        "defaultUser", "Europe/Berlin", 120);
        TimeRecord endTimeRecord =
                new TimeRecord("ID2", TimeRecordType.WORKEND,
                        LocalDateTime.of(2023, 12, 4, 13, 21, 14),
                        "defaultUser", "Europe/Berlin", 120);

        TimeRecordService timeRecordService = new TimeRecordService(
                mockTimeRecordRepository, mockIdGenerator, mockTimeGenerator);

        assertTrue(timeRecordService.isWorktimeInterval(startTimeRecord, endTimeRecord));
        assertFalse(timeRecordService.isWorktimeInterval(null, endTimeRecord));
        assertFalse(timeRecordService.isWorktimeInterval(startTimeRecord, null));
        assertFalse(timeRecordService.isWorktimeInterval(endTimeRecord, startTimeRecord));

    }
}