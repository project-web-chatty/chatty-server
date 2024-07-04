package com.messenger.chatty.repository;

import com.messenger.chatty.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
