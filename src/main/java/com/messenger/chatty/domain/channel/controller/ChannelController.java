package com.messenger.chatty.domain.channel.controller;

import com.messenger.chatty.domain.channel.service.ChannelService;
import com.messenger.chatty.domain.message.dto.response.MessageResponseDto;
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
    private final ChannelService channelService;


    @Operation(summary = "메세지 리스트 조회", description = "채널 내 메세지부터 조회하도록 합니다")
    @ApiErrorCodeExample(status = AUTH)
    @GetMapping("/{channelId}")
    public ApiResponse<List<MessageResponseDto>> getMessageInChannel(@PathVariable Long channelId,
                                                                     @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 10);
        return ApiResponse.onSuccess(messageService.getMessages(channelId, pageable));
    }

    @Operation(summary = "채널 마지막 메세지", description = "채널의 마지막 메세지를 조회합니다.")
    @ApiErrorCodeExample(status = AUTH)
    @GetMapping("/{channelId}/last")
    public ApiResponse<MessageResponseDto> getLastMessageInChannel(@PathVariable Long channelId){
        return ApiResponse.onSuccess(messageService.getLastMessageInChannel(channelId));
    }

    @Operation(summary = "안읽은 메세지 개수", description = "사용자가 채널 내 읽지 않은 메세지의 개수를 확인합니다.")
    @ApiErrorCodeExample(value = {
            ErrorStatus.CHANNEL_ACCESS_NOT_FOUND
    }, status = AUTH)
    @GetMapping("/{channelId}/count")
    public ApiResponse<Long> countUnreadMessageInChannel(@AuthenticatedUsername String username,
                                                         @PathVariable Long channelId) {
        Long workspaceJoinId = channelService.getWorkspaceJoinId(channelId, username);
        return ApiResponse.onSuccess(messageService.countUnreadMessage(channelId, workspaceJoinId));
    }

    @Operation(summary = "읽은 마지막 메세지 아이디 조회", description = "사용자가 채널에서 읽은 마지막 메세지의 아이디를 조회합니다.")
    @ApiErrorCodeExample(value = {
            ErrorStatus.CHANNEL_ACCESS_NOT_FOUND
    }, status = AUTH)
    @GetMapping("/{channelId}/read/id")
    public ApiResponse<String> getLastReadMessageId(@AuthenticatedUsername String username,
                                                    @PathVariable Long channelId) {
        Long workspaceJoinId = channelService.getWorkspaceJoinId(channelId, username);
        return ApiResponse.onSuccess(messageService.getLastReadMessageId(channelId, workspaceJoinId));
    }

}
