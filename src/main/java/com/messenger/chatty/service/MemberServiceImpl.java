package com.messenger.chatty.service;


import com.messenger.chatty.dto.request.MemberJoinRequestDTO;
import com.messenger.chatty.dto.response.MemberProfileResponseDto;
import com.messenger.chatty.dto.response.MyProfileResponseDto;
import com.messenger.chatty.dto.response.WorkspaceResponseDto;
import com.messenger.chatty.entity.Member;
import com.messenger.chatty.entity.Workspace;
import com.messenger.chatty.exception.custom.DuplicateUsernameException;
import com.messenger.chatty.repository.MemberRepository;
import com.messenger.chatty.repository.WorkspaceRepository;
import com.messenger.chatty.util.DtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void signup(MemberJoinRequestDTO memberJoinRequestDTO){
        boolean isExist = memberRepository.existsByUsername(memberJoinRequestDTO.getUsername());
        if(isExist) throw new DuplicateUsernameException("duplicated username");
        memberJoinRequestDTO.changePassword(bcrptPasswordEncoder.encode(memberJoinRequestDTO.getPassword()));
        memberRepository.save(Member.from(memberJoinRequestDTO));
    }

    @Override
    public List<MemberProfileResponseDto> getAllMemberList() {
        List<Member> memberList = memberRepository.findAll();
        return memberList.stream().map(DtoConverter::convertMemberToDto).toList();

    }

    @Override
    public MemberProfileResponseDto findMemberProfileByMemberId(Long memberId) throws NoSuchElementException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("there is no member whose member_id is " + memberId));
        return  DtoConverter.convertMemberToDto(member);
    }

    @Override
    public MyProfileResponseDto findMyProfileByUsername(String username) throws NoSuchElementException {
        Member me = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("there is no member whose username is " + username));
        List<Workspace> myWorkspaces = workspaceRepository.findWorkspacesByMemberId(me.getId());
        List<WorkspaceResponseDto> workspaceReslist = myWorkspaces.stream().map(DtoConverter::convertWorkspaceToDto).toList();

        // downCasting is dangerous so refactor it later
        MyProfileResponseDto myProfileResponseDto = (MyProfileResponseDto) DtoConverter.convertMemberToDto(me);
        myProfileResponseDto.setMyWorkspaces(workspaceReslist);
        return myProfileResponseDto;
    }






}
