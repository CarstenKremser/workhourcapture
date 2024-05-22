package de.carstenkremser.workhourcapture.backend.model;

import java.time.Duration;
import java.util.List;

public record WorkingDays(
        Duration allocated,
        Duration worked,
        List<WorkingDay> workingDays

) {
}
