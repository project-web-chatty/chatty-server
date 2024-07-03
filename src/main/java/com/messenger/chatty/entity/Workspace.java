package com.messenger.chatty.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Workspace extends BaseEntity{
    @Id
    @Column(name = "workspace_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String name;

    private String profile_img;
    private String description;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member creator;

}
