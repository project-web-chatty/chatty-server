package com.messenger.chatty.service;


import com.messenger.chatty.dto.request.MemberJoinRequestDto;
import com.messenger.chatty.dto.response.channel.ChannelBriefDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
import com.messenger.chatty.dto.response.member.MyProfileDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceBriefDto;
import com.messenger.chatty.exception.custom.DuplicatedNameException;

import java.util.List;
import java.util.NoSuchElementException;

public interface MemberService {

    MyProfileDto signup(MemberJoinRequestDto memberJoinRequestDTO) throws DuplicatedNameException;
    List<MemberBriefDto> getAllMemberList();

    MemberBriefDto getMemberProfileByMemberId(String memberName) throws NoSuchElementException;

    MyProfileDto getMyProfileByUsername(String username) throws NoSuchElementException;

    MyProfileDto updateMyProfile(String targetUsername, String name,String nickname,String introduction);

    void deleteMeByUsername(String username);
    void deleteMeById(Long id);

    List<WorkspaceBriefDto> getMyWorkspaces(String username);
    List<ChannelBriefDto> getMyChannels(String username, String workspaceName);

}
