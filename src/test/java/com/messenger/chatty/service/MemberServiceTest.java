package com.messenger.chatty.service;

import com.messenger.chatty.dto.request.MemberJoinRequestDto;
import com.messenger.chatty.dto.request.MemberUpdateRequestDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
import com.messenger.chatty.dto.response.member.MyProfileDto;
import com.messenger.chatty.entity.Member;
import com.messenger.chatty.presentation.ErrorStatus;
import com.messenger.chatty.presentation.exception.custom.MemberException;
import com.messenger.chatty.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Test
    @Transactional
    @DisplayName("memberId를 통해 member 데이터를 삭제합니다.")
    void removeMember() {
        //given
        MemberJoinRequestDto signUp = MemberJoinRequestDto.builder()
                .username("username")
                .password("password")
                .build();
        Long memberId = memberService.signup(signUp);
        //when
        memberService.deleteMeById(memberId);
        //then
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isZero();
    }
    @Test
    @Transactional
    @DisplayName("username을 통해 member 데이터를 삭제합니다.")
    void removeMemberByUsername() {
        //given
        MemberJoinRequestDto signUp = MemberJoinRequestDto.builder()
                .username("username")
                .password("password")
                .build();
        Long memberId = memberService.signup(signUp);
        //when
        memberService.deleteMeByUsername(signUp.getUsername());
        //then
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isZero();
    }

    @Test
    @DisplayName("데이터베이스에 저장된 모든 멤버 정보를 불러옵니다. 이때 리턴값은 MemberBriefDto(리스트)입니다.")
    void getAllMember() {
        //given
        MemberJoinRequestDto signUp1 = MemberJoinRequestDto.builder()
                .username("username1")
                .password("password1")
                .build();
        Long memberId1 = memberService.signup(signUp1);
        MemberJoinRequestDto signUp2 = MemberJoinRequestDto.builder()
                .username("username2")
                .password("password2")
                .build();
        Long memberId2 = memberService.signup(signUp2);
        MemberJoinRequestDto signUp3 = MemberJoinRequestDto.builder()
                .username("username3")
                .password("password3")
                .build();
        Long memberId3 = memberService.signup(signUp3);
        //when
        List<MemberBriefDto> allMemberList = memberService.getAllMemberList();
        //then
        assertThat(allMemberList).hasSize(3);
    }

    @Test
    @DisplayName("특정 멤버 정보를 memberId를 통해 불러옵니다.이때 리턴 값은 memberBriefDto입니다.")
    void getMemberById() {
        //given
        MemberJoinRequestDto signUp1 = MemberJoinRequestDto.builder()
                .username("username1")
                .password("password1")
                .build();
        Long memberId1 = memberService.signup(signUp1);
        //when
        MemberBriefDto response = memberService.getMemberProfileByMemberId(memberId1);
        //then
        assertThat(response).extracting("id", "username")
                .containsExactly(memberId1, signUp1.getUsername());
    }

    @Test
    @DisplayName("특정 멤버 정보를 username을 통해 불러옵니다. 이때 리턴 값은 memberBriefDto입니다.")
    void getMemberByUsername() {
        //given
        MemberJoinRequestDto signUp1 = MemberJoinRequestDto.builder()
                .username("username1")
                .password("password1")
                .build();
        Long memberId1 = memberService.signup(signUp1);
        //when
        MyProfileDto response = memberService.getMyProfileByUsername(signUp1.getUsername());
        //then
        assertThat(response).extracting("id", "username")
                .containsExactly(memberId1, signUp1.getUsername());
    }

    @Test
    @DisplayName("회원가입 시 작성된 유저네임이 중복인지 확인합니다")
    void checkDuplicatedUsername() {
        //given
        MemberJoinRequestDto signUp1 = MemberJoinRequestDto.builder()
                .username("username1")
                .password("password1")
                .build();
        Long memberId1 = memberService.signup(signUp1);

        //when & then
        assertThatThrownBy(() -> memberService.checkDuplicatedUsername(signUp1.getUsername()))
                .isInstanceOf(MemberException.class)
                .hasMessage(ErrorStatus.MEMBER_USERNAME_ALREADY_EXISTS.getMessage());
    }

}