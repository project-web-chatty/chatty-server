package com.messenger.chatty.repository;

import com.messenger.chatty.entity.Channel;
import com.messenger.chatty.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChannelRepository extends JpaRepository<Channel,Long> {


    List<Channel> findByWorkspace(Workspace workspace);

    // search channel in the workspace as a channelName
    Channel findByWorkspaceAndName(Workspace workspace,String name);



    // search channels that member joins at in the specific workspace
    @Query("SELECT cj.channel FROM ChannelJoin cj WHERE cj.member.id = :memberId " +
            "AND cj.channel.workspace.id = :workspaceId")
    List<Channel> findByWorkspaceIdAndMemberId(  @Param("workspaceId") Long workspaceId,@Param("memberId") Long memberId
    );





}
