package de.carstenkremser.workhourcapture.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("TimeRecords")
public record TimeRecord(
        @Id String id,
        TimeRecordType recordType,
        LocalDateTime dateTime,
        String userId,
        String timeZone,
        Integer timeZoneOffset
) {

    int getMonth() {
        return dateTime.getMonth().getValue();
    }
}
