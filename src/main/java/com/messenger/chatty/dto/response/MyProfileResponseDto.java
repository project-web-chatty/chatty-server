package com.messenger.chatty.dto.response;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;


// my member profile response is different from basic member res
// other members must not know about my workspaces
@Getter
@Setter
@SuperBuilder
public class MyProfileResponseDto extends MemberProfileResponseDto {
    private List<WorkspaceResponseDto> myWorkspaces;

}
