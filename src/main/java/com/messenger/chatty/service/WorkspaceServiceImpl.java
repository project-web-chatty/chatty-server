package com.messenger.chatty.service;
import com.messenger.chatty.dto.request.WorkspaceGenerateRequestDto;
import com.messenger.chatty.dto.request.WorkspaceUpdateRequestDto;
import com.messenger.chatty.dto.response.channel.ChannelBriefDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceBriefDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceResponseDto;
import com.messenger.chatty.entity.*;
import com.messenger.chatty.presentation.ErrorStatus;
import com.messenger.chatty.presentation.exception.custom.MemberException;
import com.messenger.chatty.presentation.exception.custom.WorkspaceException;
import com.messenger.chatty.repository.*;
import com.messenger.chatty.util.InvitationCodeGenerator;
import com.messenger.chatty.util.CustomConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService{

    private final MemberRepository memberRepository;
    private final WorkspaceRepository workspaceRepository;
    private final ChannelRepository channelRepository;
    private final InvitationCodeGenerator invitationCodeGenerator;
    private final WorkspaceJoinRepository workspaceJoinRepository;



    @Override
    @Transactional(readOnly = true)
    public WorkspaceResponseDto getWorkspaceProfile(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new WorkspaceException(ErrorStatus.WORKSPACE_NOT_FOUND));

        List<Channel> channels = channelRepository.findByWorkspace(workspace);
        List<Member> members = memberRepository.findMembersByWorkspaceId(workspace.getId());

        return  CustomConverter.convertWorkspaceToDto(workspace,channels,members);

    }

    @Override
    @Transactional(readOnly = true)
    public WorkspaceBriefDto getWorkspaceBriefProfile(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() ->  new WorkspaceException(ErrorStatus.WORKSPACE_NOT_FOUND));
        return  CustomConverter.convertWorkspaceToBriefDto(workspace);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberBriefDto> getMembersOfWorkspace(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() ->  new WorkspaceException(ErrorStatus.WORKSPACE_NOT_FOUND));

        List<Member> members = memberRepository.findMembersByWorkspaceId(workspace.getId());
        return members.stream().map(CustomConverter::convertMemberToBriefDto).toList();

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
        member.enterIntoWorkspace(workspace,"ROLE_WORKSPACE_OWNER");


        // 기본 채널 announce와 talk를 생성
        Channel announce = Channel.createChannel("announce",workspace);
        Channel talk = Channel.createChannel("talk",workspace);
        channelRepository.save(announce);
        channelRepository.save(talk);


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
        workspaceRepository.delete(workspace);
    }


    @Override
    @Transactional(readOnly = true)
    public String getNewInvitationCode(Long workspaceId) {
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
    public WorkspaceResponseDto enterToWorkspace(String username, String code) {

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

        List<Member> members = memberRepository.findMembersByWorkspaceId(workspace.getId());
        return CustomConverter.convertWorkspaceToDto(workspace, workspace.getChannels(), members);
    }


    @Override
    public void changeRoleOfMember(Long workspaceId,Long memberId,String role){
        if(!role.equals("ROLE_WORKSPACE_MEMBER") && !role.equals("ROLE_WORKSPACE_OWNER"))
            throw new WorkspaceException(ErrorStatus.WORKSPACE_INVALID_ROLE_CHANGE_REQUEST);
        WorkspaceJoin workspaceJoin = workspaceJoinRepository.findByWorkspaceIdAndMemberId(workspaceId, memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_IN_WORKSPACE));
        workspaceJoin.setRole(role);
    }

}
