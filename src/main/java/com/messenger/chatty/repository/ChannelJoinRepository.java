package com.messenger.chatty.repository;

import com.messenger.chatty.entity.ChannelJoin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelJoinRepository extends JpaRepository<ChannelJoin,Long> {
}
