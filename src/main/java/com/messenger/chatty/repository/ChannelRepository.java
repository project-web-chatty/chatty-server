package com.messenger.chatty.repository;

import com.messenger.chatty.entity.Channel;
import com.messenger.chatty.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel,Long> {


    List<Channel> findByWorkspace(Workspace workspace);

    // search channel in the specific workspace as a channelName
    Optional<Channel> findByWorkspaceAndName(Workspace workspace, String name);


    boolean existsByName(String name);

}
