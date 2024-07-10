package com.messenger.chatty.entity;


import com.messenger.chatty.dto.request.WorkspaceGenerateRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Workspace extends BaseEntity{
    @Id
    @Column(name = "workspace_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String name;

    private String profile_img;
    private String description;


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
                .profile_img(generateRequestDto.getProfile_img())
                .description(generateRequestDto.getDescription())
                .build();

    }


    //relation-method
    protected void addChannel(Channel channel){
        channels.add(channel);
        channel.linkWorkspace(this);
    }



}
