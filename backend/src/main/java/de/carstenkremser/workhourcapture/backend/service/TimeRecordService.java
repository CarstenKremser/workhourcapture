package de.carstenkremser.workhourcapture.backend.service;

import de.carstenkremser.workhourcapture.backend.dto.TimeBookingDto;
import de.carstenkremser.workhourcapture.backend.model.TimeRecord;
import de.carstenkremser.workhourcapture.backend.repository.TimeRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class TimeRecordService {

    private final TimeRecordRepository timeRecordRepository;
    private final IdGenerator idGenerator;
    private final TimeGenerator timeGenerator;

    public TimeRecord addTimeRecord(TimeBookingDto timeBookingDto) {
        LocalDateTime recordDateTime = (timeBookingDto.recordTimestamp() == null)
                ? timeGenerator.createLocalDateTimeNow()
                : LocalDateTime.ofInstant(timeBookingDto.recordTimestamp(), ZoneId.systemDefault());
        TimeRecord timeRecord = new TimeRecord(
                idGenerator.createId(),
                timeBookingDto.recordType(),
                //recordTimestamp,
                recordDateTime,
                timeBookingDto.userId(),
                timeBookingDto.timezoneName(),
                timeBookingDto.timezoneOffset() * -1
        );
        return timeRecordRepository.insert(timeRecord);
    }

}
