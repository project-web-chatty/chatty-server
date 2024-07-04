package com.messenger.chatty.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "workspace_joins")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkspaceJoin extends BaseEntity{

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

    //relation-method
    protected void setMember(Member member){
        this.member = member;
        member.getWorkspaceJoins().add(this);
    }

    //relation-method
    protected void setWorkspace(Workspace workspace){
        this.workspace =workspace;
        workspace.getWorkspaceJoins().add(this);
    }


}
