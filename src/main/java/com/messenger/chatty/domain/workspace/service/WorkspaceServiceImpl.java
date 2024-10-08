package com.messenger.chatty.domain.workspace.service;
import com.messenger.chatty.domain.channel.entity.Channel;
import com.messenger.chatty.domain.channel.repository.ChannelRepository;
import com.messenger.chatty.domain.member.dto.response.MemberInWorkspaceDto;
import com.messenger.chatty.domain.member.entity.Member;
import com.messenger.chatty.domain.member.repository.MemberRepository;
import com.messenger.chatty.domain.message.repository.MessageRepository;
import com.messenger.chatty.domain.workspace.entity.Workspace;
import com.messenger.chatty.domain.workspace.entity.WorkspaceJoin;
import com.messenger.chatty.domain.workspace.dto.request.WorkspaceGenerateRequestDto;
import com.messenger.chatty.domain.workspace.dto.request.WorkspaceUpdateRequestDto;
import com.messenger.chatty.domain.channel.dto.response.ChannelBriefDto;
import com.messenger.chatty.domain.member.dto.response.MemberBriefDto;
import com.messenger.chatty.domain.workspace.dto.response.WorkspaceBriefDto;
import com.messenger.chatty.domain.workspace.repository.WorkspaceJoinRepository;
import com.messenger.chatty.domain.workspace.repository.WorkspaceRepository;
import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.global.presentation.exception.custom.MemberException;
import com.messenger.chatty.global.presentation.exception.custom.WorkspaceException;
import com.messenger.chatty.global.service.S3Service;
import com.messenger.chatty.global.util.InvitationCodeGenerator;
import com.messenger.chatty.global.util.CustomConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.messenger.chatty.domain.workspace.entity.WorkspaceRole.ROLE_WORKSPACE_MEMBER;
import static com.messenger.chatty.domain.workspace.entity.WorkspaceRole.ROLE_WORKSPACE_OWNER;


