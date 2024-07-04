package com.messenger.chatty.repository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import com.messenger.chatty.config.DataCleaner;
import com.messenger.chatty.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private DataCleaner cleaner;


    @BeforeEach
    public void setup(){
        Member memberA = Member.builder()
                .username("sh020119")
                .password("0000")
                .email("sh020119@naver.com")
                .name("오수현")
                .nickname("우주최강수현")
                .introduction("안녕하세요")
                .profile_img("http://img.example.com").build();

        Member memberB = Member.builder()
                .username("gildong123")
                .password("1234")
                .email("gildong@naver.com")
                .name("홍길동")
                .nickname("조선최강길동")
                .introduction("안녕하세요")
                .profile_img("http://img.terror.com").build();


        memberRepository.save(memberA);
        memberRepository.save(memberB);

    }
    @AfterEach
    public void clear(){
        cleaner.clear();
    }




    @Test
    @DisplayName("저장된 멤버 수를 확인")
    public void testMembersCount(){
         Assertions.assertThat(memberRepository.findAll()).hasSize(2);
    }


    @Test
    @DisplayName("필드별로 find 하는 메서드 확인")
    public void testFindMethods(){
        Member memByUsername = memberRepository.findByUsername("sh020119");
        Member memByEmail = memberRepository.findByEmail("sh020119@naver.com");
        Member memByNickname = memberRepository.findByNickname("우주최강수현");
        Member memByName = memberRepository.findByName("오수현");


        Assertions.assertThat(memByUsername.getId()).isEqualTo(memByEmail.getId());
        Assertions.assertThat(memByEmail.getId()).isEqualTo(memByNickname.getId());
        Assertions.assertThat(memByNickname.getId()).isEqualTo(memByName.getId());

    }

    @Test
    @DisplayName("멤버 필드 수정")
    public void testChangeField(){
        Member suhyeon  = memberRepository.findByUsername("sh020119");
        suhyeon.changeNickname("한국최고수현");
        memberRepository.save(suhyeon);

        Assertions.assertThat(memberRepository.findById(suhyeon.getId()).get().getNickname())
                .isEqualTo("한국최고수현");
    }

    @Test
    @DisplayName("멤버 삭제")
    public void testRemoveMember(){
        Member member = memberRepository.findByUsername("sh020119");
        memberRepository.deleteById(member.getId());
        Assertions.assertThat(memberRepository.findAll()).hasSize(1);
    }



    @Test
    @DisplayName("유저네임이 중복될 시 exception")
    public void testUniquenessOfUsername(){
        Member newMember = Member.builder()
                .username("sh020119") // not unique
                .password("new")
                .email("new@naver.com")
                .name("new")
                .nickname("new")
                .introduction("new")
                .profile_img("http://img.example.com").build();


        assertThrows(DataIntegrityViolationException.class,()->{
            memberRepository.save(newMember);

        });

    }


    @Test
    @DisplayName("nullable=false 인 필드 누락시 exception")
    public void testNotNullableFields(){
        // password is null
        Member emptyPasswordMember = Member.builder()
                .username("sh020119")
                .email("new@naver.com")
                .name("new")
                .nickname("new")
                .introduction("new")
                .profile_img("http://img.example.com").build();
        // email is null
        Member emptyEmailMember = Member.builder()
                .username("sh020119")
                .email("new@naver.com")
                .name("new")
                .nickname("new")
                .introduction("new")
                .profile_img("http://img.example.com").build();


        assertThrows(DataIntegrityViolationException.class,()->{
            memberRepository.save(emptyPasswordMember);

        });

        assertThrows(DataIntegrityViolationException.class,()->{
            memberRepository.save(emptyEmailMember);

        });

    }








}

