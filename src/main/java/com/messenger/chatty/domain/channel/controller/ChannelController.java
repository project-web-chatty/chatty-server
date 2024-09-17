package com.messenger.chatty.domain.channel.controller;

import com.messenger.chatty.domain.message.dto.MessageDto;
import com.messenger.chatty.domain.message.service.MessageService;
import com.messenger.chatty.global.config.web.AuthenticatedUsername;
import com.messenger.chatty.global.presentation.ApiResponse;
import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.global.presentation.annotation.api.ApiErrorCodeExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.messenger.chatty.global.presentation.annotation.api.PredefinedErrorStatus.AUTH;

@Tag(name = "CHANNEL API", description = "채널과 관련된 핵심적인 API 들입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChannelController {
    private final MessageService messageService;

    @Operation(summary = "안읽은 메세지 페이징 조회", description = "사용자가 읽지 않은 채널 내 메세지부터 조회하도록 합니다")
    @ApiErrorCodeExample(value = {
            ErrorStatus.CHANNEL_ACCESS_NOT_FOUND
    }, status = AUTH)
    @GetMapping("/unread/{channelId}")
    public ApiResponse<List<MessageDto>> getUnreadMessageInChannel(@AuthenticatedUsername String username,
                                                                   @PathVariable Long channelId,
                                                                   @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 10);
        return ApiResponse.onSuccess(messageService.getMessageByLastAccessTime(channelId, username, pageable));
    }

    @Operation(summary = "메세지 페이징 조회", description = "채널 내 메세지부터 조회하도록 합니다")
    @ApiErrorCodeExample(status = AUTH)
    @GetMapping("/{channelId}")
    public ApiResponse<List<MessageDto>> getMessageInChannel(@PathVariable Long channelId,
                                                             @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 10);
        return ApiResponse.onSuccess(messageService.getMessages(channelId, pageable));
    }

    @Operation(summary = "안읽은 메세지 개수", description = "사용자가 채널 내 읽지 않은 메세지의 개수를 확인합니다.")
    @ApiErrorCodeExample(value = {
            ErrorStatus.CHANNEL_ACCESS_NOT_FOUND
    }, status = AUTH)
    @GetMapping("/{channelId}/count")
    public ApiResponse<Long> countUnreadMessageInChannel(@AuthenticatedUsername String username,
                                                         @PathVariable Long channelId) {
        return ApiResponse.onSuccess(messageService.countUnreadMessage(channelId, username));
    }

}
