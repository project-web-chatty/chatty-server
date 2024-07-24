package com.messenger.chatty.repository;

import com.messenger.chatty.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository  extends JpaRepository<RefreshToken,Long> {
    Boolean existsByUsername(String username);
    void deleteByUsername(String username);
    RefreshToken findByUsername(String username);
}
