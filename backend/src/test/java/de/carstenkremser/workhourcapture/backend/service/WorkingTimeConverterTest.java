package de.carstenkremser.workhourcapture.backend.service;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

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

/*    @Test
    void convertWorkingDaysToDto() {
    }

    @Test
    void convertWorkingDayToDto() {
    }

    @Test
    void convertWorkingTimeToDto() {
    }
*/
}