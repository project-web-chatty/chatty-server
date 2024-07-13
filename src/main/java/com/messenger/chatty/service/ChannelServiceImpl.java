package com.messenger.chatty.service;


import com.messenger.chatty.dto.request.ChannelGenerateRequestDto;
import com.messenger.chatty.dto.response.channel.ChannelBriefDto;
import com.messenger.chatty.entity.Channel;
import com.messenger.chatty.entity.ChannelJoin;
import com.messenger.chatty.entity.Member;
import com.messenger.chatty.entity.Workspace;
import com.messenger.chatty.exception.custom.DuplicatedNameException;
import com.messenger.chatty.exception.custom.CustomNoSuchElementException;
import com.messenger.chatty.repository.ChannelJoinRepository;
import com.messenger.chatty.repository.ChannelRepository;
import com.messenger.chatty.repository.MemberRepository;
import com.messenger.chatty.repository.WorkspaceRepository;
import com.messenger.chatty.util.CustomConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService{
    private final ChannelRepository channelRepository;
    private final WorkspaceRepository workspaceRepository;
    private final MemberRepository memberRepository;
    private final ChannelJoinRepository channelJoinRepository;

    @Override
    public ChannelBriefDto createChannelToWorkspace(Long workspaceId, ChannelGenerateRequestDto requestDto) {

        String channelName = requestDto.getName();

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomNoSuchElementException("id",workspaceId,"워크스페이스"));

        // 채널은 같은 워크스페이스 내에서는 이름이 동일하지 않아야 한다.
        if(channelRepository.findByWorkspaceAndName(workspace,channelName).isPresent())
            throw new DuplicatedNameException(channelName,"채널이름");

        Channel channel = Channel.createChannel(channelName, workspace);

        // 만들어진 채널에 현재 워크스페이스에 있는 모든 멤버를 채널에 등록
        memberRepository.findMembersByWorkspaceId(workspace.getId())
                .forEach(member -> {
                    ChannelJoin channelJoin = ChannelJoin.from(channel,member);
                    channelJoinRepository.save(channelJoin);
                });


        Channel savedChannel = channelRepository.save(channel);
        return CustomConverter.convertChannelToBriefDto(savedChannel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChannelBriefDto> getAllChannels() {
        return channelRepository.findAll().stream().map(CustomConverter::convertChannelToBriefDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChannelBriefDto> getChannelsOfMemberAndWorkspace(Long channelId, String username) {
        Workspace workspace = workspaceRepository.findById(channelId)
                .orElseThrow(() -> new CustomNoSuchElementException("id",channelId,"워크스페이스"));
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNoSuchElementException("username",username,"회원"));

        List<Channel> channels = channelRepository.findByWorkspaceIdAndMemberId(workspace.getId(), member.getId());
        return channels.stream().map(CustomConverter::convertChannelToBriefDto).toList();
    }

    @Override
    public void deleteChannelInWorkspace(Long channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomNoSuchElementException("채널ID", channelId, "채널"));
        channelRepository.delete(channel);
    }
}
