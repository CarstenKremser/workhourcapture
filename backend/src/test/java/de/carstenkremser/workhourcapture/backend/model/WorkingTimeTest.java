package de.carstenkremser.workhourcapture.backend.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class WorkingTimeTest {

    @Test
    void date_expectNull_whenCalledWithoutTimes() {
        TimeRecord timeRecordStart = new TimeRecord(
                "idStart",
                TimeRecordType.WORKSTART,
                null,
                "userId",
                "Berlin/Europe",
                120
        );
        TimeRecord timeRecordEnd = new TimeRecord(
                "idStart",
                TimeRecordType.WORKEND,
                null,
                "userId",
                "Berlin/Europe",
                120
        );
        WorkingTime workingTime = new WorkingTime(timeRecordStart, timeRecordEnd);

        LocalDate actual = workingTime.date();
        assertNull(actual);
    }

    @Test
    void date_expectWorkEndTime_whenCalledWithoutTimeIWorkStartTimeRecord() {
        TimeRecord timeRecordStart = new TimeRecord(
                "idStart",
                TimeRecordType.WORKSTART,
                null,
                "userId",
                "Berlin/Europe",
                120
        );
        TimeRecord timeRecordEnd = new TimeRecord(
                "idStart",
                TimeRecordType.WORKEND,
                LocalDateTime.of(2023,12,1,12,0),
                "userId",
                "Berlin/Europe",
                120
        );
        WorkingTime workingTime = new WorkingTime(timeRecordStart, timeRecordEnd);

        LocalDate actual = workingTime.date();
        assertNotNull(actual);
        assertEquals(timeRecordEnd.getDate(), actual);
    }

}