package com.messenger.chatty.util;

import com.messenger.chatty.dto.response.channel.ChannelBriefDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
import com.messenger.chatty.dto.response.member.MyProfileDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceBriefDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceResponseDto;
import com.messenger.chatty.entity.Channel;
import com.messenger.chatty.entity.Member;
import com.messenger.chatty.entity.Workspace;

import java.util.List;
import java.util.stream.Stream;

public class CustomConverter {

    // briefDto에서는 순환참조 에러 안나도록 고려해서 작성

    public static MemberBriefDto convertMemberToBriefDto(Member member) {
        return MemberBriefDto.builder().id(member.getId())
                .username(member.getUsername())
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getRole())
                .profile_img(member.getProfile_img())
                .nickname(member.getNickname())
                .introduction(member.getIntroduction())
                .createdDate(member.getCreatedDate())
                .lastModifiedDate(member.getLastModifiedDate())
                .build();
    }
    public static MyProfileDto convertMemberToDto(Member member,List<Workspace> workspaceList){
        MyProfileDto myProfileDto = (MyProfileDto) convertMemberToBriefDto(member);
        List<WorkspaceBriefDto> workspaceBriefDtoList = workspaceList.stream().map(CustomConverter::convertWorkspaceToBriefDto).toList();
        myProfileDto.setMyWorkspaces(workspaceBriefDtoList);
        return myProfileDto;
    }

    public static ChannelBriefDto convertChannelToBriefDto(Channel channel){
        return ChannelBriefDto.builder()
                .id(channel.getId())
                .name(channel.getName())
                .lastModifiedDate(channel.getLastModifiedDate())
                .createdDate(channel.getCreatedDate())
                .build();
    }
    public static WorkspaceBriefDto convertWorkspaceToBriefDto(Workspace workspace){
        return WorkspaceBriefDto.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .description(workspace.getDescription())
                .profile_img(workspace.getProfile_img())
                .createdDate(workspace.getCreatedDate())
                .lastModifiedDate(workspace.getLastModifiedDate()).build();
    }

    public static WorkspaceResponseDto convertWorkspaceToDto(Workspace workspace, List<Channel> channelList, List<Member> memberList ) {
        WorkspaceResponseDto workspaceResponseDto = (WorkspaceResponseDto) convertWorkspaceToBriefDto(workspace);
        List<ChannelBriefDto> channelBriefList = channelList.stream().map(CustomConverter::convertChannelToBriefDto).toList();
        List<MemberBriefDto> memberBriefLst = memberList.stream().map(CustomConverter::convertMemberToBriefDto).toList();
        workspaceResponseDto.setChannels(channelBriefList);
        workspaceResponseDto.setMembers(memberBriefLst);
        return workspaceResponseDto;
    }



}
