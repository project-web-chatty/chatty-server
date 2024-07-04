package com.messenger.chatty.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private List<ChannelJoin> channelJoins = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<WorkspaceJoin> workspaceJoins = new ArrayList<>();


    // use this when you generate new member
    public static Member createMember(String username, String email, String password, String role, String name, String nickname, String introduction, String profile_img){
        return Member.builder()
                .username(username)
                .email(email)
                .password(password)
                .role(role)
                .name(name)
                .nickname(nickname)
                .introduction(introduction)
                .profile_img(profile_img)
                .build();
    }


    public void joinWorkspace(Workspace workspace){
        WorkspaceJoin workspaceJoin = new WorkspaceJoin();
        workspaceJoin.setWorkspace(workspace);
        workspaceJoin.setMember(this);
    }

    public void joinChannel(Channel channel){
        ChannelJoin channelJoin = new ChannelJoin();
        channelJoin.setChannel(channel);
        channelJoin.setMember(this);
    }


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
