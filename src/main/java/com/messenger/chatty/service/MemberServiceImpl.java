package com.messenger.chatty.service;


import com.messenger.chatty.dto.request.MemberJoinRequestDTO;
import com.messenger.chatty.dto.response.MemberResponseDTO;
import com.messenger.chatty.entity.Member;
import com.messenger.chatty.exception.custom.DuplicateUsernameException;
import com.messenger.chatty.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public List<MemberResponseDTO> getAllMemberList() {
        List<Member> memberList = memberRepository.findAll();
        return memberList.stream().map(this::convertMemberToDto).toList();

    }




    private MemberResponseDTO convertMemberToDto(Member member){
        return MemberResponseDTO.builder().id(member.getId())
                .username(member.getUsername())
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getRole())
                .profile_img(member.getProfile_img())
                .nickname(member.getNickname())
                .introduction(member.getIntroduction())
                .createdDate(member.getCreatedDate())
                .lastModifiedDate(member.getLastModifiedDate())
                .build();
    }
}
