package com.messenger.chatty.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "channels")
public class Channel extends BaseEntity{

    @Id
    @Column(name = "channel_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = false) // but must be unique in the same workspace
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;



    // use this when you generate new channel
    public static Channel createChannel(String name, Workspace workspace){
        Channel channel = Channel.builder().name(name).build();
        workspace.addChannel(channel);
        return channel;
    }

    protected void linkWorkspace(Workspace workspace){
       this.workspace = workspace;
    }




    public void changeName(String name){
        this.name = name;
    }
}
