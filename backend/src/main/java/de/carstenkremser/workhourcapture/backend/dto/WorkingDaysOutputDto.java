package de.carstenkremser.workhourcapture.backend.dto;

import java.util.List;

public record WorkingDaysOutputDto(
        String allocated,
        String worked,
        String difference,
        List<WorkingDayOutputDto> workingDays
) {}
