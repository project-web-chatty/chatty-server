package com.messenger.chatty.dto.response;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class MemberResponseDTO extends BaseResDTO{

    private Long id;
    private String username;
    private String email;
    private String role;
    private String profile_img;
    private String name;
    private String nickname;
    private String introduction;

}
