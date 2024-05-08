package de.carstenkremser.workhourcapture.backend.repository;

import de.carstenkremser.workhourcapture.backend.model.TimeRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimeRecordRepository extends MongoRepository<TimeRecord, String> {

    List<TimeRecord> getTimeRecordsByUserId(String userId);
}
