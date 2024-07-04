package com.messenger.chatty.repository;

import com.messenger.chatty.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Member findByUsername(String username);
    Member findByEmail(String email);

    Member findByNickname(String nickname);

    Member findByName(String name);

    @Query("SELECT wj.member FROM WorkspaceJoin wj WHERE wj.workspace.id = :workspaceId")
    List<Member> findMembersByWorkspaceId(@Param("workspaceId") Long workspaceId);

    @Query("SELECT cj.member FROM ChannelJoin cj WHERE cj.channel.id = :channelId")
    List<Member> findByChannelId(@Param("channelId") Long channelId);

}
