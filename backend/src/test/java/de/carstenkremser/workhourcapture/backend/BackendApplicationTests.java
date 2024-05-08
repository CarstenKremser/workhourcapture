package de.carstenkremser.workhourcapture.backend;

import de.carstenkremser.workhourcapture.backend.controller.*;
import de.carstenkremser.workhourcapture.backend.repository.TimeRecordRepository;
import de.carstenkremser.workhourcapture.backend.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    IdGenerator idGenerator = new IdGenerator();
    @Autowired
    TimeGenerator timeGenerator = new TimeGenerator();

    @Autowired
    TimeRecordController timeRecordController;
    @Autowired
    TimeRecordService timeRecordService;
    @Autowired
    TimeRecordRepository timeRecordRepository;

    @Test
    void contextLoads() {
        assertNotNull(idGenerator);
        assertNotNull(timeGenerator);
        assertNotNull(timeRecordController);
        assertNotNull(timeRecordService);
        assertNotNull(timeRecordRepository);
    }

}
