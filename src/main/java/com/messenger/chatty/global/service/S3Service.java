package com.messenger.chatty.global.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class S3Service {
    @Value("${variables.cloud.s3.bucketName}")
    private String bucketName;
    private final AmazonS3 s3Client;



    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, null));
        }
        return s3Client.getUrl(bucketName, fileName).toString();
    }

    public void deleteFile(String fileName) {
        s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }

    public String updateFile(String oldFileName, MultipartFile newFile) throws IOException {
        deleteFile(oldFileName); // 기존 파일 삭제
        return uploadFile(newFile); // 새로운 파일 업로드
    }
}
