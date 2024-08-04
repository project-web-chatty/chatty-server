package com.messenger.chatty.domain.workspace.entity;


import com.messenger.chatty.domain.base.entity.BaseEntity;
import com.messenger.chatty.domain.workspace.dto.request.WorkspaceGenerateRequestDto;
import com.messenger.chatty.domain.channel.entity.Channel;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Workspace extends BaseEntity {
    @Id
    @Column(name = "workspace_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String name;

    private String profile_img;
    private String description;

    private String invitationCode;

    @Builder.Default
    // @Column(nullable = false)
    @OneToMany(mappedBy = "workspace",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<WorkspaceJoin> workspaceJoins = new ArrayList<>();


    @Builder.Default
    @OneToMany(mappedBy = "workspace",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Channel> channels = new ArrayList<>();


    // use this when you generate new workspace
    public static Workspace generateWorkspace(WorkspaceGenerateRequestDto generateRequestDto){
        return Workspace.builder().name(generateRequestDto.getName())
                .description(generateRequestDto.getDescription())
                .build();

    }


    //relation-method
    public void addChannel(Channel channel){
        channels.add(channel);
        channel.linkWorkspace(this);
    }

    public void changeDescription(String description){
        this.description = description;
    }
    public void changeProfile_img(String profile_img){
        this.profile_img = profile_img;
    }
    public void changeInvitationCode(String code){this.invitationCode = code;}


}
