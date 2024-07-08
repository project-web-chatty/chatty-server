package com.messenger.chatty.dto.response;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class BaseResDTO {
    LocalDateTime createdDate;
    LocalDateTime lastModifiedDate;
}
