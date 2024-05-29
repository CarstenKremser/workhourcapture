package de.carstenkremser.workhourcapture.backend.service;

import de.carstenkremser.workhourcapture.backend.model.WorkdayCalendarEntry;
import de.carstenkremser.workhourcapture.backend.repository.WorkdayCalendarEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkdayCalendarService {

    private final WorkdayCalendarEntryRepository repository;

    public List<WorkdayCalendarEntry> getWorkdayCalendarEntries(String userId, LocalDate dateFrom, LocalDate dateTo) {
        // TODO
        return Collections.emptyList();
    }

    public void addWorkdayCalendarEntries(List<WorkdayCalendarEntry> entries) {
        repository.saveAll(entries);
    }
}
