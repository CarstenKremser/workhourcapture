package de.carstenkremser.workhourcapture.backend.dto;

public record ReportMonthDto(
        String userId,
        int year,
        int month
) {
}
