package com.messenger.chatty.repository;

import com.messenger.chatty.entity.WorkspaceJoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkspaceJoinRepository extends JpaRepository<WorkspaceJoin, Long> {
    boolean existsByWorkspaceIdAndMemberId(Long workspaceId, Long memberId);
    Optional<WorkspaceJoin> findByWorkspaceIdAndMemberUsername(Long workspaceId, String username);
    Optional<WorkspaceJoin> findByWorkspaceIdAndMemberId(Long workspaceId, Long memberId);

}


