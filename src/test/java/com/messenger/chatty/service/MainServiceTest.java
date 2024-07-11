//package com.messenger.chatty.service;
//
//import com.messenger.chatty.config.DataCleaner;
//import com.messenger.chatty.dto.request.MemberJoinRequestDto;
//import com.messenger.chatty.dto.request.WorkspaceGenerateRequestDto;
//import com.messenger.chatty.dto.response.channel.ChannelBriefDto;
//import com.messenger.chatty.dto.response.member.MemberBriefDto;
//import com.messenger.chatty.dto.response.member.MyProfileDto;
//import com.messenger.chatty.dto.response.workspace.WorkspaceBriefDto;
//import com.messenger.chatty.dto.response.workspace.WorkspaceResponseDto;
//import com.messenger.chatty.entity.ChannelJoin;
//import com.messenger.chatty.entity.Member;
//import com.messenger.chatty.repository.MemberRepository;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//@SpringBootTest
//@Transactional
//public class MainServiceTest {
//
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//
//    @Autowired
//    private MemberRepository memberRepository;
//    @Autowired
//        private MemberService memberService;
//        @Autowired
//        private WorkspaceService workspaceService;
//        @Autowired
//        private ChannelService channelService;
//
//
//        @Autowired
//        private DataCleaner cleaner;
//
//        @BeforeEach
//        public void setUp(){
//            // 워크스페이스 생성
//
//            MemberJoinRequestDto suhyeon =MemberJoinRequestDto.builder()
//                    .username("suhyeon0000")
//                    .password("0000")
//                    .name("suhyeon")
//                    .nickname("닉네임")
//                    .build();
//
//            MemberJoinRequestDto insung =MemberJoinRequestDto.builder()
//                    .username("insung0000")
//                    .password("0000")
//                    .name("insung")
//                    .nickname("인성 이갑")
//                    .build();
//
//            MemberJoinRequestDto jieun =MemberJoinRequestDto.builder()
//                    .username("jieun0000")
//                    .password("0000")
//                    .name("jieun")
//                    .nickname("지은이")
//                    .build();
//
//            memberService.signup(suhyeon);
//            memberService.signup(insung);
//            memberService.signup(jieun);
//
//
//            WorkspaceGenerateRequestDto toss = WorkspaceGenerateRequestDto.builder()
//                    .name("토스팀").description("토스 개발팀입니다.").build();
//
//
//            // suhyeon0000 tossTeam 을 만듬
//            WorkspaceBriefDto tossWorkspaceProfile = workspaceService.generateWorkspace(toss, "suhyeon0000");
//
//            Long tossTeamId = tossWorkspaceProfile.getId();
//
//            // 기존 채널 2개 이외에 2개의 채널을 추가 생성
//         //   channelService.createChannelToWorkspace("토스팀","홍보팀");
//         //   channelService.createChannelToWorkspace("토스팀","개발팀");
//
//
//            // jieun0000 이 워크스페이스에 가입
//         //   workspaceService.enterIntoWorkspace("토스팀","jieun0000");
//
//            /////////////////////
//        //    workspaceService.deleteWorkspace("토스팀");
//
//        }
//        @AfterEach
//        public void clear(){
//            cleaner.clear();
//        }
//
//
//        @Test
//        @DisplayName("한 워크스페이스 내의 멤버 수 검증")
//        public void testNumberOfMember(){
//            Assertions.assertThat(workspaceService.getMembersOfWorkspace("토스팀").size()).isEqualTo(1);
//
//
//        }
//        @Test
//        @DisplayName("한 워크스페이스 내의 채널 수 검증")
//        public void testNumberOfChannels(){
//            Assertions.assertThat(workspaceService.getChannelsOfWorkspace("토스팀").size()).isEqualTo(4);
//        }
//
//        @Test
//        @DisplayName("워크스페이스 프로파일 업데이트")
//        public void testUpdateProfile()
//        {
//            memberService.updateMyProfile
//                    ("suhyeon0000","이름바꿈",null,"자기소개바꿈");
//
//            MyProfileDto suhyeon0000 = memberService.getMyProfileByUsername("suhyeon0000");
//            Assertions.assertThat(suhyeon0000.getName()).isEqualTo("이름바꿈");
//            Assertions.assertThat(suhyeon0000.getNickname()).isEqualTo("닉네임");
//        }
//
//        @Test
//        @DisplayName("동일 워크 스페이스에 한 멤버가 두번 참여 신청을 보내면 예외 ")
//        public void testDuplicatedRequest(){
//            org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class ,()-> {
//                workspaceService.enterIntoWorkspace("토스팀","suhyeon0000");
//            });
//
//        }
//
//
//
//        @Test
//        @DisplayName("멤버 이름과 워크스페이스 이름을 이용하여 멤버 정보 불러오기")
//        public void testFindChannelsByIWorkspaceAndMemberId(){
//            List<WorkspaceBriefDto> workspaces = memberService.getMyWorkspaces("suhyeon0000");
//            List<ChannelBriefDto> myChannelsInTossTeam = memberService.getMyChannels("suhyeon0000", "토스팀");
//
//            Assertions.assertThat(workspaces.size()).isEqualTo(1);
//            Assertions.assertThat(myChannelsInTossTeam.size()).isEqualTo(4);
//        }
//
//        @Test
//        @DisplayName("멤버 제거 시 데이터 업데이트")
//        public void testDeleteMember(){
//        }
//
//        @Test
//        @DisplayName("멤버 제거하기")
//        public void testDeleteWorkspace(){
//            memberService.deleteMeByUsername("suhyeon0000");
//
//            entityManager.flush();
//            entityManager.clear();
//
//            Assertions.assertThat(memberService.getAllMemberList()).hasSize(2);
//
//        }
//
//
//
//}
