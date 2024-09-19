package com.messenger.chatty.domain.member.dto.request;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PasswordUpdateRequestDto {
    private String oldPassword;
    private String newPassword;
}
