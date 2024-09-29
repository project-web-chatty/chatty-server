package com.messenger.chatty.domain.channel.repository;

import com.messenger.chatty.domain.channel.entity.ChannelAccess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChannelAccessRepository extends JpaRepository<ChannelAccess, Long> {
    Optional<ChannelAccess> findChannelAccessByChannel_IdAndWorkspaceJoinId(Long channelId, Long workspaceJoinId);

    boolean existsByChannelIdAndWorkspaceJoinId(Long channelId, Long workspaceJoinId);
}
