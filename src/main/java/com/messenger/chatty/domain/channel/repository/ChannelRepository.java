package com.messenger.chatty.domain.channel.repository;

import com.messenger.chatty.domain.channel.entity.Channel;
import com.messenger.chatty.domain.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel,Long> {


    List<Channel> findByWorkspace(Workspace workspace);

    // search channel in the specific workspace as a channelName
    Optional<Channel> findByWorkspaceAndName(Workspace workspace, String name);


    boolean existsByName(String name);

}
