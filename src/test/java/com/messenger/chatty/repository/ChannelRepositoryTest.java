package com.messenger.chatty.repository;


import com.messenger.chatty.config.DataCleaner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


@DataJpaTest
@ActiveProfiles("test")
public class ChannelRepositoryTest {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private DataCleaner cleaner;

    @BeforeEach
    public void setUp(){

    }
    @AfterEach
    public void tearDown(){
    }

    @Test
    public void test(){

    }
}
