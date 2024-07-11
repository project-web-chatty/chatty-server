package com.messenger.chatty.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
public class MemberJoinRequestDto {
    @NotBlank(message = "id는 필수 입력사항입니다.")
    @Size(min = 5,max = 20, message = "아이디는 최소 5자에서 최대 20자까지 가능합니다.")
    private String username;


    @NotBlank(message = "비밀번호는 필수 입력사항입니다.")
    private String password1;

    @NotBlank(message = "비밀번호는 필수 입력사항입니다.")
    private String password2;


    public void encodePassword(String encodedPassword){
        this.password1 =encodedPassword;
        this.password2 = encodedPassword;
    }
}
