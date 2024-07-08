package com.messenger.chatty.dto.response;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class MemberProfileResponseDto extends BaseResDto {

    private Long id;
    private String username;
    private String email;
    private String role;
    private String profile_img;
    private String name;
    private String nickname;
    private String introduction;

}
