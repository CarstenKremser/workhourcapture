package de.carstenkremser.workhourcapture.backend.service;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeGeneratorTest {

    @Test
    void createLocalDateTimeNow() {
        TimeGenerator timeGenerator = new TimeGenerator();
        LocalDateTime createdLocalDateTimeNow1 = timeGenerator.createLocalDateTimeNow();
        LocalDateTime createdLocalDateTimeNow2 = timeGenerator.createLocalDateTimeNow();

        assertNotNull(createdLocalDateTimeNow1);
        assertNotNull(createdLocalDateTimeNow2);
        assertNotEquals(createdLocalDateTimeNow1, createdLocalDateTimeNow2);
    }
}