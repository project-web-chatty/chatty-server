package com.messenger.chatty.service;
import com.messenger.chatty.config.DataCleaner;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
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


        // 멤버 생성
        Member suhyeon = Member.createMember("suhyeon0119","example@","0000",
                "role","suhyeon","우주최강수현",
                "대학생입니다.","img.example.com");
        Member eunji =  Member.createMember("eunji2231","example@","0000",
                "role","eunji","달리는은지",
                "열심히 일합니다.","img.example.com");

        memberRepository.save(suhyeon);
        memberRepository.save(eunji);

        // 멤버의 워크스페이스 참여
        suhyeon.joinWorkspace(toss);
        eunji.joinWorkspace(toss);

        // 워크스페이스에 채널 등록
        Channel channel1 = Channel.createChannel("일반", toss);
        Channel channel2 = Channel.createChannel("백엔드", toss);// 그대로 추가
        Channel channel3 = Channel.createChannel("프론트엔드", toss);// 그대로 추가

        // 채널에 멤버 등록
        suhyeon.joinChannel(channel1);
        suhyeon.joinChannel(channel2);
        eunji.joinChannel(channel1);


    }
    @AfterEach
    public void clear(){
        cleaner.clear();
    }



    @Test
    @DisplayName("워크스페이스에 참여하는 멤버들의 수 확인")
    public void testCreateWorkspaceAndAddMembers(){
        Workspace tossFoundByName = workspaceRepository.findByName("toss team");
        Assertions.assertThat(tossFoundByName.getAllMembers()).hasSize(2);
    }

    @Test
    @DisplayName("워크스페이스에 있는 채널 수 화인")
    public void testCreateChannelAndAddToWorkspace(){
        Workspace tossTeam = workspaceRepository.findByName("toss team");

        List<Channel> channels = tossTeam.getChannels();
        Assertions.assertThat(channels).hasSize(3);


    }

    @Test
    @DisplayName("특정 워크스페이스에 있는 채널을 이름으로 검색") // 하나만 검색된다.
    public void testFindChannelInWorkspace(){
        Workspace tossTeam = workspaceRepository.findByName("toss team");
        Channel defautChannel = channelRepository.findByWorkspaceAndName(tossTeam, "일반");
        Channel backendChannel = channelRepository.findByWorkspaceAndName(tossTeam, "백엔드");
        Channel frontendChannel = channelRepository.findByWorkspaceAndName(tossTeam, "프론트엔드");


        Assertions.assertThat(defautChannel.getMembers()).hasSize(2);
        Assertions.assertThat(backendChannel.getMembers()).hasSize(1);
        Assertions.assertThat(frontendChannel.getMembers()).hasSize(0);


    }


}
