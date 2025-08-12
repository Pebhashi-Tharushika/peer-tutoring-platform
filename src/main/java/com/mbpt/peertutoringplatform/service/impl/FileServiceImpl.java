package com.mbpt.peertutoringplatform.service.impl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mbpt.peertutoringplatform.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Value("${aws.s3.access.key}")
    private String awsS3AccessKey;
    @Value("${aws.s3.secret.key}")
    private String awsS3SecretKey;

    @Override
    public String uploadImage(MultipartFile imageFile, String folderName) {
        return saveFileToAWSS3Bucket(imageFile, folderName);
    }


    private String saveFileToAWSS3Bucket(MultipartFile file, String folderName) {
        try {
            String s3FileName = file.getOriginalFilename();

            String s3ObjectKey = folderName + "/" + s3FileName;

            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsS3AccessKey, awsS3SecretKey);

            AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.US_EAST_1)
                    .build();

            InputStream inputStream = file.getInputStream();

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType("image/jpeg");

            String bucketName = "peer-tutor-image-bucket";
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3ObjectKey, inputStream, objectMetadata);
            amazonS3Client.putObject(putObjectRequest);
            return String.format("https://%s.s3.amazonaws.com/%s/%s", bucketName, folderName, s3FileName);
        } catch (Exception e) {
            log.error("Failed to upload image file: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}