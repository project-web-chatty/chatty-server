package com.messenger.chatty.service;

import com.messenger.chatty.dto.request.MemberJoinRequestDto;
import com.messenger.chatty.dto.request.WorkspaceGenerateRequestDto;
import com.messenger.chatty.dto.request.WorkspaceUpdateRequestDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceBriefDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceResponseDto;
import com.messenger.chatty.entity.Workspace;
import com.messenger.chatty.presentation.ErrorStatus;
import com.messenger.chatty.presentation.exception.custom.WorkspaceException;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    Long memberId2;
    MemberBriefDto memberResponse;
    MemberBriefDto memberResponse2;

    @BeforeEach
    void setUp() {
        MemberJoinRequestDto signUp = MemberJoinRequestDto.builder()
                .username("username")
                .password("password")
                .build();
        memberId = memberService.signup(signUp);
        memberResponse = memberService.getMemberProfileByMemberId(memberId);
        MemberJoinRequestDto signUp2 = MemberJoinRequestDto.builder()
                .username("username2")
                .password("password2")
                .build();
        memberId2 = memberService.signup(signUp2);
        memberResponse2 = memberService.getMemberProfileByMemberId(memberId2);
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

    @Test
    @Transactional
    @DisplayName("특정한 워크스페이스를 workspaceId를 통해 삭제합니다.(통과 x)")
    void deleteWorkspace() {
        //given
        WorkspaceGenerateRequestDto generateRequestDto = WorkspaceGenerateRequestDto.builder()
                .description("description")
                .name("name")
                .build();
        Long workspaceId = workspaceService.generateWorkspace(generateRequestDto, memberResponse.getUsername());
        //when
        workspaceService.deleteWorkspace(workspaceId);
        //then
        List<Workspace> all = workspaceRepository.findAll();
        assertThat(all.size()).isZero();
    }

    @Test
    @DisplayName("서비스 내 전체 워크스페이스를 가져옵니다.")
    void getAllWorkspace() {
        //given
        WorkspaceGenerateRequestDto generateRequestDto1 = WorkspaceGenerateRequestDto.builder()
                .description("description1")
                .name("name1")
                .build();
        WorkspaceGenerateRequestDto generateRequestDto2 = WorkspaceGenerateRequestDto.builder()
                .description("description2")
                .name("name2")
                .build();
        WorkspaceGenerateRequestDto generateRequestDto3 = WorkspaceGenerateRequestDto.builder()
                .description("description3")
                .name("name3")
                .build();
        workspaceService.generateWorkspace(generateRequestDto1, memberResponse.getUsername());
        workspaceService.generateWorkspace(generateRequestDto2, memberResponse.getUsername());
        workspaceService.generateWorkspace(generateRequestDto3, memberResponse.getUsername());
        //when
        List<WorkspaceBriefDto> allWorkspaceList = workspaceService.getAllWorkspaceList();
        //then
        assertThat(allWorkspaceList.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("특정 워크스페이스에 대한 정보를 가져옵니다. 이때 멤버 권한이 있어야합니다.")
    void getWorkspaceProfile() {
        //given
        WorkspaceGenerateRequestDto generateRequestDto = WorkspaceGenerateRequestDto.builder()
                .description("description")
                .name("name")
                .build();
        Long workspaceId = workspaceService.generateWorkspace(generateRequestDto, memberResponse.getUsername());
        //when
        WorkspaceResponseDto workspaceProfile = workspaceService.getWorkspaceProfile(workspaceId);
        //then
        assertThat(workspaceProfile).extracting("id", "name", "description")
                .containsExactly(
                        workspaceId,
                        generateRequestDto.getName(),
                        generateRequestDto.getDescription());
    }

    @Test
    @DisplayName("멤버 권한이 없는 사용자가 특정 워크스페이스에 대한 정보를 가져오려고 할 때 예외를 발생시킵니다.(통과x)")
    void executionExceptionWhenGetWorkspaceProfile() {
        //given
        WorkspaceGenerateRequestDto generateRequestDto = WorkspaceGenerateRequestDto.builder()
                .description("description")
                .name("name")
                .build();
        Long workspaceId = workspaceService.generateWorkspace(generateRequestDto, memberResponse.getUsername());
        //when & then
        assertThatThrownBy(() -> workspaceService.getWorkspaceProfile(workspaceId))
                .isInstanceOf(WorkspaceException.class)
                .hasMessage(ErrorStatus.WORKSPACE_UNAUTHORIZED.getMessage());
    }

    @Test
    @DisplayName("특정 워크스페이스에 대한 정보를 간단하게(참여중인 멤버와 채널 정보 제외) 가져옵니다.")
    //기획 방향에 맞추어 진행해야할 것 같습니다. 상세조회는 권한이 필수이겠지만 preview는 기획나름일것 같습니다.
    void getBriefWorkspace() {
        //given
        WorkspaceGenerateRequestDto generateRequestDto = WorkspaceGenerateRequestDto.builder()
                .description("description")
                .name("name")
                .build();
        Long workspaceId = workspaceService.generateWorkspace(generateRequestDto, memberResponse.getUsername());
        //when
        WorkspaceBriefDto workspaceBriefProfile = workspaceService.getWorkspaceBriefProfile(workspaceId);
        //then
        assertThat(workspaceBriefProfile).extracting("id", "name", "description")
                .containsExactly(
                        workspaceId,
                        generateRequestDto.getName(),
                        generateRequestDto.getDescription());

    }

    @Test
    @Transactional
    @DisplayName("워크스페이스의 초대코드를 생성합니다. 이때 생성 인증 권한이 필요합니다.")
    void setInvitationCode() {
        //given
        WorkspaceGenerateRequestDto generateRequestDto = WorkspaceGenerateRequestDto.builder()
                .description("description")
                .name("name")
                .build();
        Long workspaceId = workspaceService.generateWorkspace(generateRequestDto, memberResponse.getUsername());
        //when
        String code = workspaceService.setInvitationCode(workspaceId);
        //then
        Optional<Workspace> optionalWorkspace = workspaceRepository.findById(workspaceId);
        assertThat(optionalWorkspace).isPresent();
        assertThat(optionalWorkspace.get().getInvitationCode()).isEqualTo(code);
    }
    @Test
    @Transactional
    @DisplayName("사용자가 초대코드를 통해 워크스페이스에 참여합니다.")
    void joinWorkspace() {
        //given
        WorkspaceGenerateRequestDto generateRequestDto = WorkspaceGenerateRequestDto.builder()
                .description("description")
                .name("name")
                .build();
        Long workspaceId = workspaceService.generateWorkspace(generateRequestDto, memberResponse.getUsername());
        String code = workspaceService.setInvitationCode(workspaceId);
        //when
        workspaceService.enterToWorkspace(memberResponse2.getUsername(), code);
        //then
        Optional<Workspace> optionalWorkspace = workspaceRepository.findById(workspaceId);
        assertThat(optionalWorkspace.get().getWorkspaceJoins().size()).isEqualTo(2);
    }

    @Test
    @Transactional
    @DisplayName("워크스페이스에 참여중인 멤버들의 정보를 조회합니다. 이때 조회 권한이 필요합니다.")
    void getJoinMemberOfWorkspace() {
        //given
        WorkspaceGenerateRequestDto generateRequestDto = WorkspaceGenerateRequestDto.builder()
                .description("description")
                .name("name")
                .build();
        Long workspaceId = workspaceService.generateWorkspace(generateRequestDto, memberResponse.getUsername());
        String code = workspaceService.setInvitationCode(workspaceId);
        workspaceService.enterToWorkspace(memberResponse2.getUsername(), code);
        //when
        List<MemberBriefDto> membersOfWorkspace = workspaceService.getMembersOfWorkspace(workspaceId);
        //then
        assertThat(membersOfWorkspace.size()).isEqualTo(2);
        assertThat(membersOfWorkspace.get(1)).extracting("username", "id")
                .containsExactly(memberResponse2.getUsername(), memberResponse2.getId());
    }
}