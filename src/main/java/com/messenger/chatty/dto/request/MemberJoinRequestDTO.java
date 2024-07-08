package com.messenger.chatty.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
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
