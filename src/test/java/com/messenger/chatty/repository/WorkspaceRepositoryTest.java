//package com.messenger.chatty.repository;
//import com.messenger.chatty.config.DataCleaner;
//import com.messenger.chatty.entity.Workspace;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.dao.DataIntegrityViolationException;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//
//@SpringBootTest
//public class WorkspaceRepositoryTest {
//
//    @Autowired
//    private WorkspaceRepository workspaceRepository;
//    @Autowired
//    private DataCleaner cleaner;
//
//
//    @BeforeEach
//    public void setup(){
//        Workspace tossWorkspace = Workspace.builder()
//                .name("toss team")
//                .description("토스 개발팀 워크스페이스")
//                .profile_img("http://img.toss.com")
//                .build();
//        workspaceRepository.save(tossWorkspace);
//    }
//
//    @AfterEach
//    public void clear(){
//        cleaner.clear();
//    }
//
//
//    @Test
//    @DisplayName("이름으로 찾기")
//    public void testFindByName(){
//         Assertions.assertThat(workspaceRepository.findByName("toss team")).isNotNull();
//
//    }
//
//    @Test
//    @DisplayName("이름 없이 저장시 exception")
//    public void testNullableException(){
//        assertThrows(DataIntegrityViolationException.class,()->{
//           workspaceRepository.save(Workspace.builder().build());
//        });
//    }
//
//
//
//}
