package de.carstenkremser.workhourcapture.backend.service;

import de.carstenkremser.workhourcapture.backend.dto.TimeBookingDto;
import de.carstenkremser.workhourcapture.backend.model.TimeRecord;
import de.carstenkremser.workhourcapture.backend.model.TimeRecordType;
import de.carstenkremser.workhourcapture.backend.model.WorkingTime;
import de.carstenkremser.workhourcapture.backend.repository.TimeRecordRepository;
import lombok.RequiredArgsConstructor;
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
                TimeRecordType.valueOf(timeBookingDto.recordType()),
                InstantToLocalDateTimeWithDefaultNow(timeBookingDto.recordTimestamp()),
                timeBookingDto.userId(),
                timeBookingDto.timezoneName(),
                timeBookingDto.timezoneOffset() * -1
        );
        return timeRecordRepository.insert(timeRecord);
    }

    public List<TimeRecord> getTimeRecordsForMonth(String userId, YearMonth monthAndYear) {
        LocalDateTime startTime = LocalDate.from(monthAndYear.atDay(1)).minusDays(1).atStartOfDay();
        LocalDateTime endTime = monthAndYear.atEndOfMonth().plusDays(2).atStartOfDay();
        return timeRecordRepository.findAllByUserIdAndDateTimeBetween(userId, startTime, endTime);
    }

    public List<WorkingTime> getWorkingTimeForMonth(String userId, YearMonth monthAndYear) {
        List<TimeRecord> timeRecords = getTimeRecordsForMonth(userId, monthAndYear);
        List<WorkingTime> workingTimes = new ArrayList<>();
        TimeRecord timeRecordStart = null;
        for (TimeRecord timeRecord : timeRecords) {
            if (timeRecordStart == null) {
                switch (timeRecord.recordType()) {
                    case TimeRecordType.WORKSTART -> timeRecordStart = timeRecord;
                    case TimeRecordType.WORKEND -> {
                        WorkingTime workingTime = new WorkingTime(null, timeRecord);
                        workingTimes.add(workingTime);
                    }
                }
            } else {
                switch (timeRecord.recordType()) {
                    case TimeRecordType.WORKSTART -> {
                        WorkingTime workingTime = new WorkingTime(timeRecordStart, null);
                        workingTimes.add(workingTime);
                        timeRecordStart = timeRecord;
                    }
                    case TimeRecordType.WORKEND -> {
                        WorkingTime workingTime = new WorkingTime(timeRecordStart, timeRecord);
                        workingTimes.add(workingTime);
                        timeRecordStart = null;
                    }
                }
            }
        }
        return workingTimes;
    }
}
