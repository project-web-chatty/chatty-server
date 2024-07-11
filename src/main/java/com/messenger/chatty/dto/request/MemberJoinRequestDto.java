package com.messenger.chatty.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
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
