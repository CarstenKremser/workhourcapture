package de.carstenkremser.workhourcapture.backend.controller;

import de.carstenkremser.workhourcapture.backend.dto.ReportMonthDto;
import de.carstenkremser.workhourcapture.backend.model.WorkingTime;
import de.carstenkremser.workhourcapture.backend.service.TimeRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/workingtime")
@RequiredArgsConstructor
public class WorkingTimeController {

    private final TimeRecordService timeRecordService;

    @GetMapping("/formonth")
    public List<WorkingTime> getFormonth(@RequestBody ReportMonthDto reportMonthBody) {
        YearMonth yearMonth = YearMonth.of(reportMonthBody.year(), reportMonthBody.month());
        return timeRecordService.getWorkingTimeForMonth(reportMonthBody.userId(), yearMonth);
    }
}
