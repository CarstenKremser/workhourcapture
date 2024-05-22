package de.carstenkremser.workhourcapture.backend.service;

import de.carstenkremser.workhourcapture.backend.dto.WorkingDayOutputDto;
import de.carstenkremser.workhourcapture.backend.dto.WorkingDaysOutputDto;
import de.carstenkremser.workhourcapture.backend.dto.WorkingTimeOutputDto;
import de.carstenkremser.workhourcapture.backend.model.*;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorkingTimeConverterTest {

    @Test
    void convertDurationToString_expectZeroTime_whenCalledWithNull() {
        WorkingTimeConverter converter = new WorkingTimeConverter();
        String actual = converter.convertDurationToString(null);
        assertEquals("0:00", actual);
    }

    @Test
    void convertDurationToString_expect2Hours33_whenCalledWithDurationOf2H33M() {
        WorkingTimeConverter converter = new WorkingTimeConverter();
        Duration duration = Duration.ofHours(2).plusMinutes(33);
        String actual = converter.convertDurationToString(duration);
        assertEquals("2:33", actual);
    }

    @Test
    void convertLocalTimeToString_expectEmptyString_whenCalledWithNull() {
        WorkingTimeConverter converter = new WorkingTimeConverter();
        String actual = converter.convertLocalTimeToString(null);
        assertEquals("", actual);
    }

    @Test
    void convertLocalTimeToString_expect0302_whenCalledWith3H2M() {
        WorkingTimeConverter converter = new WorkingTimeConverter();
        LocalTime localTime = LocalTime.of(3, 2);
        String actual = converter.convertLocalTimeToString(localTime);
        assertEquals("03:02", actual);
    }

    @Test
    void convertLocalDateToString_expectEmptyString_whenCalledWithNull() {
        WorkingTimeConverter converter = new WorkingTimeConverter();
        String actual = converter.convertLocalDateToString(null);
        assertEquals("", actual);
    }

    @Test
    void convertLocalDateToString_expect29022020_whenCalledWithY2020M02D29() {
        WorkingTimeConverter converter = new WorkingTimeConverter();
        LocalDate localDate = LocalDate.of(2020, 2, 29);
        String actual = converter.convertLocalDateToString(localDate);
        assertEquals("29.02.2020", actual);
    }

    @Test
    void convertWorkingDaysToDto_expectEmptyDto_whenCalledWithNull() {
        WorkingTimeConverter converter = new WorkingTimeConverter();
        WorkingDaysOutputDto actual = converter.convertWorkingDaysToDto(null);
        assertNotNull(actual);
        assertEquals("", actual.allocated());
        assertEquals("", actual.worked());
        assertEquals(List.of(), actual.workingDays());
    }


    @Test
    void convertWorkingDaysToDto_expectDto_whenCalledWithWorkingDays() {
        WorkingTimeConverter converter = new WorkingTimeConverter();

        TimeRecord timeRecordStart = new TimeRecord(
                "trId1",
                TimeRecordType.WORKSTART,
                LocalDateTime.of(2024,1,12,8,36,57),
                "userId",
                "Berlin/Europe",
                120);
        TimeRecord timeRecordEnd = new TimeRecord(
                "trId1",
                TimeRecordType.WORKEND,
                LocalDateTime.of(2024,1,12,11,46,57),
                "userId",
                "Berlin/Europe",
                120);
        WorkingTime workingTime = new WorkingTime(timeRecordStart, timeRecordEnd);
        WorkingDay workingDay = new WorkingDay(workingTime.date(), Duration.ZERO);
        workingDay.addWorkingTime(workingTime);
        WorkingDays workingDays = new WorkingDays(Duration.ZERO, List.of(workingDay));

        WorkingDaysOutputDto actual = converter.convertWorkingDaysToDto(workingDays);
        assertNotNull(actual);
        assertEquals("0:00", actual.allocated());
        assertEquals("3:10", actual.worked());
        assertEquals(1, actual.workingDays().size());
    }






    @Test
    void convertWorkingDayToDto_expectEmptyDto_whenCalledWithNull() {
        WorkingTimeConverter converter = new WorkingTimeConverter();
        WorkingDayOutputDto actual = converter.convertWorkingDayToDto(null);
        assertNotNull(actual);
        assertEquals("",actual.date());
        assertEquals("",actual.allocated());
        assertEquals(List.of(),actual.workingTimes());
    }

    @Test
    void convertWorkingDayToDto_expectDto_whenCalledWithWorkingDay() {
        WorkingTimeConverter converter = new WorkingTimeConverter();
        TimeRecord timeRecordStart = new TimeRecord(
                "trId1",
                TimeRecordType.WORKSTART,
                LocalDateTime.of(2024,1,12,8,36,57),
                "userId",
                "Berlin/Europe",
                120);
        TimeRecord timeRecordEnd = new TimeRecord(
                "trId1",
                TimeRecordType.WORKEND,
                LocalDateTime.of(2024,1,12,11,46,57),
                "userId",
                "Berlin/Europe",
                120);
        WorkingTime workingTime = new WorkingTime(timeRecordStart, timeRecordEnd);

        WorkingDay workingDay = new WorkingDay(workingTime.date(), Duration.ZERO);
        workingDay.addWorkingTime(workingTime);

        WorkingDayOutputDto actual = converter.convertWorkingDayToDto(workingDay);

        assertNotNull(actual);
        assertEquals("12.01.2024",actual.date());
        assertEquals("0:00",actual.allocated());
        assertEquals(1,actual.workingTimes().size());
    }

    @Test
    void convertWorkingTimeToDto_expectNull_whenCalledWithNull() {
        WorkingTimeConverter converter = new WorkingTimeConverter();
        WorkingTimeOutputDto actual = converter.convertWorkingTimeToDto(null);
        assertNull(actual);
    }

    @Test
    void convertWorkingTimeToDto_expectIncompleteDto_whenCalledWithStartWorkingTime() {
        WorkingTimeConverter converter = new WorkingTimeConverter();
        TimeRecord timeRecord = new TimeRecord(
                "trId1",
                TimeRecordType.WORKSTART,
                LocalDateTime.of(2024,1,12,8,36,57),
                "userId",
                "Berlin/Europe",
                120);
        WorkingTime workingTime = new WorkingTime(timeRecord,null);
        WorkingTimeOutputDto actual = converter.convertWorkingTimeToDto(workingTime);
        assertNotNull(actual);
        assertEquals("12.01.2024", actual.startDate());
        assertEquals("08:36", actual.startTime());
        assertEquals("", actual.endDate());
        assertEquals("", actual.endTime());
        assertEquals("0:00", actual.duration());
    }

    @Test
    void convertWorkingTimeToDto_expectIncompleteDto_whenCalledWithEndWorkingTime() {
        WorkingTimeConverter converter = new WorkingTimeConverter();
        TimeRecord timeRecord = new TimeRecord(
                "trId1",
                TimeRecordType.WORKEND,
                LocalDateTime.of(2024,1,12,8,36,57),
                "userId",
                "Berlin/Europe",
                120);
        WorkingTime workingTime = new WorkingTime(null,timeRecord);
        WorkingTimeOutputDto actual = converter.convertWorkingTimeToDto(workingTime);
        assertNotNull(actual);
        assertEquals("", actual.startDate());
        assertEquals("", actual.startTime());
        assertEquals("12.01.2024", actual.endDate());
        assertEquals("08:36", actual.endTime());
        assertEquals("0:00", actual.duration());
    }

    @Test
    void convertWorkingTimeToDto_expectCompleteDto_whenCalledWithStartAndEndWorkingTime() {
        WorkingTimeConverter converter = new WorkingTimeConverter();
        TimeRecord timeRecordStart = new TimeRecord(
                "trId1",
                TimeRecordType.WORKSTART,
                LocalDateTime.of(2024,1,12,8,36,57),
                "userId",
                "Berlin/Europe",
                120);
        TimeRecord timeRecordEnd = new TimeRecord(
                "trId1",
                TimeRecordType.WORKEND,
                LocalDateTime.of(2024,1,12,11,46,57),
                "userId",
                "Berlin/Europe",
                120);
        WorkingTime workingTime = new WorkingTime(timeRecordStart, timeRecordEnd);
        WorkingTimeOutputDto actual = converter.convertWorkingTimeToDto(workingTime);
        assertNotNull(actual);
        assertEquals("12.01.2024", actual.startDate());
        assertEquals("08:36", actual.startTime());
        assertEquals("12.01.2024", actual.endDate());
        assertEquals("11:46", actual.endTime());
        assertEquals("3:10", actual.duration());
    }

}