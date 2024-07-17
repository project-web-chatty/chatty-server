package com.messenger.chatty.repository;


import com.messenger.chatty.config.DataCleaner;
import com.messenger.chatty.entity.Channel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class ChannelRepositoryTest {
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private DataCleaner cleaner;

    @BeforeEach
    public void setUp(){
        Channel channel = Channel.builder()
                        .name("일반").build();
        channelRepository.save(channel);
    }
    @AfterEach
    public void clear(){
        cleaner.clear();
    }



    @Test
    @DisplayName("이름 없이 저장시 exception")
    public void testNullableException(){
        assertThrows(DataIntegrityViolationException.class,()->{
            channelRepository.save(Channel.builder().build());
        });
    }
}
