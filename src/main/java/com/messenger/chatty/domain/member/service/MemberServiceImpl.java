package com.messenger.chatty.domain.member.service;


import com.messenger.chatty.domain.member.dto.request.MemberJoinRequestDto;
import com.messenger.chatty.domain.member.dto.request.MemberUpdateRequestDto;
import com.messenger.chatty.domain.member.dto.response.MemberBriefDto;
import com.messenger.chatty.domain.member.dto.response.MyProfileDto;
import com.messenger.chatty.domain.workspace.dto.response.WorkspaceBriefDto;
import com.messenger.chatty.domain.member.entity.Member;
import com.messenger.chatty.domain.workspace.entity.Workspace;
import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.global.presentation.exception.custom.MemberException;
import com.messenger.chatty.domain.channel.repository.ChannelRepository;
import com.messenger.chatty.domain.member.repository.MemberRepository;
import com.messenger.chatty.domain.workspace.repository.WorkspaceRepository;
import com.messenger.chatty.global.service.S3Service;
import com.messenger.chatty.global.util.CustomConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final WorkspaceRepository workspaceRepository;
    private final ChannelRepository channelRepository;
    private final PasswordEncoder bcrptPasswordEncoder;
    private final S3Service s3Service;

    @Override
    public Long signup(MemberJoinRequestDto memberJoinRequestDTO){

        if(memberRepository.existsByUsername(memberJoinRequestDTO.getUsername()))
            throw new MemberException(ErrorStatus.MEMBER_USERNAME_ALREADY_EXISTS);


        memberJoinRequestDTO.encodePassword(bcrptPasswordEncoder.encode(memberJoinRequestDTO.getPassword()));
        Member me = Member.from(memberJoinRequestDTO);
        memberRepository.save(me);
        return me.getId();
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
    public Long updateMyProfile(String targetUsername, MemberUpdateRequestDto memberUpdateRequestDto){
        Member me = memberRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

       me.updateProfile(memberUpdateRequestDto);

        return me.getId();
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

    @Override
    public String uploadMyProfileImage(String username, MultipartFile file) {
        Member me = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));
        String oldProfileImgURI = me.getProfile_img();
        if(!(oldProfileImgURI ==null)) s3Service.deleteImage(oldProfileImgURI);

        String newProfileImgURI = s3Service.uploadImage(file);
        me.changeProfileImg(newProfileImgURI);
        return newProfileImgURI;
    }

    @Override
    public void deleteMyProfileImage(String username) {
        Member me = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));
        String profileImgURI = me.getProfile_img();
        if((profileImgURI ==null)) return;
        s3Service.deleteImage(profileImgURI);
    }
}
