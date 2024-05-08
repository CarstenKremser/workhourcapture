package de.carstenkremser.workhourcapture.backend.service;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TimeGenerator {

    Instant createInstantNow() {
        return Instant.now();
    }
}
