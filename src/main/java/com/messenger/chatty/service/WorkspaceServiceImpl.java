package com.messenger.chatty.service;
import com.messenger.chatty.dto.request.WorkspaceGenerateRequestDto;
import com.messenger.chatty.dto.request.WorkspaceUpdateRequestDto;
import com.messenger.chatty.dto.response.channel.ChannelBriefDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceBriefDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceResponseDto;
import com.messenger.chatty.entity.Channel;
import com.messenger.chatty.entity.ChannelJoin;
import com.messenger.chatty.entity.Member;
import com.messenger.chatty.entity.Workspace;
import com.messenger.chatty.exception.custom.CustomNoSuchElementException;
import com.messenger.chatty.exception.custom.DuplicatedNameException;
import com.messenger.chatty.exception.custom.InvalidInvitationCodeException;
import com.messenger.chatty.exception.custom.UnAuthorizedMemberException;
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
    private final ChannelJoinRepository channelJoinRepository;
    private final InvitationCodeGenerator invitationCodeGenerator;
    private final WorkspaceJoinRepository workspaceJoinRepository;


    @Override
    @Transactional(readOnly = true)
    public WorkspaceResponseDto getWorkspaceProfile(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomNoSuchElementException("id",workspaceId,"워크스페이스"));

        List<Channel> channels = channelRepository.findByWorkspace(workspace);
        List<Member> members = memberRepository.findMembersByWorkspaceId(workspace.getId());

        return  CustomConverter.convertWorkspaceToDto(workspace,channels,members);

    }

    @Override
    @Transactional(readOnly = true)
    public WorkspaceBriefDto getWorkspaceBriefProfile(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomNoSuchElementException("id",workspaceId,"워크스페이스"));
        return  CustomConverter.convertWorkspaceToBriefDto(workspace);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberBriefDto> getMembersOfWorkspace(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomNoSuchElementException("id",workspaceId,"워크스페이스"));

        List<Member> members = memberRepository.findMembersByWorkspaceId(workspace.getId());
        return members.stream().map(CustomConverter::convertMemberToBriefDto).toList();

    }
    @Override
    @Transactional(readOnly = true)
    public List<ChannelBriefDto> getChannelsOfWorkspace(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomNoSuchElementException("id",workspaceId,"워크스페이스"));

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
    public WorkspaceBriefDto generateWorkspace(WorkspaceGenerateRequestDto generateRequestDto, String creatorUsername) {
        if(workspaceRepository.existsByName(generateRequestDto.getName()))
            throw new DuplicatedNameException(generateRequestDto.getName(),"워크스페이스 이름");

        // 멤버 조회
        Member member = memberRepository.findByUsername(creatorUsername)
                .orElseThrow(() -> new CustomNoSuchElementException("username",creatorUsername,"회원"));

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
        ChannelJoin announceJoin = ChannelJoin.from(announce,member);
      //  channelJoinRepository.save(announceJoin);
        ChannelJoin talkJoin = ChannelJoin.from(talk,member);
      //  channelJoinRepository.save(talkJoin);


        return CustomConverter.convertWorkspaceToBriefDto(workspace);
    }

    @Override
    public WorkspaceBriefDto updateWorkspaceProfile(Long workspaceId, WorkspaceUpdateRequestDto updateRequestDto) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(()->new CustomNoSuchElementException("id" , workspaceId,"워크스페이스"));

        // 이미지 업로드 기능은 차후 구현
        // if(profile_img !=null) workspace.changeProfile_img(profile_img);
        if(updateRequestDto.getDescription() !=null)
            workspace.changeDescription(updateRequestDto.getDescription());

        Workspace saved = workspaceRepository.save(workspace);

        return CustomConverter.convertWorkspaceToBriefDto(saved);

    }

    @Override
    public void deleteWorkspace(Long workspaceId){
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(()->new CustomNoSuchElementException("id" , workspaceId,"워크스페이스"));
        workspaceRepository.delete(workspace);
    }

    @Override
    @Transactional(readOnly = true)
    public String getNewInvitationCode(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomNoSuchElementException("아이디", workspaceId, "워크스페이스"));
        return workspace.getInvitationCode();
    }

    @Override
    public String setInvitationCode(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomNoSuchElementException("아이디", workspaceId, "워크스페이스"));
        String newCode = invitationCodeGenerator.generateInviteCode();
        workspace.changeInvitationCode(newCode);
        return newCode;
    }


    @Override
    public WorkspaceResponseDto enterToWorkspace(String username, String code) {

        // code 검증
        Workspace workspace = workspaceRepository.findByInvitationCode(code)
                .orElseThrow(()-> new InvalidInvitationCodeException("유효하지 않은 코드입니다."));

        // 중복 회원 대비 검증
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNoSuchElementException("username",username,"회원"));
        if(workspaceJoinRepository.existsByWorkspaceIdAndMemberId(workspace.getId(), member.getId()))
            throw new DuplicatedNameException("이미 이 워크스페이스 내에 존재하는 회원입니다.");



        // 해당 멤버를 워크스페이스에 참여
        member.enterIntoWorkspace(workspace,"ROLE_WORKSPACE_MEMBER");
        // channel_join table은 삭제될 예정. 이후 리팩터링하기
        List<Channel> channels = channelRepository.findByWorkspace(workspace);
        channels.forEach((channel)->{
            ChannelJoin channelJoin = ChannelJoin.from(channel,member);
            channelJoinRepository.save(channelJoin);
        });


        Workspace savedWorkspace = workspaceRepository.save(workspace);
        List<Member> members = memberRepository.findMembersByWorkspaceId(workspace.getId());
        return CustomConverter.convertWorkspaceToDto(savedWorkspace,channels,members);

    }





}
