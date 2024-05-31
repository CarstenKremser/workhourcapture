package de.carstenkremser.workhourcapture.backend.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdGenerator {
    public String createId() {
        return UUID.randomUUID().toString();
    }
}
