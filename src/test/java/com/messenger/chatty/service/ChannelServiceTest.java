package com.messenger.chatty.service;

import com.messenger.chatty.dto.request.ChannelGenerateRequestDto;
import com.messenger.chatty.dto.request.MemberJoinRequestDto;
import com.messenger.chatty.dto.request.WorkspaceGenerateRequestDto;
import com.messenger.chatty.dto.response.channel.ChannelBriefDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
import com.messenger.chatty.entity.Channel;
import com.messenger.chatty.repository.ChannelRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@SpringBootTest
class ChannelServiceTest {
    //Service
    @Autowired
    ChannelService channelService;
    @Autowired
    WorkspaceService workspaceService;
    @Autowired
    MemberService memberService;
    @Autowired
    DatabaseCleanup databaseCleanup;
    //Repository
    @Autowired
    ChannelRepository channelRepository;

    //Entity&Dto&Else
    Long memberId1;
    Long memberId2;
    Long workspaceId;
    MemberBriefDto memberResponse1;
    MemberBriefDto memberResponse2;

    @BeforeEach
    void setUp() {
        MemberJoinRequestDto signUp1 = MemberJoinRequestDto.builder()
                .username("username1")
                .password("password1")
                .build();
        memberId1 = memberService.signup(signUp1);
        memberResponse1 = memberService.getMemberProfileByMemberId(memberId1);
        MemberJoinRequestDto signUp2 = MemberJoinRequestDto.builder()
                .username("username2")
                .password("password2")
                .build();
        memberId2 = memberService.signup(signUp2);
        memberResponse2 = memberService.getMemberProfileByMemberId(memberId2);
        WorkspaceGenerateRequestDto workspaceDto = WorkspaceGenerateRequestDto.builder()
                .name("name")
                .description("description")
                .build();
        workspaceId = workspaceService.generateWorkspace(workspaceDto, memberResponse1.getUsername());
    }

    @AfterEach
    void cleanUp() {
        databaseCleanup.truncateAllEntity();
    }

    @Test
    @DisplayName("워크스페이스 내 채널을 추가 생성합니다. 이때 생성 권한이 필요하며 중복된 이름은 사용할 수 없습니다.")
    void createChannelInWorkspace() {
        //given
        ChannelGenerateRequestDto channel = ChannelGenerateRequestDto.builder()
                .name("channel")
                .build();
        //when
        Long channelId = channelService.createChannelToWorkspace(workspaceId, channel);
        //then
        List<ChannelBriefDto> channelsOfWorkspace = workspaceService.getChannelsOfWorkspace(workspaceId);
        assertThat(channelsOfWorkspace.size()).isEqualTo(3);
        assertThat(channelsOfWorkspace.get(2).getName()).isEqualTo(channel.getName());
    }
    @Test
    @DisplayName("특정 채널을 삭제합니다. 이때 삭제 권한이 필요합니다.")
    void deleteChannel() {
        //given
        ChannelGenerateRequestDto channel = ChannelGenerateRequestDto.builder()
                .name("channel")
                .build();
        Long channelId = channelService.createChannelToWorkspace(workspaceId, channel);
        //when
        channelService.deleteChannelInWorkspace(workspaceId, channelId);

        //then
        List<Channel> all = channelRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
    }

}