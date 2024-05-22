package de.carstenkremser.workhourcapture.backend.model;

import java.time.Duration;
import java.time.LocalDate;

public record WorkingTime (
    TimeRecord workStart,
    TimeRecord workEnd,
    Duration duration
    ){

    public WorkingTime(TimeRecord workStart, TimeRecord workEnd) {
        this(workStart, workEnd, calculateDuration(workStart, workEnd));
    }

    private static Duration calculateDuration(TimeRecord workStart, TimeRecord workEnd) {
        if (workStart == null || workStart.dateTime() == null) {
            return Duration.ZERO;
        }
        if (workEnd == null || workEnd.dateTime() == null) {
            return Duration.ZERO;
        }
        return Duration.between(workStart.dateTime(),workEnd.dateTime());
    };

    public LocalDate date() {
        if (workStart != null && workStart.dateTime() != null) {
            return workStart.getDate();
        } else if (workEnd != null && workEnd.dateTime() != null) {
            return workEnd.getDate();
        }
        return null;
    }
}
