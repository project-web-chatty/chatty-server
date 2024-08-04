package com.messenger.chatty.domain.member.dto.response;
import com.messenger.chatty.domain.workspace.dto.response.WorkspaceBriefDto;
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
