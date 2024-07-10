package com.messenger.chatty.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class WorkspaceGenerateRequestDto {
    @NotBlank
    private String name;
    private String profile_img;
    private String description;
}
