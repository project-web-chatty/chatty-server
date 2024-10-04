package com.messenger.chatty.domain.workspace.entity;


import com.messenger.chatty.domain.base.entity.BaseEntity;
import com.messenger.chatty.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@Table(name = "workspace_joins")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class  WorkspaceJoin extends BaseEntity {

    @Id
    @Column(name = "workspace_join_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    private String role;

    //relation-method
    public void linkMember(Member member){
        this.member = member;
        member.getWorkspaceJoins().add(this);
    }

    //relation-method
    public void linkWorkspace(Workspace workspace){
        this.workspace = workspace;
        workspace.getWorkspaceJoins().add(this);
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role){
        this.role =role;
    }
}
