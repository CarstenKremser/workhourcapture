package de.carstenkremser.workhourcapture.backend.service;

import de.carstenkremser.workhourcapture.backend.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WorkingTimeService {

    final TimeRecordService timeRecordService;
    final WorkdayCalendarService calendarService;

    public List<WorkingTime> getWorkingTimeForMonth(String userId, YearMonth monthAndYear) {
        LocalDateTime startTime = LocalDate.from(monthAndYear.atDay(1)).atStartOfDay();
        LocalDateTime endTime = monthAndYear.atEndOfMonth().plusDays(1).atStartOfDay();
        return getWorkingTimeForInterval(userId, startTime, endTime);
    }

    public List<WorkingTime> getWorkingTimeForInterval(String userId, LocalDateTime startTime, LocalDateTime endTime) {
        TimeRecord lastBefore = timeRecordService.getTimeRecordLatestBefore(
                userId,
                startTime
        );
        TimeRecord firstAfter = timeRecordService.getTimeRecordFirstAfter(
                userId,
                endTime.minusNanos(1)
        );
        List<TimeRecord> timeRecords = new ArrayList<>(timeRecordService.getTimeRecordsForInterval(userId, startTime, endTime));
        if (!timeRecords.isEmpty() && isWorktimeInterval(lastBefore, timeRecords.getFirst())) {
            timeRecords.removeFirst();
        }
        if (!timeRecords.isEmpty() && isWorktimeInterval(timeRecords.getLast(), firstAfter)) {
            timeRecords.addLast(firstAfter);
        }
        List<WorkingTime> workingTimes = new ArrayList<>();
        TimeRecord previous = null;
        for (TimeRecord timeRecord : timeRecords) {
            if (timeRecord.recordType().equals(TimeRecordType.WORKEND)) {
                workingTimes.add(new WorkingTime(previous, timeRecord));
                previous = null;
            } else if (previous != null) {
                workingTimes.add(new WorkingTime(previous, null));
                previous = timeRecord;
            } else {
                previous = timeRecord;
            }
        }
        if (previous != null) {
            workingTimes.add(new WorkingTime(previous, null));
        }
        return workingTimes;
    }

    boolean isWorktimeInterval(TimeRecord startRecord, TimeRecord endRecord) {
        if (startRecord == null || endRecord == null) {
            return false;
        }
        return startRecord.recordType().equals(TimeRecordType.WORKSTART)
                && endRecord.recordType().equals(TimeRecordType.WORKEND);
    }

    public WorkingDays getWorkingDaysForMonth(String userId, YearMonth monthAndYear) {
        LocalDate startDate = LocalDate.from(monthAndYear.atDay(1));
        LocalDate endDate = monthAndYear.atEndOfMonth();
        return getWorkingDaysForInterval(userId, startDate, endDate);
    }

    public WorkingDays getWorkingDaysForInterval(String userId, LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, WorkingDay> workingDays = calendarService.getRegularWorkingDayMap(userId, startDate, endDate);
        LocalDate currentDate;

        List<WorkingTime> workingTimes = getWorkingTimeForInterval(userId, startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));
        for (WorkingTime workingTime : workingTimes) {
            currentDate = workingTime.date();
            if (currentDate != null && !workingDays.containsKey(currentDate)) {
                workingDays.put(currentDate, new WorkingDay(currentDate, Duration.ZERO));
            }
            workingDays.get(currentDate).addWorkingTime(workingTime);
        }

        currentDate = startDate;
        Duration allocatedSum = Duration.ZERO;
        List<WorkingDay> workingDayList = new ArrayList<>();

        while (currentDate.isBefore(endDate.plusDays(1))) {
            if (workingDays.containsKey(currentDate)) {
                WorkingDay workingDay = workingDays.get(currentDate);
                allocatedSum = allocatedSum.plus(workingDay.getAllocated());
                workingDayList.add(workingDay);
            }
            currentDate = currentDate.plusDays(1);
        }

        return new WorkingDays(
                allocatedSum,
                workingDayList);
    }

    Duration getAllocatedDurationForDate(String userId, LocalDate date) {
        if (date == null) {
            return Duration.ZERO;
        }
        // erstmal hartcodiert - soll später aus Datenbank kommen
        if (date.getDayOfWeek().equals(DayOfWeek.TUESDAY)) {
            return Duration.ofHours(8);
        } else if (date.getDayOfWeek().equals(DayOfWeek.THURSDAY)) {
            return Duration.ofHours(8);
        } else if (date.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
            return Duration.ofHours(4);
        }
        return Duration.ZERO;
    }
}
