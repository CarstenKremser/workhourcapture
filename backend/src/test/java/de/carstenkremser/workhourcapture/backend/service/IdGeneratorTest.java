package de.carstenkremser.workhourcapture.backend.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdGeneratorTest {

    @Test
    void createId() {
        IdGenerator idGenerator = new IdGenerator();
        String createdId1 = idGenerator.createId();
        String createdId2 = idGenerator.createId();

        assertNotNull(createdId1);
        assertNotNull(createdId2);
        assertNotEquals(createdId1, createdId2);
    }
}