package com.messenger.chatty.dto.response.member;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TokenResponseDto {
    String refresh_token;
    String access_token;
}
