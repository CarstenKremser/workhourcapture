package de.carstenkremser.workhourcapture.backend.model;

import lombok.Getter;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ToString
public class WorkingDay {
    @Getter
    private final LocalDate date;
    @Getter
    private final Duration allocated;
    private final List<WorkingTime> workingTimes;

    public WorkingDay(LocalDate date, Duration allocated) {
        this.date = date;
        this.allocated = allocated;
        this.workingTimes = new ArrayList<>();
    }

    public List<WorkingTime> getWorkingTimes() {
        return List.copyOf(workingTimes);
    }

    public void addWorkingTime(WorkingTime workingTime) {
        workingTimes.add(workingTime);
    }

    public Duration getWorkingTime() {
        Duration sum = Duration.ZERO;
        for (WorkingTime workingTime : workingTimes) {
            sum = sum.plus(workingTime.duration());
        }
        return sum;
    }

    public Duration getDifference() {
        return getWorkingTime().minus(allocated);
    }
}
