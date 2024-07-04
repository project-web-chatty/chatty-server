package com.messenger.chatty.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder  //object should be generated by builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String email;


    @Column(nullable = false)
    private String password;

    private String role;

    private String profile_img;

    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String introduction;

    @Builder.Default
    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ChannelMember> channelMembers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<WorkspaceMember> workspaceMembers = new ArrayList<>();


    public void changeEmail(String email){
        this.email = email;
    }
    public void changeProfileImg(String profile_img){
        this.profile_img = profile_img;
    }
    public void changeName(String name){
        this.name = name;
    }
    public void changeNickname(String nickname){
        this.nickname = nickname;
    }
    public void changePassword(String password){
        this.password = password;
    }
    public void changeIntroduction(String introduction){
        this.introduction = introduction;
    }


}
