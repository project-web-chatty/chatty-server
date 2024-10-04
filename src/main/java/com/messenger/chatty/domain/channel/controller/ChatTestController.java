package com.messenger.chatty.domain.channel.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/chat/test")
public class ChatTestController {

    @GetMapping("/room")
    public String getRoom(@RequestParam Long channelId, @RequestParam String accessToken, Model model) {
        log.info("channelId = {}", channelId);
        log.info("accessToken = {}", accessToken);
        model.addAttribute("channelId", channelId);
        model.addAttribute("accessToken", accessToken);
        return "chat/room";
    }
}
