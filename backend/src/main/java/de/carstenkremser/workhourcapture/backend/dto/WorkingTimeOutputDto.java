package de.carstenkremser.workhourcapture.backend.dto;

public record WorkingTimeOutputDto(
        String startDate,
        String startTime,
        String endDate,
        String endTime,
        String duration

) {
}
