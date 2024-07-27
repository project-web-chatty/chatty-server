package com.messenger.chatty.service;

import com.messenger.chatty.dto.request.MemberJoinRequestDto;
import com.messenger.chatty.dto.request.WorkspaceGenerateRequestDto;
import com.messenger.chatty.dto.request.WorkspaceUpdateRequestDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceBriefDto;
import com.messenger.chatty.entity.Workspace;
import com.messenger.chatty.repository.MemberRepository;
import com.messenger.chatty.repository.WorkspaceRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.Database;
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
class WorkspaceServiceTest {
    //Service
    @Autowired
    WorkspaceService workspaceService;
    @Autowired
    MemberService memberService;
    @Autowired
    DatabaseCleanup databaseCleanup;
    //Repository
    @Autowired
    WorkspaceRepository workspaceRepository;
    @Autowired
    MemberRepository memberRepository;
    //Entity & Dto & Else
    Long memberId;
    MemberBriefDto memberResponse;

    @BeforeEach
    void setUp() {
        MemberJoinRequestDto signUp = MemberJoinRequestDto.builder()
                .username("username")
                .password("password")
                .build();
        memberId = memberService.signup(signUp);
        memberResponse = memberService.getMemberProfileByMemberId(memberId);
    }

    @AfterEach
    void cleanUp() {
        databaseCleanup.truncateAllEntity();
    }

    @Test
    @Transactional
    @DisplayName("유저가 워크스페이스를 생성합니다. 이때 기본 채널 2개가 생성되며 생성자가 해당 워크스페이스의 멤버로 등록됩니다.")
    void createWorkspace() {
        //given
        WorkspaceGenerateRequestDto request = WorkspaceGenerateRequestDto.builder()
                .description("description")
                .name("name")
                .build();
        //when
        Long workspaceId = workspaceService.generateWorkspace(request, memberResponse.getUsername());
        //then
        Optional<Workspace> optionalWorkspace = workspaceRepository.findById(workspaceId);
        assertThat(optionalWorkspace).isPresent()
                .get()
                .extracting("name", "description")
                .containsExactly(request.getName(), request.getDescription());
        assertThat(optionalWorkspace.get().getChannels().size()).isEqualTo(2);
        assertThat(optionalWorkspace.get().getWorkspaceJoins().size()).isOne();
    }

    @Test
    @DisplayName("생성자가 워크스페이스를 수정합니다.")
    void updateWorkspace() {
        //given
        WorkspaceGenerateRequestDto generateRequestDto = WorkspaceGenerateRequestDto.builder()
                .description("description")
                .name("name")
                .build();
        Long workspaceId = workspaceService.generateWorkspace(generateRequestDto, memberResponse.getUsername());
        WorkspaceUpdateRequestDto request = WorkspaceUpdateRequestDto.builder()
                .description("updatedDescription")
                .build();
        //when
        Long updatedWorkspaceId = workspaceService.updateWorkspaceProfile(workspaceId, request);
        //then
        Optional<Workspace> optionalWorkspace = workspaceRepository.findById(updatedWorkspaceId);
        assertThat(optionalWorkspace).isPresent()
                .get()
                .extracting("description", "name")
                .containsExactly(request.getDescription(), generateRequestDto.getName());

    }
}