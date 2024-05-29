package de.carstenkremser.workhourcapture.backend.controller;

import de.carstenkremser.workhourcapture.backend.dto.WorkdayCalendarEntryInputDto;
import de.carstenkremser.workhourcapture.backend.service.AppDateAndTimeConverter;
import de.carstenkremser.workhourcapture.backend.service.IdGenerator;
import de.carstenkremser.workhourcapture.backend.service.WorkdayCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class WorkdayCalendarController {

    private final WorkdayCalendarService workdayCalendarService;
    private final AppDateAndTimeConverter converter;
    private final IdGenerator idGenerator;

    @PostMapping("/addworkdays")
    public void addWorkdays(@RequestBody List<WorkdayCalendarEntryInputDto> workdayCalendarEntries) {
        workdayCalendarService.addWorkdayCalendarEntries(
                workdayCalendarEntries
                        .stream()
                        .map(entry -> converter.convertToWorkdayCalendarEntry(entry))
                        .map(entry -> entry.withId(idGenerator.createId()))
                        .toList()
        );
    }
}
