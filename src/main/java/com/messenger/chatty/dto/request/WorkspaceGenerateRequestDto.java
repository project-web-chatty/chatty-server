package com.messenger.chatty.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class WorkspaceGenerateRequestDto {
    @NotBlank
    private String name;
    private String description;
}
