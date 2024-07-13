package com.messenger.chatty.service;


import com.messenger.chatty.dto.request.MemberJoinRequestDto;
import com.messenger.chatty.dto.request.MemberUpdateRequestDto;
import com.messenger.chatty.dto.response.channel.ChannelBriefDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
import com.messenger.chatty.dto.response.member.MyProfileDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceBriefDto;
import com.messenger.chatty.entity.Channel;
import com.messenger.chatty.entity.Member;
import com.messenger.chatty.entity.Workspace;
import com.messenger.chatty.exception.custom.CustomNoSuchElementException;
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
            throw new DuplicatedNameException(memberJoinRequestDTO.getUsername(),"username");


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
                .orElseThrow(() -> new CustomNoSuchElementException("id",memberId,"회원"));
        return  CustomConverter.convertMemberToBriefDto(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MyProfileDto getMyProfileByUsername(String username) {
        Member me = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNoSuchElementException("username",username,"회원"));
        List<Workspace> myWorkspaces = workspaceRepository.findWorkspacesByMemberId(me.getId());

        return CustomConverter.convertMemberToDto(me,myWorkspaces);
    }

    @Override
    public MyProfileDto updateMyProfile(String targetUsername, MemberUpdateRequestDto memberUpdateRequestDto){

        Member me = memberRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new CustomNoSuchElementException("username",targetUsername,"회원"));

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
                .orElseThrow(() -> new CustomNoSuchElementException("username",username,"회원"));
        memberRepository.deleteByUsername(username);
    }

    @Override
    public void deleteMeById(Long id) {
        Member me = memberRepository.findById(id)
                .orElseThrow(() -> new CustomNoSuchElementException("id",id,"회원"));
        memberRepository.delete(me);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkspaceBriefDto> getMyWorkspaces(String username) {
        Member me = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNoSuchElementException("username",username,"회원"));
        List<Workspace> myWorkspaces = workspaceRepository.findWorkspacesByMemberId(me.getId());
        return myWorkspaces.stream().map(CustomConverter::convertWorkspaceToBriefDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChannelBriefDto> getMyChannels(String username, String workspaceName) {
        Member me = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNoSuchElementException("username",username,"회원"));
        Workspace workspace = workspaceRepository.findByName(workspaceName)
                .orElseThrow(() -> new CustomNoSuchElementException("이름",workspaceName,"워크스페이스"));

        List<Channel> channels = channelRepository.findByWorkspaceIdAndMemberId(workspace.getId(), me.getId());
        return channels.stream().map(CustomConverter::convertChannelToBriefDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public void checkDuplicatedUsername(String username) {
        if(memberRepository.existsByUsername(username))
            throw new DuplicatedNameException(username,"username");
    }
}
