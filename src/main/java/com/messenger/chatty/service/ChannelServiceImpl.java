package com.messenger.chatty.service;


import com.messenger.chatty.dto.response.channel.ChannelBriefDto;
import com.messenger.chatty.entity.Channel;
import com.messenger.chatty.entity.ChannelJoin;
import com.messenger.chatty.entity.Member;
import com.messenger.chatty.entity.Workspace;
import com.messenger.chatty.exception.custom.DuplicatedNameException;
import com.messenger.chatty.repository.ChannelJoinRepository;
import com.messenger.chatty.repository.ChannelRepository;
import com.messenger.chatty.repository.MemberRepository;
import com.messenger.chatty.repository.WorkspaceRepository;
import com.messenger.chatty.util.CustomConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService{
    private final ChannelRepository channelRepository;
    private final WorkspaceRepository workspaceRepository;
    private final MemberRepository memberRepository;
    private final ChannelJoinRepository channelJoinRepository;

    @Override
    public ChannelBriefDto createChannelToWorkspace(String targetWorkspaceName, String channelName) {


        Workspace workspace = workspaceRepository.findByName(targetWorkspaceName)
                .orElseThrow(() -> new NoSuchElementException("there is no workspace which name is " + targetWorkspaceName));

        // channel's name must be unique in the same workspace
        if(channelRepository.findByWorkspaceAndName(workspace,channelName).isPresent())
            throw new DuplicatedNameException("duplicated channel name in the same workspace : " +channelName);

        Channel channel = Channel.createChannel(channelName, workspace);

        // 만들어진 채널에 현재 워크스페이스에 있는 모든 멤버를 채널에 등록
        memberRepository.findMembersByWorkspaceId(workspace.getId())
                .forEach(member -> {
                    System.out.println("enter executed");
                    ChannelJoin channelJoin = new ChannelJoin(channel,member);
                    channelJoinRepository.save(channelJoin);
                });


        Channel saved = channelRepository.save(channel);



        return CustomConverter.convertChannelToBriefDto(saved);
    }

    @Override
    public List<ChannelBriefDto> getChannelsOfMemberAndWorkspace(String workspaceName, String username) {
        Workspace workspace = workspaceRepository.findByName(workspaceName).orElseThrow(() -> new NoSuchElementException("dsfsdfsfd"));
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("dfssfddfs"));

        List<Channel> channels = channelRepository.findByWorkspaceIdAndMemberId(workspace.getId(), member.getId());
        return channels.stream().map(CustomConverter::convertChannelToBriefDto).toList();
    }



}
