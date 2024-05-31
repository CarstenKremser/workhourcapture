package de.carstenkremser.workhourcapture.backend.service;

import de.carstenkremser.workhourcapture.backend.model.WorkdayCalendarEntry;
import de.carstenkremser.workhourcapture.backend.repository.WorkdayCalendarEntryRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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

/*    @Test
    void testGetWorkdayCalendarEntries() {
        //WorkdayCalendarService service = new WorkdayCalendarService(mockRepository);

    }*/
}