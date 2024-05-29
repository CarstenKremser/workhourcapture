package de.carstenkremser.workhourcapture.backend.controller;

import de.carstenkremser.workhourcapture.backend.dto.ReportMonthDto;
import de.carstenkremser.workhourcapture.backend.dto.WorkingDaysOutputDto;
import de.carstenkremser.workhourcapture.backend.model.WorkingDays;
import de.carstenkremser.workhourcapture.backend.service.WorkingTimeConverter;
import de.carstenkremser.workhourcapture.backend.service.WorkingTimeService;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.YearMonth;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WorkingTimeControllerTest {


    private final WorkingTimeService mockWorkingTimeService = mock(WorkingTimeService.class);
    private final WorkingTimeConverter workingTimeConverter = new WorkingTimeConverter();

    @Test
    void getForMonth() {
        WorkingTimeController controller = new WorkingTimeController(
                mockWorkingTimeService, workingTimeConverter);
        WorkingDays workingDays = new WorkingDays (
                Duration.ZERO,
                Collections.emptyList()
        );
        when(mockWorkingTimeService
                .getWorkingDaysForMonth("userId", YearMonth.of(2024,5))
        ).thenReturn(workingDays);

        WorkingDaysOutputDto expected = workingTimeConverter.convertWorkingDaysToDto(workingDays);

        ReportMonthDto reportMonthBody = new ReportMonthDto (
                "userId", 2024, 5);

        WorkingDaysOutputDto actual = controller.getForMonth(reportMonthBody);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}