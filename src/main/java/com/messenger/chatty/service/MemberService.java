package com.messenger.chatty.service;


import com.messenger.chatty.dto.request.MemberJoinRequestDTO;
import com.messenger.chatty.dto.response.MemberProfileResponseDto;
import com.messenger.chatty.dto.response.MyProfileResponseDto;
import com.messenger.chatty.exception.custom.DuplicateUsernameException;

import java.util.List;
import java.util.NoSuchElementException;

public interface MemberService {

    void signup(MemberJoinRequestDTO memberJoinRequestDTO) throws DuplicateUsernameException;
    List<MemberProfileResponseDto> getAllMemberList();

    MemberProfileResponseDto findMemberProfileByMemberId(Long memberId) throws NoSuchElementException;

    MyProfileResponseDto findMyProfileByUsername(String username) throws NoSuchElementException;
}
