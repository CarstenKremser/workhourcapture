package de.carstenkremser.workhourcapture.backend.dto;

public record WorkdayCalendarEntryInputDto(
        int evaluationPriority,
        String userId,
        String entryType,
        String scheduledHours,
        String dateFrom,
        String dateTo,
        String dayOfWeek,
        String annotation
) {
}
