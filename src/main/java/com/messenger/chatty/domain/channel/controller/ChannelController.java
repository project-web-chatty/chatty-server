package com.messenger.chatty.domain.channel.controller;

import com.messenger.chatty.domain.channel.dto.response.ChannelBriefDto;
import com.messenger.chatty.domain.channel.service.ChannelService;
import com.messenger.chatty.global.presentation.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChannelController {
    private final ChannelService channelService;
    @GetMapping("/rooms")
    public ApiResponse<List<ChannelBriefDto>> getRooms(Model model) {
        List<ChannelBriefDto> allChannels = channelService.getAllChannels();
//        model.addAttribute("channels", allChannels);
        return ApiResponse.onSuccess(allChannels);
    }

//    @GetMapping("/room")
//    public String getRoom(Long channelId, String senderNickname, Model model) {
//        model.addAttribute("channelId", channelId);
//        model.addAttribute("senderNickname", senderNickname);
//        return "chat/room";
//    }

}
