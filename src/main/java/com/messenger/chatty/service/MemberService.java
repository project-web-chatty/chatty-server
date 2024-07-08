package com.messenger.chatty.service;


import com.messenger.chatty.dto.MemberJoinRequestDTO;

public interface MemberService {

    void signup(MemberJoinRequestDTO memberJoinRequestDTO);
}
