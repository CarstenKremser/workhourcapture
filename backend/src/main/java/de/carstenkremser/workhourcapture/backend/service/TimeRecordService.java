package de.carstenkremser.workhourcapture.backend.service;

import de.carstenkremser.workhourcapture.backend.dto.TimeBookingDto;
import de.carstenkremser.workhourcapture.backend.model.TimeRecord;
import de.carstenkremser.workhourcapture.backend.model.TimeRecordType;
import de.carstenkremser.workhourcapture.backend.repository.TimeRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

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

}
