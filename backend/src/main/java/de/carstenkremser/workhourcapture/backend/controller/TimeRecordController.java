package de.carstenkremser.workhourcapture.backend.controller;

import de.carstenkremser.workhourcapture.backend.dto.BookingNowDto;
import de.carstenkremser.workhourcapture.backend.model.TimeRecord;
import de.carstenkremser.workhourcapture.backend.service.TimeRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api/timerecord")
@RequiredArgsConstructor
public class TimeRecordController {

    private final TimeRecordService timeRecordService;

    @PostMapping("/addnow/{userId}")
    public TimeRecord addNow(@PathVariable String userId, @RequestBody BookingNowDto bookingBody) {
        System.out.println("addnow: " + bookingBody.userId() + ", " + bookingBody.recordType());
        return timeRecordService.addTimeRecordForNow(userId);
    }
}
