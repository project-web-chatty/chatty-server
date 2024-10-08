package com.messenger.chatty.domain.member.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class MemberUpdateRequestDto {
    private String name ;
    private String nickname;
    private String introduction;
}