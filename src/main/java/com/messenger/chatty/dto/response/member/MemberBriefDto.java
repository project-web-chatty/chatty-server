package com.messenger.chatty.dto.response.member;
import com.messenger.chatty.dto.response.BaseResDto;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


// 공개될 수 있는 멤버 정보
@Getter
@SuperBuilder
@ToString
public class MemberBriefDto extends BaseResDto {

    protected Long id;
    protected String username;
    protected String email;
    protected String role;
    protected String profile_img;
    protected String name;
    protected String nickname;
    protected String introduction;

}
