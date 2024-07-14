package com.messenger.chatty.repository;

import com.messenger.chatty.entity.WorkspaceJoin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceJoinRepository extends JpaRepository<WorkspaceJoin, Long> {
    boolean existsByWorkspaceIdAndMemberId(Long workspaceId, Long memberId);
    WorkspaceJoin findByWorkspaceIdAndMemberUsername(Long workspaceId, String username);

}


