package de.carstenkremser.workhourcapture.backend.model;

import java.time.Duration;
import java.util.List;

public record WorkingDays(
        Duration allocated,
        List<WorkingDay> workingDays
) {
    public Duration worked() {
        Duration sum = Duration.ZERO;
        for (WorkingDay workingDay : workingDays) {
            sum = sum.plus(workingDay.getWorkingTime());
        }
        return sum;
    }

    public Duration difference() {
        return worked().minus(allocated);
    }
}
