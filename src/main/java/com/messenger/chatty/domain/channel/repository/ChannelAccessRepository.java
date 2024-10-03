package com.messenger.chatty.domain.channel.repository;

import com.messenger.chatty.domain.channel.entity.ChannelAccess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChannelAccessRepository extends JpaRepository<ChannelAccess, Long> {
    Optional<ChannelAccess> findChannelAccessByChannel_IdAndUsername(Long channelId, String username);

    boolean existsByChannelIdAndUsername(Long channelId, String username);
}
