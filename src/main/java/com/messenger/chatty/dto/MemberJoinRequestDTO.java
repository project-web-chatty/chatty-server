package com.messenger.chatty.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberJoinRequestDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String nickname;

    private String introduction;

    public void changePassword(String encodedPassword){
        this.password =encodedPassword;
    }
}
