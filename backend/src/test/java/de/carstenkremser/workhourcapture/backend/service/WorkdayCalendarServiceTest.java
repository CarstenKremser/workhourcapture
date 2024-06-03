package de.carstenkremser.workhourcapture.backend.service;

import de.carstenkremser.workhourcapture.backend.model.WorkdayCalendarEntry;
import de.carstenkremser.workhourcapture.backend.model.WorkdayCalendarEntryType;
import de.carstenkremser.workhourcapture.backend.model.WorkingDay;
import de.carstenkremser.workhourcapture.backend.repository.WorkdayCalendarEntryRepository;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class WorkdayCalendarServiceTest {

    private final WorkdayCalendarEntryRepository mockRepository = mock(WorkdayCalendarEntryRepository.class);

    @Test
    void getWorkdayCalendarEntries() {
        WorkdayCalendarService service = new WorkdayCalendarService(mockRepository);
        LocalDate dateStart = LocalDate.of(2024,5,1);
        LocalDate dateEnd = LocalDate.of(2024,5,31);
        List<WorkdayCalendarEntry> actual = service.getWorkdayCalendarEntries(
                "userId", dateStart, dateEnd);
        assertNotNull(actual);
    }

    @Test
    void addWorkdayCalendarEntries() {
        WorkdayCalendarService service = new WorkdayCalendarService(mockRepository);
        List<WorkdayCalendarEntry> entries = List.of();
        service.addWorkdayCalendarEntries(entries);
        verify(mockRepository).saveAll(anyList());
    }

    @Test
    void getRegularWorkingDayMap() {
        WorkdayCalendarService service = new WorkdayCalendarService(mockRepository);
        List<WorkdayCalendarEntry> workEntries = new ArrayList<>();
        workEntries.add(new WorkdayCalendarEntry(
                "Id1", 10, "userId", WorkdayCalendarEntryType.WORKDAY, Duration.ofHours(8),
                null, null, null, "workday"));
        List<WorkdayCalendarEntry> freeEntries = new ArrayList<>();
        freeEntries.add(new WorkdayCalendarEntry(
                "Id1", 10, "userId", WorkdayCalendarEntryType.FREEDAY, Duration.ofHours(0),
                LocalDate.of(2023,12,5), LocalDate.of(2023,12,8), DayOfWeek.WEDNESDAY, "workday"));
        when(mockRepository.findAllByUserIdAndDateFromIsNullAndDateToIsNull("userId"))
                .thenReturn(workEntries);
        when(mockRepository.findAllByUserIdAndDateFromIsNullAndDateToAfter(anyString(), any(LocalDate.class)))
                .thenReturn(new ArrayList<>());
        when(mockRepository.findAllByUserIdAndDateFromBeforeAndDateToIsNull(anyString(), any(LocalDate.class)))
                .thenReturn(new ArrayList<>());
        when(mockRepository.findAllByUserIdAndDateFromBeforeAndDateToAfter(
                anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(freeEntries);

        Map<LocalDate, WorkingDay> actual = service.getRegularWorkingDayMap("userId",
                LocalDate.of(2023, 12, 4), LocalDate.of(2023, 12, 8));

        assertNotNull(actual);
        assertEquals(5, actual.size());
    }

/*    @Test
    void testGetWorkdayCalendarEntries() {
        //WorkdayCalendarService service = new WorkdayCalendarService(mockRepository);

    }*/
}