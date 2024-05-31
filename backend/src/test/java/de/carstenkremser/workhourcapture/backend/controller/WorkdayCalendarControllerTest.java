package de.carstenkremser.workhourcapture.backend.controller;

import de.carstenkremser.workhourcapture.backend.dto.WorkdayCalendarEntryInputDto;
import de.carstenkremser.workhourcapture.backend.service.AppDateAndTimeConverter;
import de.carstenkremser.workhourcapture.backend.service.IdGenerator;
import de.carstenkremser.workhourcapture.backend.service.WorkdayCalendarService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class WorkdayCalendarControllerTest {

    private final WorkdayCalendarService mockWorkdayCalendarService= mock(WorkdayCalendarService.class);
    private final AppDateAndTimeConverter mockConverter = mock(AppDateAndTimeConverter.class);
    private final IdGenerator mockIdGenerator = mock(IdGenerator.class);

    @Test
    void addWorkdays() {
        WorkdayCalendarController controller = new WorkdayCalendarController(
                mockWorkdayCalendarService, mockConverter, mockIdGenerator);
        List<WorkdayCalendarEntryInputDto> dtoList = List.of();

        controller.addWorkdays(dtoList);

        verify(mockWorkdayCalendarService).addWorkdayCalendarEntries(anyList());
    }
}