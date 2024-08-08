package com.messenger.chatty.global.controller;


import com.messenger.chatty.global.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping
    public String uploadFile(@RequestParam("file") MultipartFile file) {
       return s3Service.uploadImage(file);
    }

    @DeleteMapping
    public String deleteFile(@RequestParam("url") String url) {
        s3Service.deleteImage(url);
        return "File deleted successfully: " + url;
    }

    @PutMapping
    public String updateFile(@RequestParam("url") String url, @RequestParam("file") MultipartFile newFile) {
       return s3Service.updateImage(url, newFile);
    }

}
