package de.carstenkremser.workhourcapture.backend.service;

import de.carstenkremser.workhourcapture.backend.dto.TimeBookingDto;
import de.carstenkremser.workhourcapture.backend.model.TimeRecord;
import de.carstenkremser.workhourcapture.backend.model.TimeRecordType;
import de.carstenkremser.workhourcapture.backend.model.WorkingTime;
import de.carstenkremser.workhourcapture.backend.repository.TimeRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeRecordService {

    private final TimeRecordRepository timeRecordRepository;
    private final IdGenerator idGenerator;
    private final TimeGenerator timeGenerator;

    private LocalDateTime InstantToLocalDateTimeWithDefaultNow(Instant timestamp) {
        if (timestamp == null) {
            return timeGenerator.createLocalDateTimeNow();
        }
        return LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault());
    }

    public TimeRecord addTimeRecord(TimeBookingDto timeBookingDto) {
        TimeRecord timeRecord = new TimeRecord(
                idGenerator.createId(),
                TimeRecordType.valueOf(timeBookingDto.recordType().toUpperCase()),
                InstantToLocalDateTimeWithDefaultNow(timeBookingDto.recordTimestamp()),
                timeBookingDto.userId(),
                timeBookingDto.timezoneName(),
                timeBookingDto.timezoneOffset() * -1
        );
        return timeRecordRepository.insert(timeRecord);
    }

    TimeRecord getTimeRecordLatestBefore(String UserId, LocalDateTime localDateTime) {
        List<TimeRecord> queryResult = timeRecordRepository
                .findAllByUserIdAndDateTimeBeforeOrderByDateTimeDesc(
                        UserId, localDateTime, PageRequest.of(0,1));
        if (queryResult.isEmpty()) {
            return null;
        }
        return queryResult.getFirst();
    }

    TimeRecord getTimeRecordFirstAfter(String UserId, LocalDateTime localDateTime) {
        List<TimeRecord> queryResult = timeRecordRepository
                .findAllByUserIdAndDateTimeAfterOrderByDateTimeAsc(
                        UserId, localDateTime, PageRequest.of(0,1));
        if (queryResult.isEmpty()) {
            return null;
        }
        return queryResult.getFirst();
    }

    public List<TimeRecord> getTimeRecordsForInterval(String userId, LocalDateTime startTime, LocalDateTime endTime) {
        return timeRecordRepository.findAllByUserIdAndDateTimeBetween(userId, startTime, endTime);
    }

    public List<TimeRecord> getTimeRecordsForMonth(String userId, YearMonth monthAndYear) {
        LocalDateTime startTime = LocalDate.from(monthAndYear.atDay(1)).atStartOfDay();
        LocalDateTime endTime = monthAndYear.atEndOfMonth().plusDays(1).atStartOfDay();
        return getTimeRecordsForInterval(userId, startTime, endTime);
    }


    public List<WorkingTime> getWorkingTimeForMonth(String userId, YearMonth monthAndYear) {
        TimeRecord lastBefore = getTimeRecordLatestBefore(
                userId,
                LocalDateTime.from(
                        monthAndYear.atDay(1).atStartOfDay()
                ));
        TimeRecord firstAfter = getTimeRecordFirstAfter(
                userId,
                monthAndYear.atEndOfMonth().plusDays(1).atTime(0,0).minusNanos(1)
        );
        List<TimeRecord> timeRecords = new ArrayList<>(getTimeRecordsForMonth(userId, monthAndYear));
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
            }
            else if (previous != null) {
                workingTimes.add(new WorkingTime(previous, null));
                previous = timeRecord;
            }
            else {
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

}
