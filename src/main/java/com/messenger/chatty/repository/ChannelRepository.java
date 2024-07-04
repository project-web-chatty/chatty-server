package com.messenger.chatty.repository;

import com.messenger.chatty.entity.Channel;
import com.messenger.chatty.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel,Long> {

    Channel findByName(String name);
    Channel findByWorkspace(Workspace workspace);

}
