package com.messenger.chatty.service;

import com.messenger.chatty.dto.request.MemberJoinRequestDto;
import com.messenger.chatty.dto.response.member.MyProfileDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    @DisplayName("유저네임과 패스워드를 통해 회원가입을 합니다.")
    void registerMember() {
        //given
        MemberJoinRequestDto signUp = MemberJoinRequestDto.builder()
                .username("username")
                .password("password")
                .build();
        //when
        MyProfileDto profileDto = memberService.signup(signUp);
        //then
        assertThat(profileDto.getUsername()).isEqualTo(signUp.getUsername());
        assertThat(profileDto.getEmail()).isNull();

    }

}