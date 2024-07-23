package com.messenger.chatty.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {
    //Service
    @Autowired
    MemberService memberService;
    @Autowired
    DatabaseCleanup databaseCleanup;
    //Repository
    //Entity & Dto

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void cleanUp() {
        databaseCleanup.truncateAllEntity();
    }

}