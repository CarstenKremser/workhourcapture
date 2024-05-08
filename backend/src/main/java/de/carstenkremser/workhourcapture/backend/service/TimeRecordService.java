package de.carstenkremser.workhourcapture.backend.service;

import de.carstenkremser.workhourcapture.backend.model.TimeRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TimeRecordService {

    private final IdGenerator idGenerator;
    private final TimeGenerator timeGenerator;

    public TimeRecord addTimeRecordForNow(String userId) {
        TimeRecord timeRecord = new TimeRecord(
                idGenerator.createId(),
                timeGenerator.createInstantNow(),
                userId
        );
        return timeRecord;
    }
}
