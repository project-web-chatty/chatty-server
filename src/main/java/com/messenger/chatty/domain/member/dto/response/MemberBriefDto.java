package com.messenger.chatty.domain.member.dto.response;
import com.messenger.chatty.domain.base.dto.response.BaseResDto;
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
    protected String profileImg;
    protected String name;
    protected String nickname;
    protected String introduction;

}
