package com.messenger.chatty.repository;

import com.messenger.chatty.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<Workspace,Long> {
    Workspace findByName(String name);
}
