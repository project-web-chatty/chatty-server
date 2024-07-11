package com.messenger.chatty.test;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamPerson> teamPersons = new ArrayList<>();

    // Getters and Setters
    public void addTeamPerson(TeamPerson teamPerson) {
        teamPersons.add(teamPerson);
        teamPerson.setPerson(this);
    }

    public void removeTeamPerson(TeamPerson teamPerson) {
        teamPersons.remove(teamPerson);
        teamPerson.setPerson(null);
    }
}