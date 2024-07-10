package com.messenger.chatty.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;


@Getter
@Setter
public class MemberProfileUpdateRequestDto {
    private Optional<String> name ;
    private Optional<String> nickname;
    private Optional<String> introduction;
}
