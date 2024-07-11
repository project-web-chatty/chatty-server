package com.messenger.chatty.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @PostMapping("/teams")
    public ResponseEntity<Team> createTeam(@RequestBody Team team) {
        Team createdTeam = teamService.createTeam(team);
        return new ResponseEntity<>(createdTeam, HttpStatus.CREATED);
    }

    @PostMapping("/persons")
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        Person createdPerson = teamService.createPerson(person);
        return new ResponseEntity<>(createdPerson, HttpStatus.CREATED);
    }

    @PostMapping("/teams/{teamId}/members/{personId}")
    public ResponseEntity<Team> addMemberToTeam(@PathVariable Long teamId, @PathVariable Long personId) {
        Team updatedTeam = teamService.addMemberToTeam(teamId, personId);
        return new ResponseEntity<>(updatedTeam, HttpStatus.OK);
    }

    @DeleteMapping("/teams/{teamId}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}