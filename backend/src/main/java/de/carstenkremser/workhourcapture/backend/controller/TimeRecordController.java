package de.carstenkremser.workhourcapture.backend.controller;

import de.carstenkremser.workhourcapture.backend.model.TimeRecord;
import de.carstenkremser.workhourcapture.backend.service.TimeRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/api/timerecord")
@RequiredArgsConstructor
public class TimeRecordController {

    private final TimeRecordService timeRecordService;

    @PostMapping("/addnow/{userId}")
    public TimeRecord addNow(@PathVariable String userId) {
        return timeRecordService.addTimeRecordForNow(userId);
    }
}
