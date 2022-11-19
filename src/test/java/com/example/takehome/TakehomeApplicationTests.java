package com.example.takehome;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TakehomeApplicationTests {

    @Test
    void contextLoads() {
        String[] args = new String[0];
        TakehomeApplication.main(args);
        assertTrue(true);
    }
}
