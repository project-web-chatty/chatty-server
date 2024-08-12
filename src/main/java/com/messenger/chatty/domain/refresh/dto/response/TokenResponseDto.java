package com.messenger.chatty.domain.refresh.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TokenResponseDto {
    String refreshToken;
    String accessToken;
}
