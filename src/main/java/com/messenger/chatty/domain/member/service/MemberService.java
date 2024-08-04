package com.messenger.chatty.domain.member.service;


import com.messenger.chatty.domain.member.dto.request.MemberJoinRequestDto;
import com.messenger.chatty.domain.member.dto.request.MemberUpdateRequestDto;
import com.messenger.chatty.domain.member.dto.response.MemberBriefDto;
import com.messenger.chatty.domain.member.dto.response.MyProfileDto;
import com.messenger.chatty.domain.workspace.dto.response.WorkspaceBriefDto;

import java.util.List;

public interface MemberService {

    Long signup(MemberJoinRequestDto memberJoinRequestDTO) ;
    List<MemberBriefDto> getAllMemberList();

    MemberBriefDto getMemberProfileByMemberId(Long memberId);

    MyProfileDto getMyProfileByUsername(String username) ;

    Long updateMyProfile(String targetUsername, MemberUpdateRequestDto updateRequestDto);

    void deleteMeByUsername(String username);
    void deleteMeById(Long id);

    List<WorkspaceBriefDto> getMyWorkspaces(String username);

    void checkDuplicatedUsername(String username);

}
