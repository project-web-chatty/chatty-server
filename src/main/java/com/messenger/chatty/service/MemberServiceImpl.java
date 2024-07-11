package com.messenger.chatty.service;


import com.messenger.chatty.dto.request.MemberJoinRequestDto;
import com.messenger.chatty.dto.response.channel.ChannelBriefDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
import com.messenger.chatty.dto.response.member.MyProfileDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceBriefDto;
import com.messenger.chatty.entity.Channel;
import com.messenger.chatty.entity.Member;
import com.messenger.chatty.entity.Workspace;
import com.messenger.chatty.exception.custom.DuplicatedNameException;
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
import java.util.NoSuchElementException;

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
    public MemberBriefDto getMemberProfileByMemberId(String memberName) {
        Member member = memberRepository.findByUsername(memberName)
                .orElseThrow(() -> new NoSuchElementException("there is no member whose member_id is " + memberName));
        return  CustomConverter.convertMemberToBriefDto(member);
    }

    @Override
    public MyProfileDto getMyProfileByUsername(String username) {
        Member me = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("there is no member whose username is " + username));
        List<Workspace> myWorkspaces = workspaceRepository.findWorkspacesByMemberId(me.getId());

        return CustomConverter.convertMemberToDto(me,myWorkspaces);
    }

    @Override
    public MyProfileDto updateMyProfile(String targetUsername, String name,String nickname,String introduction){

        Member me = memberRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new NoSuchElementException("there is no member whose username is " + targetUsername));

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
                .orElseThrow(() -> new NoSuchElementException("there is no member whose username is " + username));
        memberRepository.deleteByUsername(username);
    }

    @Override
    public void deleteMeById(Long id) {
        Member me = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("there is no member whose id is " + id));
        memberRepository.delete(me);
    }

    @Override
    public List<WorkspaceBriefDto> getMyWorkspaces(String username) {
        Member me = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("there is no member whose username is " + username));
        List<Workspace> myWorkspaces = workspaceRepository.findWorkspacesByMemberId(me.getId());
        return myWorkspaces.stream().map(CustomConverter::convertWorkspaceToBriefDto).toList();
    }

    @Override
    public List<ChannelBriefDto> getMyChannels(String username, String workspaceName) {
        Member me = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("there is no member whose username is " + username));
        Workspace workspace = workspaceRepository.findByName(workspaceName).orElseThrow(() -> new NoSuchElementException("dsffds"));
        List<Channel> channels = channelRepository.findByWorkspaceIdAndMemberId(workspace.getId(), me.getId());
        return channels.stream().map(CustomConverter::convertChannelToBriefDto).toList();
    }
}
