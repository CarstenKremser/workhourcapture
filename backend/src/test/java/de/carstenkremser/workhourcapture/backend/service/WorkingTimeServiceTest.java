package de.carstenkremser.workhourcapture.backend.service;

import de.carstenkremser.workhourcapture.backend.model.TimeRecord;
import de.carstenkremser.workhourcapture.backend.model.TimeRecordType;
import de.carstenkremser.workhourcapture.backend.model.WorkingDays;
import de.carstenkremser.workhourcapture.backend.model.WorkingTime;
import de.carstenkremser.workhourcapture.backend.repository.TimeRecordRepository;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WorkingTimeServiceTest {

    private final IdGenerator mockIdGenerator = mock(IdGenerator.class);
    private final TimeGenerator mockTimeGenerator = mock(TimeGenerator.class);
    private final TimeRecordRepository mockTimeRecordRepository = mock(TimeRecordRepository.class);
    private final TimeRecordService mockTimeRecordService = mock(TimeRecordService.class);
    private final WorkdayCalendarService mockWorkdayCalendarService = mock(WorkdayCalendarService.class);

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

        TimeRecordService trs = new TimeRecordService(
                mockTimeRecordRepository, mockIdGenerator, mockTimeGenerator);

        WorkingTimeService workingTimeService = new WorkingTimeService(
                trs, mockWorkdayCalendarService);
        List<WorkingTime> actual = workingTimeService.getWorkingTimeForMonth("defaultUser", YearMonth.of(2023, 12));

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

        TimeRecordService trs = new TimeRecordService(
                mockTimeRecordRepository, mockIdGenerator, mockTimeGenerator);

        WorkingTimeService workingTimeService = new WorkingTimeService(
                trs, mockWorkdayCalendarService);

        List<WorkingTime> actual = workingTimeService.getWorkingTimeForMonth("defaultUser", YearMonth.of(2023, 12));

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

        TimeRecordService trs = new TimeRecordService(
                mockTimeRecordRepository, mockIdGenerator, mockTimeGenerator);

        WorkingTimeService workingTimeService = new WorkingTimeService(
                trs, mockWorkdayCalendarService);

        List<WorkingTime> actual = workingTimeService.getWorkingTimeForMonth("defaultUser", YearMonth.of(2023, 12));

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

        TimeRecordService trs = new TimeRecordService(
                mockTimeRecordRepository, mockIdGenerator, mockTimeGenerator);

        WorkingTimeService workingTimeService = new WorkingTimeService(
                trs, mockWorkdayCalendarService);

        List<WorkingTime> actual = workingTimeService.getWorkingTimeForMonth("defaultUser", YearMonth.of(2023, 12));

        assertNotNull(actual);
        assertEquals(3, actual.size());
        assertNull(actual.get(0).workStart());
        assertNull(actual.get(2).workEnd());
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

        WorkingTimeService workingTimeService = new WorkingTimeService(
                null, null);

        assertTrue(workingTimeService.isWorktimeInterval(startTimeRecord, endTimeRecord));
        assertFalse(workingTimeService.isWorktimeInterval(null, endTimeRecord));
        assertFalse(workingTimeService.isWorktimeInterval(startTimeRecord, null));
        assertFalse(workingTimeService.isWorktimeInterval(endTimeRecord, startTimeRecord));
    }

    @Test
    void getWorkingTimeForMonth() {

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

        List<TimeRecord> all = new ArrayList<>(timeRecords);
        all.addFirst(timeRecordBefore);
        all.addLast(timeRecordAfter);

        when(mockTimeRecordService.getTimeRecordsForInterval(
                        anyString(),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
                ))
                .thenReturn(all);

        WorkingTimeService workingTimeService = new WorkingTimeService(
                mockTimeRecordService, mockWorkdayCalendarService);

        WorkingDays workingDays = workingTimeService.getWorkingDaysForMonth("defaultUser", YearMonth.of(2023, 12));
        assertNotNull(workingDays);
        assertEquals(14,workingDays.workingDays().size());
    }

    @Test
    void getWorkingTimeForInterval() {
    }

    @Test
    void testIsWorktimeInterval() {
    }

    @Test
    void getWorkingDaysForMonth() {
    }

    @Test
    void getWorkingDaysForInterval() {
    }

    @Test
    void getAllocatedDurationForDate() {
        WorkingTimeService workingTimeService = new WorkingTimeService(
                null, null);
        Duration tuesday = workingTimeService
                .getAllocatedDurationForDate("defaultUser",
                        LocalDate.of(2024,5,7));
        Duration wednesday = workingTimeService
                .getAllocatedDurationForDate("defaultUser",
                        LocalDate.of(2024,5,8));
        Duration thursday = workingTimeService
                .getAllocatedDurationForDate("defaultUser",
                        LocalDate.of(2024,5,9));
        Duration friday = workingTimeService
                .getAllocatedDurationForDate("defaultUser",
                        LocalDate.of(2024,5,10));
        assertEquals(Duration.ofHours(8), tuesday);
        assertEquals(Duration.ZERO, wednesday);
        assertEquals(Duration.ofHours(8), thursday);
        assertEquals(Duration.ofHours(4), friday);
    }

    @Test
    void getAllocatedDurationForDate_returnsDurationZero_whenCalledWithoutDate() {
        WorkingTimeService workingTimeService = new WorkingTimeService(
                null, null);
        Duration noDate = workingTimeService
                .getAllocatedDurationForDate("defaultUser", null);

        assertEquals(Duration.ZERO, noDate);
    }


}