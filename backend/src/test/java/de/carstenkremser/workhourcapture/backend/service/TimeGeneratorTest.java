package de.carstenkremser.workhourcapture.backend.service;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class TimeGeneratorTest {

    @Test
    void createInstantNow() {
        TimeGenerator timeGenerator = new TimeGenerator();
        Instant createdInstantNow1 = timeGenerator.createInstantNow();
        Instant createdInstantNow2 = timeGenerator.createInstantNow();

        assertNotNull(createdInstantNow1);
        assertNotNull(createdInstantNow2);
        assertNotEquals(createdInstantNow1, createdInstantNow2);
    }
}