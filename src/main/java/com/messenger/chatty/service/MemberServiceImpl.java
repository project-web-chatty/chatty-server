package com.messenger.chatty.service;


import com.messenger.chatty.dto.request.MemberJoinRequestDto;
import com.messenger.chatty.dto.request.MemberProfileUpdateRequestDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
import com.messenger.chatty.dto.response.member.MyProfileDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceBriefDto;
import com.messenger.chatty.entity.Member;
import com.messenger.chatty.entity.Workspace;
import com.messenger.chatty.exception.custom.DuplicatedNameException;
import com.messenger.chatty.repository.MemberRepository;
import com.messenger.chatty.repository.WorkspaceRepository;
import com.messenger.chatty.util.CustomConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final WorkspaceRepository workspaceRepository;
    private final PasswordEncoder bcrptPasswordEncoder;

    @Override
    public MyProfileDto signup(MemberJoinRequestDto memberJoinRequestDTO){

        if(memberRepository.existsByUsername(memberJoinRequestDTO.getUsername()))
            throw new DuplicatedNameException("duplicated username : "+ memberJoinRequestDTO.getUsername());

        memberJoinRequestDTO.encodePassword(bcrptPasswordEncoder.encode(memberJoinRequestDTO.getPassword()));
        Member me = Member.from(memberJoinRequestDTO);
        memberRepository.save(me);
        return CustomConverter.convertMemberToDto(me, Collections.emptyList());
    }

    @Override
    public List<MemberBriefDto> getAllMemberList() {
        List<Member> memberList = memberRepository.findAll();
        return memberList.stream().map(CustomConverter::convertMemberToBriefDto).toList();

    }

    @Override
    public MemberBriefDto findMemberProfileByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("there is no member whose member_id is " + memberId));
        return  CustomConverter.convertMemberToBriefDto(member);
    }

    @Override
    public MyProfileDto findMyProfileByUsername(String username) {
        Member me = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("there is no member whose username is " + username));
        List<Workspace> myWorkspaces = workspaceRepository.findWorkspacesByMemberId(me.getId());

        return CustomConverter.convertMemberToDto(me,myWorkspaces);
    }

    @Override
    public MyProfileDto updateMyProfile(String username, MemberProfileUpdateRequestDto updateRequestDto){
        Member me = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("there is no member whose username is " + username));

        updateRequestDto.getName().ifPresent(me::changeName);
        updateRequestDto.getNickname().ifPresent(me::changeNickname);
        updateRequestDto.getIntroduction().ifPresent(me::changeIntroduction);

        memberRepository.save(me);
        List<Workspace> myWorkspaces = workspaceRepository.findWorkspacesByMemberId(me.getId());

        return CustomConverter.convertMemberToDto(me,myWorkspaces);
    }

    @Override
    public void deleteMeByUsername(String username){
        Member me = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("there is no member whose username is " + username));
        memberRepository.delete(me);
    }

    @Override
    public void deleteMeById(Long id) {
        Member me = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("there is no member whose id is " + id));
        memberRepository.delete(me);
    }
}
