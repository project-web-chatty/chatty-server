package com.messenger.chatty.domain.workspace.dto.request;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WorkspaceUpdateRequestDto {
    private String description;
}
