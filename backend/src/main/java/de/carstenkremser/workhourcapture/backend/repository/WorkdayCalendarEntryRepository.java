package de.carstenkremser.workhourcapture.backend.repository;

import de.carstenkremser.workhourcapture.backend.model.WorkdayCalendarEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface WorkdayCalendarEntryRepository extends MongoRepository<WorkdayCalendarEntry, String> {

    @Override
    <S extends WorkdayCalendarEntry> List<S> saveAll(Iterable<S> entities);


    List<WorkdayCalendarEntry> findAllByUserIdAndDateFromIsNullAndDateToIsNull(String userId);

    List<WorkdayCalendarEntry> findAllByUserIdAndDateFromBeforeAndDateToIsNull(String userId, LocalDate dateFrom);

    List<WorkdayCalendarEntry> findAllByUserIdAndDateFromIsNullAndDateToAfter(String userId, LocalDate dateTo);

    List<WorkdayCalendarEntry> findAllByUserIdAndDateFromBeforeAndDateToAfter(String userId, LocalDate dateFrom, LocalDate dateTo);
}
