package com.messenger.chatty.util;

import com.messenger.chatty.dto.response.MemberProfileResponseDto;
import com.messenger.chatty.dto.response.WorkspaceResponseDto;
import com.messenger.chatty.entity.Member;
import com.messenger.chatty.entity.Workspace;

public class DtoConverter {

    public static MemberProfileResponseDto convertMemberToDto(Member member){
        return MemberProfileResponseDto.builder().id(member.getId())
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
    public static WorkspaceResponseDto convertWorkspaceToDto(Workspace workspace){
        return WorkspaceResponseDto.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .description(workspace.getDescription())
                .profile_img(workspace.getProfile_img())
                .createdDate(workspace.getCreatedDate())
                .lastModifiedDate(workspace.getLastModifiedDate()).build();
    }
}
