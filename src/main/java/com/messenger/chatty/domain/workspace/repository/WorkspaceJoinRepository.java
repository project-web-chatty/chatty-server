package com.messenger.chatty.domain.workspace.repository;
import com.messenger.chatty.domain.member.entity.Member;
import com.messenger.chatty.domain.workspace.entity.WorkspaceJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WorkspaceJoinRepository extends JpaRepository<WorkspaceJoin, Long> {
    boolean existsByWorkspaceIdAndMemberId(Long workspaceId, Long memberId);
    Optional<WorkspaceJoin> findByWorkspaceIdAndMemberUsername(Long workspaceId, String username);
    Optional<WorkspaceJoin> findByWorkspaceIdAndMemberId(Long workspaceId, Long memberId);

    List<WorkspaceJoin> findByMemberId(Long memberId);

    @Query("SELECT wj.member FROM WorkspaceJoin wj WHERE wj.workspace.id = :workspaceId")
    List<Member> findMembersByWorkspaceId(@Param("workspaceId") Long workspaceId);

}


