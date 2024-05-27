package de.carstenkremser.workhourcapture.backend.controller;

import de.carstenkremser.workhourcapture.backend.dto.ReportMonthDto;
import de.carstenkremser.workhourcapture.backend.dto.WorkingDaysOutputDto;
import de.carstenkremser.workhourcapture.backend.service.WorkingTimeConverter;
import de.carstenkremser.workhourcapture.backend.service.WorkingTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/workingtime")
@RequiredArgsConstructor
public class WorkingTimeController {

    private final WorkingTimeService workingTimeService;
    private final WorkingTimeConverter workingTimeConverter;

    @PostMapping("/formonth")
    public WorkingDaysOutputDto getForMonth(@RequestBody ReportMonthDto reportMonthBody) {
        YearMonth yearMonth = YearMonth.of(reportMonthBody.year(), reportMonthBody.month());
        return workingTimeConverter.convertWorkingDaysToDto(
                workingTimeService.getWorkingDaysForMonth(reportMonthBody.userId(), yearMonth)
        );
    }
}
