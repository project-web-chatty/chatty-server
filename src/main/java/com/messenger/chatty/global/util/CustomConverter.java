package com.messenger.chatty.global.util;

import com.messenger.chatty.domain.channel.dto.response.ChannelBriefDto;
import com.messenger.chatty.domain.member.dto.response.MemberBriefDto;
import com.messenger.chatty.domain.member.dto.response.MemberInWorkspaceDto;
import com.messenger.chatty.domain.member.dto.response.MyProfileDto;
import com.messenger.chatty.domain.workspace.dto.response.MyWorkspaceDto;
import com.messenger.chatty.domain.workspace.dto.response.WorkspaceBriefDto;
import com.messenger.chatty.domain.channel.entity.Channel;
import com.messenger.chatty.domain.member.entity.Member;
import com.messenger.chatty.domain.workspace.entity.Workspace;

import java.time.LocalDateTime;
import java.util.List;


public class CustomConverter {

    // briefDto에서는 순환참조 에러 안나도록 고려해서 작성

    public static MemberBriefDto convertMemberToBriefDto(Member member) {
        return MemberBriefDto.builder().id(member.getId())
                .username(member.getUsername())
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getRole())
                .profileImg(member.getProfile_img())
                .nickname(member.getNickname())
                .introduction(member.getIntroduction())
                .createdDate(member.getCreatedDate())
                .lastModifiedDate(member.getLastModifiedDate())
                .build();
    }
    public static MemberInWorkspaceDto convertToMemberInWorkspaceDto(Member member,String role, LocalDateTime joinDate){
        return MemberInWorkspaceDto.builder().id(member.getId())
                .username(member.getUsername())
                .email(member.getEmail())
                .name(member.getName())
                .role(role)
                .profileImg(member.getProfile_img())
                .nickname(member.getNickname())
                .introduction(member.getIntroduction())
                .createdDate(member.getCreatedDate())
                .lastModifiedDate(member.getLastModifiedDate())
                .joinDate(joinDate)
                .build();
    }

    public static MyProfileDto convertMemberToDto(Member member,List<Workspace> workspaceList){
        List<WorkspaceBriefDto> workspaceBriefDtoList = workspaceList.stream()
                .map(CustomConverter::convertWorkspaceToBriefDto).toList();
        return MyProfileDto.builder().id(member.getId())
                .username(member.getUsername())
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getRole())
                .profileImg(member.getProfile_img())
                .nickname(member.getNickname())
                .introduction(member.getIntroduction())
                .createdDate(member.getCreatedDate())
                .lastModifiedDate(member.getLastModifiedDate())
                .myWorkspaces(workspaceBriefDtoList)
                .build();
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
                .profileImg(workspace.getProfile_img())
                .createdDate(workspace.getCreatedDate())
                .lastModifiedDate(workspace.getLastModifiedDate()).build();
    }
    public static MyWorkspaceDto convertToMyWorkspaceDto(Workspace workspace, String myRole){
        return MyWorkspaceDto.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .description(workspace.getDescription())
                .profileImg(workspace.getProfile_img())
                .createdDate(workspace.getCreatedDate())
                .lastModifiedDate(workspace.getLastModifiedDate())
                .myRole(myRole)
                .build();
    }

    /*public static WorkspaceResponseDto convertWorkspaceToDto(Workspace workspace, List<Channel> channelList, List<Member> memberList ) {

        List<ChannelBriefDto> channelBriefList = channelList.stream().map(CustomConverter::convertChannelToBriefDto).toList();
        List<MemberBriefDto> memberBriefLst = memberList.stream().map(CustomConverter::convertMemberToBriefDto).toList();

        return WorkspaceResponseDto.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .description(workspace.getDescription())
                .profile_img(workspace.getProfile_img())
                .createdDate(workspace.getCreatedDate())
                .lastModifiedDate(workspace.getLastModifiedDate())
                .channels(channelBriefList)
                .members(memberBriefLst)
                .build();
    }
*/




}
