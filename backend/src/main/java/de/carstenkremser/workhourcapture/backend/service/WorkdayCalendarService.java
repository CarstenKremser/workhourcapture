package de.carstenkremser.workhourcapture.backend.service;

import de.carstenkremser.workhourcapture.backend.model.WorkdayCalendarEntry;
import de.carstenkremser.workhourcapture.backend.model.WorkingDay;
import de.carstenkremser.workhourcapture.backend.repository.WorkdayCalendarEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

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

        return calendarEntries;
    }

    public Map<LocalDate, WorkingDay> getRegularWorkingDayMap(String userId, LocalDate dateFrom, LocalDate dateTo) {
        Map<LocalDate, WorkingDay> result = new HashMap<>();
        for (WorkingDay workingDay : getRegularWorkingDayList(userId, dateFrom, dateTo)) {
            result.put(workingDay.getDate(), workingDay);
        }
        return result;
    }

    public List<WorkingDay> getRegularWorkingDayList(String userId, LocalDate dateFrom, LocalDate dateTo) {
        WorkdayExpander expander = new WorkdayExpander(dateFrom, dateTo);
        List<WorkdayCalendarEntry> calendarEntries =
                getWorkdayCalendarEntries(userId, dateFrom, dateTo)
                        .stream()
                        .flatMap((entry) -> expander.expand(entry).stream())
                        .sorted((WorkdayCalendarEntry entryL, WorkdayCalendarEntry entryR) -> {
                            int result = 0;
                            result = entryL.dateFrom().compareTo(entryR.dateFrom());
                            if (result == 0) {
                                result = Integer.compare(
                                        entryL.evaluationPriority(),
                                        entryR.evaluationPriority());
                            }
                            return result;
                        })
                        .toList();

        List<WorkingDay> workingDays = new ArrayList<>();
        WorkingDay previousWorkingDay = null;
        for (WorkdayCalendarEntry entry : calendarEntries) {
            if (previousWorkingDay != null) {
                if (previousWorkingDay.getDate().isBefore(entry.dateFrom())) {
                    workingDays.add(previousWorkingDay);
                }
            }
            previousWorkingDay = new WorkingDay(
                    entry.dateFrom(),
                    entry.scheduledHours()
            );

        }
        if (previousWorkingDay != null) {
            workingDays.add(previousWorkingDay);
        }
        for (WorkingDay workingDay : workingDays) {
            System.out.println(workingDay);
        }
        return workingDays;
    }

    public void addWorkdayCalendarEntries(List<WorkdayCalendarEntry> entries) {
        repository.saveAll(entries);
    }
}
