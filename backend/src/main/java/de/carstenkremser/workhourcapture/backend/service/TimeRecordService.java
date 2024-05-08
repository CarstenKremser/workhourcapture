package de.carstenkremser.workhourcapture.backend.service;

import de.carstenkremser.workhourcapture.backend.model.TimeRecord;
import de.carstenkremser.workhourcapture.backend.repository.TimeRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TimeRecordService {

    private final TimeRecordRepository timeRecordRepository;
    private final IdGenerator idGenerator;
    private final TimeGenerator timeGenerator;

    public TimeRecord addTimeRecordForNow(String userId) {
        TimeRecord timeRecord = new TimeRecord(
                idGenerator.createId(),
                timeGenerator.createInstantNow(),
                userId
        );
        return timeRecordRepository.insert(timeRecord);
    }
}
