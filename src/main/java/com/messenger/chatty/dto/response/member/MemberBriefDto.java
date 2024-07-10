package com.messenger.chatty.dto.response.member;
import com.messenger.chatty.dto.response.BaseResDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;


// 공개될 수 있는 멤버 정보
@Getter
@SuperBuilder
public class MemberBriefDto extends BaseResDto {

    private Long id;
    private String username;
    private String email;
    private String role;
    private String profile_img;
    private String name;
    private String nickname;
    private String introduction;

}
