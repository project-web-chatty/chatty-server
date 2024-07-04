package com.messenger.chatty.repository;

import com.messenger.chatty.entity.Channel;
import com.messenger.chatty.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelRepository extends JpaRepository<Channel,Long> {


    List<Channel> findByWorkspace(Workspace workspace);

    Channel findByWorkspaceAndName(Workspace workspace,String name);
    // name should be unique in the same workspace

}
