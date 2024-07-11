package com.messenger.chatty.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TeamPersonRepositroy teamPersonRepository;

    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }

    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

    public Team addMemberToTeam(Long teamId, Long personId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found"));
        Person person = personRepository.findById(personId).orElseThrow(() -> new RuntimeException("Person not found"));

        TeamPerson teamPerson = new TeamPerson();
        teamPerson.setTeam(team);
        teamPerson.setPerson(person);

        teamPersonRepository.save(teamPerson);

        return team;
    }

    public void deleteTeam(Long teamId) {
        teamRepository.deleteById(teamId);
    }
}