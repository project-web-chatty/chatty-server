package com.messenger.chatty.service;

import com.messenger.chatty.dto.request.MemberJoinRequestDto;
import com.messenger.chatty.dto.request.MemberUpdateRequestDto;
import com.messenger.chatty.dto.response.member.MyProfileDto;
import com.messenger.chatty.entity.Member;
import com.messenger.chatty.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    @Autowired
    MemberRepository memberRepository;
    //Entity & Dto

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void cleanUp() {
        databaseCleanup.truncateAllEntity();
    }

    @Test
    @Transactional
    @DisplayName("유저네임과 패스워드를 통해 회원가입을 합니다.")
    void registerMember() {
        //given
        MemberJoinRequestDto signUp = MemberJoinRequestDto.builder()
                .username("username")
                .password("password")
                .build();
        //when
        Long memberId = memberService.signup(signUp);
        //then
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        assertThat(optionalMember).isPresent()
                .get().extracting("username", "password")
                .containsExactly(signUp.getUsername(), signUp.getPassword());
    }

    @Test
    @Transactional
    @DisplayName("유저의 이름, 닉네임, 소개에 대한 정보를 수정합니다")
    void updateProfile() {
        //given
        MemberJoinRequestDto signUp = MemberJoinRequestDto.builder()
                .username("username")
                .password("password")
                .build();
        memberService.signup(signUp);
        MemberUpdateRequestDto profile = MemberUpdateRequestDto.builder()
                .name("name")
                .nickname("nickname")
                .introduction("introduction")
                .build();
        //when
        Long memberId = memberService.updateMyProfile(signUp.getUsername(), profile);
        //then
        Optional<Member> byId = memberRepository.findById(memberId);
        assertThat(byId).isPresent()
                .get()
                .extracting("name", "nickname", "introduction")
                .containsExactly(profile.getName(), profile.getNickname(), profile.getIntroduction());
    }

}