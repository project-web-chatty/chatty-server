package com.messenger.chatty.service;


import com.messenger.chatty.dto.request.MemberJoinRequestDto;
import com.messenger.chatty.dto.request.MemberUpdateRequestDto;
import com.messenger.chatty.dto.response.channel.ChannelBriefDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
import com.messenger.chatty.dto.response.member.MyProfileDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceBriefDto;

import java.util.List;

public interface MemberService {

    MyProfileDto signup(MemberJoinRequestDto memberJoinRequestDTO) ;
    List<MemberBriefDto> getAllMemberList();

    MemberBriefDto getMemberProfileByMemberId(Long memberId);

    MyProfileDto getMyProfileByUsername(String username) ;

    MyProfileDto updateMyProfile(String targetUsername, MemberUpdateRequestDto updateRequestDto);

    void deleteMeByUsername(String username);
    void deleteMeById(Long id);

    List<WorkspaceBriefDto> getMyWorkspaces(String username);

    void checkDuplicatedUsername(String username);

}
