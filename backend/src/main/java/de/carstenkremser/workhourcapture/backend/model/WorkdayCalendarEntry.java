package de.carstenkremser.workhourcapture.backend.model;

import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;

@Document("Workdays")
public record WorkdayCalendarEntry(
        @With
        @Id String id,
        int evaluationPriority,
        String userId,
        WorkdayCalendarEntryType entryType,
        Duration scheduledHours,
        LocalDate dateFrom,
        LocalDate dateTo,
        DayOfWeek dayOfWeek,
        String annotation
) {
}
