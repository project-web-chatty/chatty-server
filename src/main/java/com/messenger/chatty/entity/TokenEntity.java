package com.messenger.chatty.entity;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    // 다중 기기 로그인 등을 고려하면 나중에 멤버와의 일대다로 구성
    //@ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "member_id", nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String token;

    public static TokenEntity createTokenEntity(String token, String username){
       return TokenEntity.builder().username(username).token(token).build();
    }

}
