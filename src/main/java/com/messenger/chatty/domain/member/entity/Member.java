package com.messenger.chatty.domain.member.entity;
import com.messenger.chatty.domain.base.entity.BaseEntity;
import com.messenger.chatty.domain.member.dto.request.MemberJoinRequestDto;
import com.messenger.chatty.domain.member.dto.request.MemberUpdateRequestDto;
import com.messenger.chatty.domain.workspace.entity.Workspace;
import com.messenger.chatty.domain.workspace.entity.WorkspaceJoin;
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

    // @Column(nullable = false)
    private String email;


    @Column(nullable = false)
    private String password;

    private String role;

    private String profile_img;

    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private String nickname;

    @Column // (nullable = false)
    private String introduction;



    @Builder.Default
    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<WorkspaceJoin> workspaceJoins = new ArrayList<>();


    // use this when you generate new member
    public static Member from(MemberJoinRequestDto memberJoinRequestDTO){
        String username = memberJoinRequestDTO.getUsername();
        return Member.builder()
                .username(username)
                .password(memberJoinRequestDTO.getPassword())
                .role("ROLE_USER")
                .name(username)
                .nickname(username)
                .introduction(username +" 님의 소개글입니다.")
                .build();
    }

    public void enterIntoWorkspace(Workspace workspace, String role){
        WorkspaceJoin workspaceJoin = new WorkspaceJoin();
        workspaceJoin.setRole(role);
        workspaceJoin.linkWorkspace(workspace);
        workspaceJoin.linkMember(this);
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

    public void updateProfile(MemberUpdateRequestDto memberUpdateRequestDto) {
        this.name = memberUpdateRequestDto.getName();
        this.nickname = memberUpdateRequestDto.getNickname();
        this.introduction = memberUpdateRequestDto.getIntroduction();
    }

}