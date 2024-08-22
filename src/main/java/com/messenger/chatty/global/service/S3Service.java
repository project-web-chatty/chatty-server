package com.messenger.chatty.global.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.global.presentation.exception.custom.S3Exception;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    @Value("${variables.cloud.s3.bucketName}")
    private String bucketName;
    private final AmazonS3 s3Client;
    private final List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "gif");



    // upload
    public String uploadImage(MultipartFile file) {
        this.validateFileData(file);
        this.validateImageFileExtension(file.getOriginalFilename());
        return this.uploadImageToS3(file);
    }

    private void validateFileData(MultipartFile file){
        if(file == null ||file.isEmpty() || Objects.isNull(file.getOriginalFilename())){
            throw new S3Exception(ErrorStatus.EMPTY_FILE_EXCEPTION);
        }
    }
    private void validateImageFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new S3Exception(ErrorStatus.NO_FILE_EXTENSION);
        }
        String extension = filename.substring(lastDotIndex + 1).toLowerCase();
        if (!allowedExtentionList.contains(extension)) {
            throw new S3Exception(ErrorStatus.INVALID_FILE_EXTENSION);
        }
    }

    private String uploadImageToS3(MultipartFile image) {
        try {
            String originalFilename = image.getOriginalFilename();
            String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename.substring(0,10);
            InputStream inputStream = image.getInputStream();
            s3Client.putObject(new PutObjectRequest(bucketName, s3FileName, inputStream, null));
            return s3Client.getUrl(bucketName, s3FileName).toString();
        }
        catch (IOException e){
            throw new S3Exception(ErrorStatus.IO_EXCEPTION_ON_IMAGE_UPLOAD);
        }

    }


    // delete
    public void deleteImage(String imageURI) {
        String key = getKeyFromImageURI(imageURI);
        this.deleteImageToS3(key);
    }
    private String getKeyFromImageURI(String imageURI){
        try{
            URL url = new URL(imageURI);
            String decodingKey = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
            return decodingKey.substring(1);
        }catch (IOException e){
            throw new S3Exception(ErrorStatus.INVALID_FILE_URI);
        }
    }
    private void deleteImageToS3(String key){
        try{
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, key));
        }catch (Exception e){
            throw new S3Exception(ErrorStatus.IO_EXCEPTION_ON_IMAGE_DELETE);
        }
    }


    // update
    public String updateImage(String imageURI, MultipartFile newFile)  {
        deleteImage(imageURI);
        return uploadImage(newFile);
    }
}
