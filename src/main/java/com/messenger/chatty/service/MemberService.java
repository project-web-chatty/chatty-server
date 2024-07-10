package com.messenger.chatty.service;


import com.messenger.chatty.dto.request.MemberJoinRequestDto;
import com.messenger.chatty.dto.request.MemberProfileUpdateRequestDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
import com.messenger.chatty.dto.response.member.MyProfileDto;
import com.messenger.chatty.exception.custom.DuplicatedNameException;

import java.util.List;
import java.util.NoSuchElementException;

public interface MemberService {

    MyProfileDto signup(MemberJoinRequestDto memberJoinRequestDTO) throws DuplicatedNameException;
    List<MemberBriefDto> getAllMemberList();

    MemberBriefDto findMemberProfileByMemberId(Long memberId) throws NoSuchElementException;

    MyProfileDto findMyProfileByUsername(String username) throws NoSuchElementException;

    MyProfileDto updateMyProfile(String target, String name,String nickname,String introduction);

    void deleteMeByUsername(String username);
    void deleteMeById(Long id);
}
