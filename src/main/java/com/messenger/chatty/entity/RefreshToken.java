package com.messenger.chatty.entity;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Builder
@Table(name = "refresh_token")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // 다중 기기 로그인 등을 고려하면 나중에 멤버와의 일대다로 구성
    //@ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "member_id", nullable = false)
    @Column(unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String token;

    public static RefreshToken createTokenEntity(String token, String username){
       return RefreshToken.builder().username(username).token(token).build();
    }

}
