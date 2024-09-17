package com.messenger.chatty.domain.channel.service;

import com.messenger.chatty.domain.channel.dto.request.ChannelGenerateRequestDto;
import com.messenger.chatty.domain.channel.dto.response.ChannelBriefDto;
import com.messenger.chatty.domain.channel.entity.Channel;
import com.messenger.chatty.domain.channel.entity.ChannelAccess;
import com.messenger.chatty.domain.channel.repository.ChannelAccessRepository;
import com.messenger.chatty.domain.channel.repository.ChannelRepository;
import com.messenger.chatty.domain.member.entity.Member;
import com.messenger.chatty.domain.member.repository.MemberRepository;
import com.messenger.chatty.domain.message.repository.MessageRepository;
import com.messenger.chatty.domain.workspace.entity.Workspace;
import com.messenger.chatty.domain.workspace.repository.WorkspaceRepository;
import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.global.presentation.exception.custom.ChannelException;
import com.messenger.chatty.global.presentation.exception.custom.MemberException;
import com.messenger.chatty.global.presentation.exception.custom.WorkspaceException;
import com.messenger.chatty.global.util.CustomConverter;
import com.messenger.chatty.global.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService{
    private final ChannelRepository channelRepository;
    private final WorkspaceRepository workspaceRepository;
    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;
    private final ChannelAccessRepository channelAccessRepository;
    @Override
    public Long createChannelToWorkspace(Long workspaceId, ChannelGenerateRequestDto requestDto) {

        String channelName = requestDto.getName();

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new WorkspaceException(ErrorStatus.WORKSPACE_NOT_FOUND));


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

        channelRepository.delete(channel);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateEnterChannel(Long channelId, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ChannelException(ErrorStatus.CHANNEL_NOT_FOUND));
        return channel.getWorkspace().getWorkspaceJoins().stream()
                .anyMatch(workspaceJoin -> workspaceJoin.getMember().equals(member));
    }

    @Override
    public void updateAccessTime(Long channelId, String username, LocalDateTime currentTime) {
        ChannelAccess channelAccess = channelAccessRepository
                .findChannelAccessByChannel_IdAndUsername(channelId, username)
                .orElseThrow(() -> new ChannelException(ErrorStatus.CHANNEL_ACCESS_NOT_FOUND));
        //accesstime -> lastModifiedTime으로 사용 / 마지막으로 읽은 메세지 아이디 변경
        messageRepository
                .findLastMessageBeforeTime(channelId, TimeUtil.convertTimeTypeToLong(currentTime))
                .ifPresent(channelAccess::updateAccessTime);
    }

    @Override
    public Long createAccessTime(Long channelId, String username) {
        return channelAccessRepository.save(builderChannelAccess(username, channelId)).getId();
    }

    @Override
    public boolean hasAccessTime(Long channelId, String username) {
        return channelAccessRepository.existsByChannelIdAndUsername(channelId, username);
    }

    private ChannelAccess builderChannelAccess(String username, Long channelId) {
        return ChannelAccess.builder()
                .username(username)
                .channel(channelRepository.findById(channelId)
                        .orElseThrow(() -> new ChannelException(ErrorStatus.CHANNEL_NOT_FOUND)))
                .build();
    }
}
