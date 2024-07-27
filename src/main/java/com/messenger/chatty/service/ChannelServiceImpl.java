package com.messenger.chatty.service;
import com.messenger.chatty.dto.request.ChannelGenerateRequestDto;
import com.messenger.chatty.dto.response.channel.ChannelBriefDto;
import com.messenger.chatty.entity.Channel;
import com.messenger.chatty.entity.Workspace;
import com.messenger.chatty.presentation.ErrorStatus;
import com.messenger.chatty.presentation.exception.custom.ChannelException;
import com.messenger.chatty.presentation.exception.custom.WorkspaceException;
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
    @Override
    public Long createChannelToWorkspace(Long workspaceId, ChannelGenerateRequestDto requestDto) {

        String channelName = requestDto.getName();

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new WorkspaceException(ErrorStatus.WORKSPACE_NOT_FOUND));


        //TODO 생성 권한 체크
        if(workspace.getChannels().stream().map(Channel::getName).toList().contains(channelName))
            throw new ChannelException(ErrorStatus.CHANNEL_NAME_ALREADY_EXISTS);

        Channel channel = Channel.createChannel(channelName, workspace);
        Channel savedChannel = channelRepository.save(channel);
        return savedChannel.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChannelBriefDto> getAllChannels() {
        return channelRepository.findAll().stream().map(CustomConverter::convertChannelToBriefDto).toList();
    }

    /*
    @Override
    @Transactional(readOnly = true)
    public List<ChannelBriefDto> getChannelsOfMemberAndWorkspace(Long workspaceId, String username) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomNoSuchElementException("id",workspaceId,"워크스페이스"));
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNoSuchElementException("username",username,"회원"));

        List<Channel> channels = channelRepository.findByWorkspaceIdAndMemberId(workspace.getId(), member.getId());
        return channels.stream().map(CustomConverter::convertChannelToBriefDto).toList();
    }

     */


    @Override
    public void deleteChannelInWorkspace(Long workspaceId, Long channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ChannelException(ErrorStatus.CHANNEL_NOT_FOUND));
        if(!channel.getWorkspace().getId().equals(workspaceId))     //해당 과정은 굳이 필요 없을 것 같습니다
            throw new ChannelException(ErrorStatus.CHANNEL_NOT_IN_WORKSPACE);
        //TODO 삭제 권한

        channelRepository.delete(channel);
    }
}
