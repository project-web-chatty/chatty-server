package com.messenger.chatty.domain.message.dto.response;

import com.messenger.chatty.domain.member.entity.Member;
import com.messenger.chatty.domain.message.dto.request.MessageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDto extends MessageDto {
    private String id;

    public void fillOutMemberInfo(Member member) {
        this.setSenderNickname(member.getNickname());
        this.setSenderProfileImg(member.getProfile_img());
    }
}
