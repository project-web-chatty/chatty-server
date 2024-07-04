package com.messenger.chatty.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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
    public static Workspace createWorkspace(String name, String profile_img,String description){
        return Workspace.builder().name(name)
                .profile_img(profile_img)
                .description(description)
                .build();

    }


    //relation-method
    protected void addChannel(Channel channel){
        channels.add(channel);
        channel.setWorkspace(this);
    }

    public List<Member> getAllMembers(){
        return workspaceJoins.stream().map(WorkspaceJoin::getMember).collect(Collectors.toList());
    }


}
