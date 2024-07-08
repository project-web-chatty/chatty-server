package com.messenger.chatty.service;


import com.messenger.chatty.dto.request.MemberJoinRequestDTO;
import com.messenger.chatty.dto.response.MemberResponseDTO;
import com.messenger.chatty.exception.custom.DuplicateUsernameException;

import java.util.List;

public interface MemberService {

    void signup(MemberJoinRequestDTO memberJoinRequestDTO) throws DuplicateUsernameException;
    List<MemberResponseDTO> getAllMemberList();
}
