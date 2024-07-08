package com.messenger.chatty.service;


import com.messenger.chatty.dto.MemberJoinRequestDTO;
import com.messenger.chatty.entity.Member;
import com.messenger.chatty.exception.custom.DuplicateUsernameException;
import com.messenger.chatty.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder bcrptPasswordEncoder;

    @Override
    public void signup(MemberJoinRequestDTO memberJoinRequestDTO){
        boolean isExist = memberRepository.existsByUsername(memberJoinRequestDTO.getUsername());
        if(isExist) throw new DuplicateUsernameException("duplicated username");
        memberJoinRequestDTO.changePassword(bcrptPasswordEncoder.encode(memberJoinRequestDTO.getPassword()));
        memberRepository.save(Member.from(memberJoinRequestDTO));
    };


}
