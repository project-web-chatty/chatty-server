package com.messenger.chatty.dto.request;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberUpdateRequestDto {
    private String name ;
    private String nickname;
    private String introduction;
}