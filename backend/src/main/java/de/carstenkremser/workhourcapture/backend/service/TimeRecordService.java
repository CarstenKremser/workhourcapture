package de.carstenkremser.workhourcapture.backend.service;

import de.carstenkremser.workhourcapture.backend.dto.TimeBookingDto;
import de.carstenkremser.workhourcapture.backend.model.TimeRecord;
import de.carstenkremser.workhourcapture.backend.model.TimeRecordType;
import de.carstenkremser.workhourcapture.backend.repository.TimeRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.*;
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

    List<TimeRecord> getTimeRecordsForInterval(String userId, LocalDateTime startTime, LocalDateTime endTime) {
        return timeRecordRepository.findAllByUserIdAndDateTimeBetween(userId, startTime, endTime);
    }

    public List<TimeRecord> getTimeRecordsForMonth(String userId, YearMonth monthAndYear) {
        LocalDateTime startTime = LocalDate.from(monthAndYear.atDay(1)).atStartOfDay();
        LocalDateTime endTime = monthAndYear.atEndOfMonth().plusDays(1).atStartOfDay();
        return getTimeRecordsForInterval(userId, startTime, endTime);
    }
}
