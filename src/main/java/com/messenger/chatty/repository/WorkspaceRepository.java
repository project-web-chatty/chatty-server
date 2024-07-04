package com.messenger.chatty.repository;

import com.messenger.chatty.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkspaceRepository extends JpaRepository<Workspace,Long> {

    //search workspace by name
    Workspace findByName(String name);

    // search workspaces member joins at
    @Query("SELECT wj.workspace FROM WorkspaceJoin wj WHERE wj.member.id = :memberId")
    List<Workspace> findWorkspacesByMemberId(@Param("memberId") Long memberId);

}
