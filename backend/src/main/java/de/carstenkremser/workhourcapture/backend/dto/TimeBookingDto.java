package de.carstenkremser.workhourcapture.backend.dto;


import java.time.Instant;

public record TimeBookingDto(
        String userId,
        String recordType ,
        Instant recordTime,
        Integer timezoneOffset,
        String timezoneName
) {
}
