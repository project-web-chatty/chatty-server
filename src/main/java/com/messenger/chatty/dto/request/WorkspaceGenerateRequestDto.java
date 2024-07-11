package com.messenger.chatty.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class WorkspaceGenerateRequestDto {
    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    @Size(min = 2,max = 15,message = "최소 2자에서 최대 20자까지 가능합니다.")
    private String name;

    private String description;
}
