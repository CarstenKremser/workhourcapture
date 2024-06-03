package de.carstenkremser.workhourcapture.backend.service;

import de.carstenkremser.workhourcapture.backend.model.WorkdayCalendarEntry;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class WorkdayExpander {
    private final LocalDate dateStart;
    private final LocalDate dateEnd;

    List<WorkdayCalendarEntry> expand(WorkdayCalendarEntry entry) {
        List<WorkdayCalendarEntry> calendarEntries = new ArrayList<>();

        LocalDate expandStart = entry.dateFrom() == null
                || dateStart.isAfter(entry.dateFrom())
                ? dateStart
                : entry.dateFrom();
        LocalDate expandEnd = entry.dateTo() == null
                || dateEnd.isBefore(entry.dateTo())
                ? dateEnd
                : entry.dateTo();
        for (LocalDate currentDate = expandStart;
             currentDate.isBefore(expandEnd.plusDays(1));
             currentDate = currentDate.plusDays(1)) {
            if (entry.dayOfWeek() == null
                    || entry.dayOfWeek().getValue() == currentDate.getDayOfWeek().getValue()) {
                calendarEntries.add(new WorkdayCalendarEntry(
                        currentDate.toString() + "-" + entry.id(),
                        entry.evaluationPriority(),
                        entry.userId(),
                        entry.entryType(),
                        entry.scheduledHours(),
                        currentDate,
                        currentDate,
                        null,
                        entry.annotation()
                ));
            }
        }
        return calendarEntries;
   }
}