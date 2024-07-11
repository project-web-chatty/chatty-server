package com.messenger.chatty;

import com.messenger.chatty.config.DataCleaner;
import com.messenger.chatty.dto.request.MemberJoinRequestDto;
import com.messenger.chatty.entity.Channel;
import com.messenger.chatty.entity.ChannelJoin;
import com.messenger.chatty.entity.Member;
import com.messenger.chatty.entity.Workspace;
import com.messenger.chatty.repository.ChannelRepository;
import com.messenger.chatty.repository.MemberRepository;
import com.messenger.chatty.repository.WorkspaceRepository;
import com.messenger.chatty.test.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class RelationTest {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private TeamPersonRepositroy teamPersonRepositroy;


    @Autowired
    private DataCleaner cleaner;

    @BeforeEach
    public void setUp(){
        Team team = new Team();
        team.setName("Team A");

        Person person = new Person();
        person.setName("Person A");

        TeamPerson teamPerson = new TeamPerson();
        team.addTeamPerson(teamPerson);
        person.addTeamPerson(teamPerson);
        teamPerson.setTeam(team);
        teamPerson.setPerson(person);

        teamRepository.save(team);
        personRepository.save(person);


    }
    @AfterEach
    public void clear(){
        cleaner.clear();
    }


    @Test
    public void test(){


        teamRepository.deleteById(1L);
       Assertions.assertThat(teamRepository.findAll()).hasSize(0);

    }


}