@Service
@Transactional
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService{

    private final S3Service s3Service;
    private final MemberRepository memberRepository;
    private final WorkspaceRepository workspaceRepository;
    private final ChannelRepository channelRepository;
    private final InvitationCodeGenerator invitationCodeGenerator;
    private final WorkspaceJoinRepository workspaceJoinRepository;
    private final MessageRepository messageRepository;



    @Override
    @Transactional(readOnly = true)
    public WorkspaceBriefDto getWorkspaceProfile(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new WorkspaceException(ErrorStatus.WORKSPACE_NOT_FOUND));

        return  CustomConverter.convertWorkspaceToBriefDto(workspace);

    }

    @Override
    @Transactional(readOnly = true)
    public WorkspaceBriefDto getWorkspaceBriefProfile(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() ->  new WorkspaceException(ErrorStatus.WORKSPACE_NOT_FOUND));
        return CustomConverter.convertWorkspaceToBriefDto(workspace);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberInWorkspaceDto> getMembersOfWorkspace(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() ->  new WorkspaceException(ErrorStatus.WORKSPACE_NOT_FOUND));

        List<Member> members = workspaceJoinRepository.findMembersByWorkspaceId(workspace.getId());

        return members.stream().map(member -> {
            WorkspaceJoin workspaceJoin = workspaceJoinRepository.
                    findByWorkspaceIdAndMemberUsername(workspaceId, member.getUsername())
                    .orElseThrow(()->new MemberException(ErrorStatus.MEMBER_NOT_IN_WORKSPACE));
            return CustomConverter.convertToMemberInWorkspaceDto(member,workspaceJoin.getRole(),workspaceJoin.getCreatedDate() );
        }
        ).toList();

    }

    @Override
    @Transactional(readOnly = true)
    public MemberInWorkspaceDto getMemberProfileOfWorkspace(Long workspaceId, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        WorkspaceJoin workspaceJoin = workspaceJoinRepository.
                findByWorkspaceIdAndMemberUsername(workspaceId, member.getUsername())
                .orElseThrow(()->new MemberException(ErrorStatus.MEMBER_NOT_IN_WORKSPACE));


        return CustomConverter.convertToMemberInWorkspaceDto(member,workspaceJoin.getRole(),workspaceJoin.getCreatedDate());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChannelBriefDto> getChannelsOfWorkspace(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() ->  new WorkspaceException(ErrorStatus.WORKSPACE_NOT_FOUND));

        return  channelRepository.findByWorkspace(workspace)
                .stream().map(CustomConverter::convertChannelToBriefDto).toList();
    }




    @Override
    @Transactional(readOnly = true)
    public List<WorkspaceBriefDto> getAllWorkspaceList() {
        List<Workspace> workspaceList = workspaceRepository.findAll();
        return workspaceList.stream().map(CustomConverter::convertWorkspaceToBriefDto).toList();
    }


    @Override
    public Long generateWorkspace(WorkspaceGenerateRequestDto generateRequestDto, String creatorUsername) {
        if(workspaceRepository.existsByName(generateRequestDto.getName()))
            throw new WorkspaceException(ErrorStatus.WORKSPACE_NAME_ALREADY_EXISTS);

        // 멤버 조회
        Member member = memberRepository.findByUsername(creatorUsername)
                .orElseThrow(() ->  new WorkspaceException(ErrorStatus.WORKSPACE_NOT_FOUND));

        // 워크스페이스 생성 후 초대코드와 같이 저장
        Workspace workspace = Workspace.generateWorkspace(generateRequestDto);
        String code = invitationCodeGenerator.generateInviteCode();
        workspace.changeInvitationCode(code);
        workspaceRepository.save(workspace);

        // 생성한 멤버는 곧바로 워크스페이스에 들어간다
        member.enterIntoWorkspace(workspace, ROLE_WORKSPACE_OWNER.getRole());


        // 기본 채널 announce와 talk를 생성
        Channel announce = Channel.createChannel("announce",workspace);
        Channel talk = Channel.createChannel("talk",workspace);
        channelRepository.save(announce);
        channelRepository.save(talk);

        // 이미지 업로드
        MultipartFile file = generateRequestDto.getFile();
        if(file != null && !file.isEmpty()) this.uploadProfileImage(workspace.getId(),file);

        return workspace.getId();
    }

    @Override
    public Long updateWorkspaceProfile(Long workspaceId, WorkspaceUpdateRequestDto updateRequestDto) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(()-> new WorkspaceException(ErrorStatus.WORKSPACE_NOT_FOUND));

        // 이미지 업로드 기능은 차후 구현
        // if(profile_img !=null) workspace.changeProfile_img(profile_img);
        if (updateRequestDto.getDescription() != null) {
            workspace.changeDescription(updateRequestDto.getDescription());
        }
        Workspace saved = workspaceRepository.save(workspace);

        return workspace.getId();

    }

    @Override
    public void deleteWorkspace(Long workspaceId){
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(()-> new WorkspaceException(ErrorStatus.WORKSPACE_NOT_FOUND));
        this.deleteProfileImage(workspaceId);
        //TODO remove relations
        workspaceRepository.delete(workspace);
    }


    @Override
    @Transactional(readOnly = true)
    public String getInvitationCode(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() ->  new WorkspaceException(ErrorStatus.WORKSPACE_NOT_FOUND));
        return workspace.getInvitationCode();
    }

    @Override
    public String setInvitationCode(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() ->  new WorkspaceException(ErrorStatus.WORKSPACE_NOT_FOUND));
        String newCode = invitationCodeGenerator.generateInviteCode();
        workspace.changeInvitationCode(newCode);
        return newCode;
    }


    @Override
    public void enterToWorkspace(String username, String code) {

        // code 검증
        Workspace workspace = workspaceRepository.findByInvitationCode(code)
                .orElseThrow(()-> new WorkspaceException(ErrorStatus.WORKSPACE_INVALID_INVITATION_CODE));

        // 중복 회원 대비 검증
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));
        if(workspaceJoinRepository.existsByWorkspaceIdAndMemberId(workspace.getId(), member.getId()))
            throw new MemberException(ErrorStatus.MEMBER_ALREADY_EXISTS_IN_WORKSPACE);

        // 해당 멤버를 워크스페이스에 참여
        member.enterIntoWorkspace(workspace,"ROLE_WORKSPACE_MEMBER");

    }


    @Override
    public void changeRoleOfMember(Long workspaceId, Long memberId, String role) {
        if (!role.equals(ROLE_WORKSPACE_MEMBER.getRole()) && !role.equals(ROLE_WORKSPACE_OWNER.getRole()))
            throw new WorkspaceException(ErrorStatus.WORKSPACE_INVALID_ROLE_CHANGE_REQUEST);
        WorkspaceJoin workspaceJoin = workspaceJoinRepository.findByWorkspaceIdAndMemberId(workspaceId, memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_IN_WORKSPACE));
        workspaceJoin.setRole(role);
        //구체적인 역할 조정 권한이 필요하지 않을까 싶습니다. 멤버 -> 오너 승격에 제한이 필요할 것이고, 오너 -> 멤버 권한 제한에 대한 특별 케이스도
        //존재할 것 같습니다.
    }

    @Override
    public String uploadProfileImage(Long workspaceId, MultipartFile file) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() ->  new WorkspaceException(ErrorStatus.WORKSPACE_NOT_FOUND));
        String oldProfileImgURI = workspace.getProfile_img();
        if(oldProfileImgURI != null) s3Service.deleteImage(oldProfileImgURI);

        String newProfileImgURI = s3Service.uploadImage(file);
        workspace.changeProfile_img(newProfileImgURI);
        return newProfileImgURI;
    }

    @Override
    public void deleteProfileImage(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() ->  new WorkspaceException(ErrorStatus.WORKSPACE_NOT_FOUND));
        String profileImgURI = workspace.getProfile_img();
        if(profileImgURI ==null) return;
        s3Service.deleteImage(profileImgURI);
        workspace.changeProfile_img(null);
    }

    @Override
    public void leaveWorkspace(String username, Long workspaceId) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));
        WorkspaceJoin workspaceJoin = workspaceJoinRepository.findByWorkspaceIdAndMemberId(workspaceId, member.getId())
                .orElseThrow(()-> new MemberException(ErrorStatus.MEMBER_NOT_IN_WORKSPACE));

        workspaceJoinRepository.delete(workspaceJoin);
    }

    @Override
    public void deleteMemberFromWorkspace(Long workspaceId, Long memberId) {
        WorkspaceJoin workspaceJoin = workspaceJoinRepository.findByWorkspaceIdAndMemberId(workspaceId, memberId)
                .orElseThrow(()-> new MemberException(ErrorStatus.MEMBER_NOT_IN_WORKSPACE));
        workspaceJoinRepository.delete(workspaceJoin);
    }
}
