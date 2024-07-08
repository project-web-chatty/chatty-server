package com.messenger.chatty.dto.response;


import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class WorkspaceResponseDto extends BaseResDto {

    private Long id;
    private String name;
    private String profile_img;
    private String description;

}
