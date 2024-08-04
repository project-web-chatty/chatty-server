package com.messenger.chatty.domain.refresh.dto.response;


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
