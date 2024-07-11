package com.messenger.chatty.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class MemberJoinRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String nickname;

    private String introduction;

    public void encodePassword(String encodedPassword){
        this.password =encodedPassword;
    }
}
