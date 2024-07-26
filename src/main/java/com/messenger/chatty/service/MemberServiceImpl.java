package com.messenger.chatty.service;


import com.messenger.chatty.dto.request.MemberJoinRequestDto;
import com.messenger.chatty.dto.request.MemberUpdateRequestDto;
import com.messenger.chatty.dto.response.channel.ChannelBriefDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
import com.messenger.chatty.dto.response.member.MyProfileDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceBriefDto;
import com.messenger.chatty.entity.Member;
import com.messenger.chatty.entity.Workspace;
import com.messenger.chatty.presentation.ErrorStatus;
import com.messenger.chatty.presentation.exception.custom.MemberException;
import com.messenger.chatty.repository.ChannelRepository;
import com.messenger.chatty.repository.MemberRepository;
import com.messenger.chatty.repository.WorkspaceRepository;
import com.messenger.chatty.util.CustomConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final WorkspaceRepository workspaceRepository;
    private final ChannelRepository channelRepository;
    private final PasswordEncoder bcrptPasswordEncoder;

    @Override
    public MyProfileDto signup(MemberJoinRequestDto memberJoinRequestDTO){

        if(memberRepository.existsByUsername(memberJoinRequestDTO.getUsername()))
            throw new MemberException(ErrorStatus.MEMBER_USERNAME_ALREADY_EXISTS);


        memberJoinRequestDTO.encodePassword(bcrptPasswordEncoder.encode(memberJoinRequestDTO.getPassword()));
        Member me = Member.from(memberJoinRequestDTO);
        memberRepository.save(me);
        return CustomConverter.convertMemberToDto(me, Collections.emptyList());
    }


    @Override
    @Transactional(readOnly = true)
    public List<MemberBriefDto> getAllMemberList() {
        List<Member> memberList = memberRepository.findAll();
        return memberList.stream().map(CustomConverter::convertMemberToBriefDto).toList();

    }

    @Override
    @Transactional(readOnly = true)
    public MemberBriefDto getMemberProfileByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));
        return  CustomConverter.convertMemberToBriefDto(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MyProfileDto getMyProfileByUsername(String username) {
        Member me = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));
        List<Workspace> myWorkspaces = workspaceRepository.findWorkspacesByMemberId(me.getId());

        return CustomConverter.convertMemberToDto(me,myWorkspaces);
    }

    @Override
    public MyProfileDto updateMyProfile(String targetUsername, MemberUpdateRequestDto memberUpdateRequestDto){

        Member me = memberRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        String name = memberUpdateRequestDto.getName();
        String nickname = memberUpdateRequestDto.getNickname();
        String introduction = memberUpdateRequestDto.getIntroduction();

        // 이미지 업로드 기능은 차후 구현
        if(name !=null) me.changeName(name);
        if(nickname !=null) me.changeNickname(nickname);
        if(introduction !=null) me.changeIntroduction(introduction);

        Member saved = memberRepository.save(me);
        List<Workspace> myWorkspaces = workspaceRepository.findWorkspacesByMemberId(saved.getId());

        return CustomConverter.convertMemberToDto(me,myWorkspaces);
    }

    @Override
    public void deleteMeByUsername(String username){
        Member me = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));
        memberRepository.deleteByUsername(username);
    }

    @Override
    public void deleteMeById(Long id) {
        Member me = memberRepository.findById(id)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));
        memberRepository.delete(me);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkspaceBriefDto> getMyWorkspaces(String username) {
        Member me = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));
        List<Workspace> myWorkspaces = workspaceRepository.findWorkspacesByMemberId(me.getId());
        return myWorkspaces.stream().map(CustomConverter::convertWorkspaceToBriefDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public void checkDuplicatedUsername(String username) {
        if(memberRepository.existsByUsername(username))
            throw new MemberException(ErrorStatus.MEMBER_USERNAME_ALREADY_EXISTS);
    }
}
