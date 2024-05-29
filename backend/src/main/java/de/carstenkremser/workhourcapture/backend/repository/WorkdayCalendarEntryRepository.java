package de.carstenkremser.workhourcapture.backend.repository;

import de.carstenkremser.workhourcapture.backend.model.WorkdayCalendarEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WorkdayCalendarEntryRepository extends MongoRepository<WorkdayCalendarEntry, String> {

    @Override
    <S extends WorkdayCalendarEntry> List<S> saveAll(Iterable<S> entities);
}
