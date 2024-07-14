package com.messenger.chatty.service;


import com.messenger.chatty.dto.response.workspace.WorkspaceResponseDto;
import com.messenger.chatty.entity.Channel;
import com.messenger.chatty.entity.ChannelJoin;
import com.messenger.chatty.entity.Member;
import com.messenger.chatty.entity.Workspace;
import com.messenger.chatty.exception.custom.CustomNoSuchElementException;
import com.messenger.chatty.exception.custom.DuplicatedNameException;
import com.messenger.chatty.exception.custom.InvalidInvitationCodeException;
import com.messenger.chatty.repository.*;
import com.messenger.chatty.security.InvitationCodeGenerator;
import com.messenger.chatty.util.CustomConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InviteServiceImpl implements InviteService {

    private final WorkspaceRepository workspaceRepository;
    private final ChannelRepository channelRepository;
    private final MemberRepository memberRepository;
    private final ChannelJoinRepository channelJoinRepository;
    private final WorkspaceJoinRepository workspaceJoinRepository;
    private final InvitationCodeGenerator invitationCodeGenerator;



    @Override
    @Transactional(readOnly = true)
    public String getNewInvitationCode(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomNoSuchElementException("아이디", workspaceId, "워크스페이스"));
        return workspace.getInvitationCode();
    }

    @Override
    public String setInvitationCode(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomNoSuchElementException("아이디", workspaceId, "워크스페이스"));
        String newCode = invitationCodeGenerator.generateInviteCode();
        workspace.changeInvitationCode(newCode);
        return newCode;
    }


    @Override
    public WorkspaceResponseDto enterToWorkspace(String username, String code) {

        // code 검증
        Workspace workspace = workspaceRepository.findByInvitationCode(code)
                .orElseThrow(()-> new InvalidInvitationCodeException("유효하지 않은 코드입니다."));

        // 중복 회원 대비 검증
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNoSuchElementException("username",username,"회원"));
        if(workspaceJoinRepository.existsByWorkspaceIdAndMemberId(workspace.getId(), member.getId()))
            throw new DuplicatedNameException("이미 이 워크스페이스 내에 존재하는 회원입니다.");



        // 해당 멤버를 워크스페이스에 참여
        member.enterIntoWorkspace(workspace,"ROLE_WORKSPACE_MEMBER");
        // 멤버를 추가 시 워크 스페이스 내 모든 채널에 멤버가 속하도록 함 (dm 기능 등의 도입 시 수정 요망)
        List<Channel> channels = channelRepository.findByWorkspace(workspace);
        channels.forEach((channel)->{
            ChannelJoin channelJoin = ChannelJoin.from(channel,member);
            channelJoinRepository.save(channelJoin);
        });


        Workspace savedWorkspace = workspaceRepository.save(workspace);
        List<Member> members = memberRepository.findMembersByWorkspaceId(workspace.getId());
        return CustomConverter.convertWorkspaceToDto(savedWorkspace,channels,members);

    }
}
