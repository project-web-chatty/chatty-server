package com.messenger.chatty.domain.channel.controller;

import com.messenger.chatty.domain.channel.service.ChannelService;
import com.messenger.chatty.domain.message.dto.MessageDto;
import com.messenger.chatty.domain.message.entity.Message;
import com.messenger.chatty.domain.message.service.MessageService;
import com.messenger.chatty.domain.workspace.dto.request.WorkspaceGenerateRequestDto;
import com.messenger.chatty.global.config.web.AuthenticatedUsername;
import com.messenger.chatty.global.presentation.ApiResponse;
import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.global.presentation.annotation.api.ApiErrorCodeExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "CHANNEL API", description = "채널과 관련된 핵심적인 API 들입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChannelController {
    private final ChannelService channelService;
    private final MessageService messageService;

    @Operation(summary = "안읽은 메세지 페이징 조회")
    @ApiErrorCodeExample
    @GetMapping("/{channelId}")
    public ApiResponse<List<MessageDto>> getUnreadMessageInChannel(@AuthenticatedUsername String username,
                                                                   @PathVariable Long channelId,
                                                                   @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 10);
        return ApiResponse.onSuccess(messageService.getMessageByLastAccessTime(channelId, username, pageable););
    }



}
