package com.messenger.chatty.repository;

import com.messenger.chatty.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository  extends JpaRepository<TokenEntity,Long> {
}
