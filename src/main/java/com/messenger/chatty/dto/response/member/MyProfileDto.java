package com.messenger.chatty.dto.response.member;
import com.messenger.chatty.dto.response.workspace.WorkspaceBriefDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;


// my member profile response is different from basic member res
// other members must not know about my workspaces
@Getter
@Setter
@SuperBuilder
@ToString
public class MyProfileDto extends MemberBriefDto {
    protected List<WorkspaceBriefDto> myWorkspaces;


}
