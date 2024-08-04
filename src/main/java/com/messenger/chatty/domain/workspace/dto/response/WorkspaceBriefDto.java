package com.messenger.chatty.domain.workspace.dto.response;


import com.messenger.chatty.domain.base.dto.response.BaseResDto;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@ToString
public class WorkspaceBriefDto  extends BaseResDto {
    private Long id;
    private String name;
    private String profile_img;
    private String description;
}
