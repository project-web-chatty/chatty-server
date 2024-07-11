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
import com.messenger.chatty.exception.custom.UnAuthorizedMemberException;
import com.messenger.chatty.repository.ChannelJoinRepository;
import com.messenger.chatty.repository.ChannelRepository;
import com.messenger.chatty.repository.MemberRepository;
import com.messenger.chatty.repository.WorkspaceRepository;
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

    // 워크스페이스 내에서 수행되는 멤버쉽과 관련된 인가 권한 검증은 시큐리티의 커스텀 필터를 URL에 맞게 구현
    // 특정 멤버가 특정 워크스페이스에 대한 해당 메서드에 대해 권한이 있는가 에 대한 validation
    // 필터에서 이 메서드를 호출하여 검증 수행
    public void validateAuthorization(){
        //  추후 로직 작성
         throw new UnAuthorizedMemberException("incomplete");
    }


    @Override
    public WorkspaceResponseDto getWorkspaceProfile(Long workspaceId) {
        // 앞의 필터에서 인가 권한에 대하여 검증되었다고 가정하므로 여기서 인가 validation 할 필요 x
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomNoSuchElementException("id",workspaceId,"워크스페이스"));

        List<Channel> channels = channelRepository.findByWorkspace(workspace);
        List<Member> members = memberRepository.findMembersByWorkspaceId(workspace.getId());

        return  CustomConverter.convertWorkspaceToDto(workspace,channels,members);

    }

    @Override
    public WorkspaceBriefDto getWorkspaceBriefProfile(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomNoSuchElementException("id",workspaceId,"워크스페이스"));
        return  CustomConverter.convertWorkspaceToBriefDto(workspace);
    }

    @Override
    public List<MemberBriefDto> getMembersOfWorkspace(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomNoSuchElementException("id",workspaceId,"워크스페이스"));

        List<Member> members = memberRepository.findMembersByWorkspaceId(workspace.getId());
        return members.stream().map(CustomConverter::convertMemberToBriefDto).toList();

    }
    @Override
    public List<ChannelBriefDto> getChannelsOfWorkspace(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomNoSuchElementException("id",workspaceId,"워크스페이스"));

        return  channelRepository.findByWorkspace(workspace)
                .stream().map(CustomConverter::convertChannelToBriefDto).toList();
    }


    @Override
    public void enterIntoWorkspace(Long workspaceId, String targetUsername) {
        // 멤버를 추가 시, 워크 스페이스 내의 모든 채널에 해당 멤버가 속해야 함
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomNoSuchElementException("id",workspaceId,"워크스페이스"));
        List<Channel> channels = channelRepository.findByWorkspace(workspace);

        Member member = memberRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new CustomNoSuchElementException("username",targetUsername,"회원"));

        List<Long> memberIdListOfWorkspace = memberRepository.
                findMembersByWorkspaceId(workspace.getId()).stream().map(Member::getId).toList();

        if(memberIdListOfWorkspace.contains(member.getId()))
            throw new DuplicatedNameException("이미 존재하는 회원입니다.");

        member.enterIntoWorkspace(workspace);

        channels.forEach((channel)->{
            ChannelJoin channelJoin = new ChannelJoin(channel,member);
            channelJoinRepository.save(channelJoin);
        });



    }


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

        // 워크스페이스 생성 후 저장
        Workspace workspace = Workspace.generateWorkspace(generateRequestDto);
        workspaceRepository.save(workspace);

        member.enterIntoWorkspace(workspace);
        Channel announce = Channel.createChannel("announce",workspace);
        Channel talk = Channel.createChannel("talk",workspace);

        channelRepository.save(announce);
        channelRepository.save(talk);

        ChannelJoin channelJoin1 = new ChannelJoin(announce,member);
        channelJoinRepository.save(channelJoin1);

        ChannelJoin channelJoin2 = new ChannelJoin(talk,member);
        channelJoinRepository.save(channelJoin2);


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


}
