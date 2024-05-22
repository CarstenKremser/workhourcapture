package de.carstenkremser.workhourcapture.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Document("TimeRecords")
public record TimeRecord(
        @Id String id,
        TimeRecordType recordType,
        LocalDateTime dateTime,
        String userId,
        String timeZone,
        Integer timeZoneOffset
) {

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }
}
