package com.messenger.chatty.dto.response.workspace;


import com.messenger.chatty.dto.response.BaseResDto;
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
