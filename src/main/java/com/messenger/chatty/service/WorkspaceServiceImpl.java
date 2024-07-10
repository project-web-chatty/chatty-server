package com.messenger.chatty.service;


import com.messenger.chatty.dto.request.WorkspaceGenerateRequestDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceResponseDto;
import com.messenger.chatty.entity.Channel;
import com.messenger.chatty.entity.Member;
import com.messenger.chatty.entity.Workspace;
import com.messenger.chatty.exception.custom.DuplicatedNameException;
import com.messenger.chatty.repository.ChannelRepository;
import com.messenger.chatty.repository.MemberRepository;
import com.messenger.chatty.repository.WorkspaceRepository;
import com.messenger.chatty.util.CustomConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService{

    private final MemberRepository memberRepository;
    private final WorkspaceRepository workspaceRepository;
    private final ChannelRepository channelRepository;
    // private final WorkspaceJoinRepository workspaceJoinRepository ;

    @Override
    public WorkspaceResponseDto getWorkspaceProfile(String workspaceName) {
        // 앞의 필터에서 인가 권한에 대하여 검증되었다고 가정하므로 여기서 인가 validation 할 필요 x

        Workspace workspace = workspaceRepository.findByName(workspaceName)
                .orElseThrow(() -> new NoSuchElementException("there is no workspace which name is " + workspaceName));

        List<Channel> channels = channelRepository.findByWorkspace(workspace);
        List<Member> members = memberRepository.findMembersByWorkspaceId(workspace.getId());




        // return null;
    }






    // 워크스페이스 내에서 수행되는 멤버쉽과 관련된 인가 권한 검증은 시큐리티의 커스텀 필터를 URL에 맞게 구현
    // 필터에서 이 메서드를 호출하여 검증 수행
    // (비효율적인 것 같으면 서비스 단에서 검증하기)
    private void validateAuthorization(){
        //  추후 로직 작성
        //
        // throw new UnAuthorizedMemberException("incomplete");
    }



    public List<WorkspaceResponseDto> getAllWorkspaceList() {
        List<Workspace> workspaceList = workspaceRepository.findAll();
        return workspaceList.stream().map(CustomConverter::convertWorkspaceToDto).toList();
    };

    @Override
    public WorkspaceResponseDto generateWorkspace(WorkspaceGenerateRequestDto generateRequestDto, String username) {
        if(workspaceRepository.existsByName(generateRequestDto.getName()))
            throw new DuplicatedNameException("duplicated workspaceName : "+ generateRequestDto.getName());

        // 멤버
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("there is no member whose username is " + username));
        Workspace workspace = Workspace.generateWorkspace(generateRequestDto);
        workspaceRepository.save(workspace); // 워크스페이스 저장

        member.enterIntoWorkspace(workspace);
        // 채널 등록
        Channel defaultChannel = Channel.createChannel("announce",workspace);
        channelRepository.save(defaultChannel);
        member.enterIntoChannel(defaultChannel);
        return CustomConverter.convertWorkspaceToDto(workspace);
    }


}
