package com.messenger.chatty.repository;

import com.messenger.chatty.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Member findByUsername(String username);
    Member findByEmail(String email);

    Member findByNickname(String nickname);

    Member findByName(String name);

}
