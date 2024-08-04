package com.messenger.chatty.domain.refresh.repository;

import com.messenger.chatty.domain.refresh.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository  extends JpaRepository<RefreshToken,Long> {
    Boolean existsByUsername(String username);
    void deleteByUsername(String username);
    RefreshToken findByUsername(String username);
}
