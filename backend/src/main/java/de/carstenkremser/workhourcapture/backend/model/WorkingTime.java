package de.carstenkremser.workhourcapture.backend.model;

import java.time.Duration;

public record WorkingTime (
    TimeRecord workStart,
    TimeRecord workEnd,
    Duration duration
    ){

    public WorkingTime(TimeRecord workStart, TimeRecord workEnd) {
        this(workStart, workEnd, calculateDuration(workStart, workEnd));
    }

    private static Duration calculateDuration(TimeRecord workStart, TimeRecord workEnd) {
        if (workStart == null || workEnd == null) {
            return null;
        }
        return Duration.between(workStart.dateTime(),workEnd.dateTime());
    };
}
