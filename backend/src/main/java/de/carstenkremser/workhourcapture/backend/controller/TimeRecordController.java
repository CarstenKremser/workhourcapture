package de.carstenkremser.workhourcapture.backend.controller;

import de.carstenkremser.workhourcapture.backend.dto.TimeBookingDto;
import de.carstenkremser.workhourcapture.backend.model.TimeRecord;
import de.carstenkremser.workhourcapture.backend.service.TimeRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api/timerecord")
@RequiredArgsConstructor
public class TimeRecordController {

    private final TimeRecordService timeRecordService;

    @PostMapping("/add")
    public TimeRecord add(@RequestBody TimeBookingDto bookingBody) {
        return timeRecordService.addTimeRecord(bookingBody);
    }
/*
    @GetMapping("/formonth")
    public List<TimeRecord> getFormonth(@RequestBody ReportMonthDto reportMonthBody) {
        YearMonth yearMonth = YearMonth.of(reportMonthBody.year(), reportMonthBody.month());
        return timeRecordService.getTimeRecordsForMonth(reportMonthBody.userId(), yearMonth);
    }
*/
}
