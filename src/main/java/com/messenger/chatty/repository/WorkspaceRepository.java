package com.messenger.chatty.repository;

import com.messenger.chatty.entity.Member;
import com.messenger.chatty.entity.Workspace;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WorkspaceRepository extends JpaRepository<Workspace,Long> {

    //search workspace by name
    Optional<Workspace> findByName(String name);

    boolean existsByName(String name);

    @NonNull List<Workspace> findAll();

    // search workspaces member joins at
    @Query("SELECT wj.workspace FROM WorkspaceJoin wj WHERE wj.member.id = :memberId")
    List<Workspace> findWorkspacesByMemberId(@Param("memberId") Long memberId);




}
