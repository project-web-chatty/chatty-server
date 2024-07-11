package com.messenger.chatty.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class WorkspaceGenerateRequestDto {
    @NotBlank
    private String name;
    private String profile_img;
    private String description;
}
