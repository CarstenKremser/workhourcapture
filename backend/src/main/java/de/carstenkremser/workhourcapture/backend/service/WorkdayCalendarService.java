package de.carstenkremser.workhourcapture.backend.service;

import de.carstenkremser.workhourcapture.backend.model.WorkdayCalendarEntry;
import de.carstenkremser.workhourcapture.backend.repository.WorkdayCalendarEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkdayCalendarService {

    private final WorkdayCalendarEntryRepository repository;

    public List<WorkdayCalendarEntry> getWorkdayCalendarEntries(String userId, LocalDate dateFrom, LocalDate dateTo) {
        List<WorkdayCalendarEntry> calendarEntries = new ArrayList<>();

        calendarEntries.addAll(repository
                .findAllByUserIdAndDateFromIsNullAndDateToIsNull(userId));

        if (dateTo != null) {
            calendarEntries.addAll(repository
                    .findAllByUserIdAndDateFromBeforeAndDateToIsNull(
                            userId, dateTo.plusDays(1)));
        }

        if (dateFrom != null) {
            calendarEntries.addAll(repository
                    .findAllByUserIdAndDateFromIsNullAndDateToAfter(
                            userId, dateFrom.minusDays(1)));
        }

        if (dateFrom != null && dateTo != null) {
            calendarEntries.addAll(repository
                    .findAllByUserIdAndDateFromBeforeAndDateToAfter(
                            userId, dateTo.plusDays(1), dateFrom.minusDays(1)));
        }
        System.out.println(calendarEntries.size() + " calendarEntries found");
        System.out.println("CalendarEntries: " + calendarEntries);

        return Collections.emptyList();
    }

    public void addWorkdayCalendarEntries(List<WorkdayCalendarEntry> entries) {
        repository.saveAll(entries);
    }
}
