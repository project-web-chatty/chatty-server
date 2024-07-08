package com.messenger.chatty.dto.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class BaseResDto {
    LocalDateTime createdDate;
    LocalDateTime lastModifiedDate;
}
