package de.carstenkremser.workhourcapture.backend.repository;

import de.carstenkremser.workhourcapture.backend.model.TimeRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeRecordRepository extends MongoRepository<TimeRecord, String> {

    List<TimeRecord> findAllByUserIdAndDateTimeBetween(String userId, LocalDateTime start, LocalDateTime end);

    List<TimeRecord> findAllByUserIdAndDateTimeBeforeOrderByDateTimeDesc(String userId, LocalDateTime date, Pageable pageable);

    List<TimeRecord> findAllByUserIdAndDateTimeAfterOrderByDateTimeAsc(String userId, LocalDateTime date, Pageable pageable);
}
