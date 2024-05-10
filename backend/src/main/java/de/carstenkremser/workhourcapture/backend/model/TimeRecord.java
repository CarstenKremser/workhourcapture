package de.carstenkremser.workhourcapture.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("TimeRecords")
public record TimeRecord(
        @Id String id,
        String recordType,
        Instant time,
        String userId,
        String timeZone,
        Integer timeZoneOffset
        ) {
}
