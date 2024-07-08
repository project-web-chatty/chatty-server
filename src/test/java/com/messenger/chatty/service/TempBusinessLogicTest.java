/*package com.messenger.chatty.service;
import com.messenger.chatty.config.DataCleaner;
import com.messenger.chatty.dto.MemberJoinReqDTO;
import com.messenger.chatty.entity.Channel;
import com.messenger.chatty.entity.Member;
import com.messenger.chatty.entity.Workspace;
import com.messenger.chatty.repository.ChannelRepository;
import com.messenger.chatty.repository.MemberRepository;
import com.messenger.chatty.repository.WorkspaceRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@SpringBootTest
@Transactional
public class TempBusinessLogicTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private DataCleaner cleaner;

    @BeforeEach
    public void setUp(){
        // 워크스페이스 생성
        Workspace toss = Workspace.createWorkspace("toss team", "img.example.com", "토스 개발팀");
        workspaceRepository.save(toss);


        MemberJoinReqDTO memberJoinReqDTO = new MemberJoinReqDTO("suhyeon0119","");
        
                
        // 멤버 생성
        Member suhyeon = Member.from("suhyeon0119","example@","0000",
                "role","suhyeon","우주최강수현",
                "대학생입니다.","img.example.com");
        Member eunji =  Member.from("eunji2231","example@","0000",
                "role","eunji","달리는은지",
                "열심히 일합니다.","img.example.com");

        memberRepository.save(suhyeon);
        memberRepository.save(eunji);

        // 멤버의 워크스페이스 참여
        suhyeon.enterIntoWorkspace(toss);
        eunji.enterIntoWorkspace(toss);

        // 워크스페이스에 채널 등록
        Channel channel1 = Channel.createChannel("일반", toss);
        Channel channel2 = Channel.createChannel("백엔드", toss);// 그대로 추가
        Channel channel3 = Channel.createChannel("프론트엔드", toss);// 그대로 추가

        // 채널에 멤버 등록
        suhyeon.enterIntoChannel(channel1);
        suhyeon.enterIntoChannel(channel2);
        eunji.enterIntoChannel(channel1);


    }
    @AfterEach
    public void clear(){
        cleaner.clear();
    }




    @Test
    @DisplayName("워크스페이스에 있는 채널 수 화인")
    public void testCreateChannelAndAddToWorkspace(){
        Workspace tossTeam = workspaceRepository.findByName("toss team");

        List<Channel> channels = tossTeam.getChannels();

        //then
        Assertions.assertThat(channels).hasSize(3);


    }

    @Test
    @DisplayName("특정 워크스페이스에 있는 채널을 이름으로 검색") // 하나만 검색된다.
    public void testFindChannelInWorkspace(){
        Workspace tossTeam = workspaceRepository.findByName("toss team");
        Channel defautChannel = channelRepository.findByWorkspaceAndName(tossTeam, "일반");

        // then
        Assertions.assertThat(defautChannel).isNotNull();


    }

    @Test
    @DisplayName("특정 멤버가 속한 워크스페이스 리스트 가져오기")
    public void testFindWorkspacesByMemberId(){
        Member suhyeon = memberRepository.findByName("suhyeon");
       List<Workspace> workspaces = workspaceRepository.findWorkspacesByMemberId(suhyeon.getId());

       // then
       // 수현이 현재 속한 워크스페이스는 하나임
        Assertions.assertThat(workspaces).hasSize(1);

    }
    @Test
    @DisplayName("특정 워크스페이스 내의 멤버 리스트 가져오기")
    public void testFindMembersByWorkspaceId(){
        Workspace tossTeam = workspaceRepository.findByName("toss team");
        List<Member> members = memberRepository.findMembersByWorkspaceId(tossTeam.getId());

        // then
        // 워크스페이스 내에는 두명이 있음
        Assertions.assertThat(members).hasSize(2);

    }

    @Test
    @DisplayName("특정 채널 내의 멤버들 리스트 가져오기")
    public void testFindMembersByChannelId() {
        Workspace tossTeam = workspaceRepository.findByName("toss team");
        Channel defaultChannel = channelRepository.findByWorkspaceAndName(tossTeam, "일반");
        List<Member> members = memberRepository.findByChannelId(defaultChannel.getId());


        // then
        // 일반 채널에는 두명이 있음
        Assertions.assertThat(members).hasSize(2);

    }

    @Test
    @DisplayName("멤버 아이디와 워크스페이스 이용하여 멤버가 속한 채널 리스트 가져오기")
    public void testFindChannelsByIWorkspaceAndMemberId(){
        Workspace tossTeam = workspaceRepository.findByName("toss team");
        Member suhyeon = memberRepository.findByName("suhyeon");
        Member eunji = memberRepository.findByName("eunji");

        List<Channel> channelsOfSuhyeonInTossTeam = channelRepository.findByWorkspaceIdAndMemberId(tossTeam.getId(), suhyeon.getId());
        List<Channel> channelsOfEunjiInTossTeam = channelRepository.findByWorkspaceIdAndMemberId(tossTeam.getId(), eunji.getId());

        //then
        // 수현과 은지는 각각 토스 워크스페이스 내에서 2개, 1개의 채널에 속함
        Assertions.assertThat(channelsOfSuhyeonInTossTeam).hasSize(2);
        Assertions.assertThat(channelsOfEunjiInTossTeam).hasSize(1);
    }

}*/
