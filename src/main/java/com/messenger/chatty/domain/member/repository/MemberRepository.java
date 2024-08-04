package com.messenger.chatty.domain.member.repository;

import com.messenger.chatty.domain.member.entity.Member;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByUsername(String username);
    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByName(String name);

    boolean existsByUsername(String username);

    @NonNull List<Member> findAll();

    void deleteByUsername(String username);

    @Query("SELECT wj.member FROM WorkspaceJoin wj WHERE wj.workspace.id = :workspaceId")
    List<Member> findMembersByWorkspaceId(@Param("workspaceId") Long workspaceId);

//    @Query("SELECT cj.member FROM ChannelJoin cj WHERE cj.channel.id = :channelId")
//    List<Member> findByChannelId(@Param("channelId") Long channelId);

}
