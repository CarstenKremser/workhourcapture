package de.carstenkremser.workhourcapture.backend.repository;

import de.carstenkremser.workhourcapture.backend.model.TimeRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeRecordRepository extends MongoRepository<TimeRecord, String> {

}
