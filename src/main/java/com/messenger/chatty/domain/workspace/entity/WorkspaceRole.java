package com.messenger.chatty.domain.workspace.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WorkspaceRole {
    //사실 이렇게 한 다음 굳이 필드에 role을 넣지 않고 엔티티에 enum->string하는 과정을 넣어도 되는데 일단 이해에 편하도록 이렇게 해뒀습니다.
    ROLE_WORKSPACE_MEMBER("ROLE_WORKSPACE_MEMBER"), ROLE_WORKSPACE_OWNER("ROLE_WORKSPACE_OWNER");
    private String role;
}
