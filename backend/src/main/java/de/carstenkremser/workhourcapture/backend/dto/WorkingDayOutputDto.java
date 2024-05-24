package de.carstenkremser.workhourcapture.backend.dto;

import java.util.List;

public record WorkingDayOutputDto(
        String date,
        String allocated,
        String worked,
        String difference,
        List<WorkingTimeOutputDto>workingTimes
) {
}
