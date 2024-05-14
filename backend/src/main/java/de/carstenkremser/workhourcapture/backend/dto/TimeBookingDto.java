package de.carstenkremser.workhourcapture.backend.dto;


import java.time.Instant;

public record TimeBookingDto(
        String userId,
        String recordType ,
        Instant recordTimestamp,
        Integer timezoneOffset,
        String timezoneName
) {
}
