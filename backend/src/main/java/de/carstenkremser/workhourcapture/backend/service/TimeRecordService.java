package de.carstenkremser.workhourcapture.backend.service;

import de.carstenkremser.workhourcapture.backend.dto.TimeBookingDto;
import de.carstenkremser.workhourcapture.backend.model.TimeRecord;
import de.carstenkremser.workhourcapture.backend.repository.TimeRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TimeRecordService {

    private final TimeRecordRepository timeRecordRepository;
    private final IdGenerator idGenerator;
    private final TimeGenerator timeGenerator;

    public TimeRecord addTimeRecord(TimeBookingDto timeBookingDto) {
        Instant recordTime = (timeBookingDto.recordTime() == null)
                ? timeGenerator.createInstantNow()
                : timeBookingDto.recordTime();
        TimeRecord timeRecord = new TimeRecord(
                idGenerator.createId(),
                timeBookingDto.recordType(),
                recordTime,
                timeBookingDto.userId(),
                timeBookingDto.timezoneName(),
                timeBookingDto.timezoneOffset() * -1
        );
        return timeRecordRepository.insert(timeRecord);
    }

}
