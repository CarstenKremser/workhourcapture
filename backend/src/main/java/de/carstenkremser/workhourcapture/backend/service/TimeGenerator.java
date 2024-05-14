package de.carstenkremser.workhourcapture.backend.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TimeGenerator {

    LocalDateTime createLocalDateTimeNow() {
        return LocalDateTime.now();
    }
}
