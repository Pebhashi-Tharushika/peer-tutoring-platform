package com.mbpt.peertutoringplatform.service.impl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mbpt.peertutoringplatform.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Value("${aws.s3.access.key}")
    private String awsS3AccessKey;
    @Value("${aws.s3.secret.key}")
    private String awsS3SecretKey;

    @Override
    public String uploadImage(MultipartFile imageFile) {
        return saveFileToAWSS3Bucket(imageFile);
    }

//    @Override
//    public List<FileEntity> getAllFiles() {
//        return fileRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
//    }


    private String saveFileToAWSS3Bucket(MultipartFile file) {
        try {
            String s3FileName = file.getOriginalFilename();

            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsS3AccessKey, awsS3SecretKey);

            AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.US_EAST_1)
                    .build();

            InputStream inputStream = file.getInputStream();

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType("image/jpeg");

            String bucketName = "peer-tutor-image-bucket";
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3FileName, inputStream, objectMetadata);
            amazonS3Client.putObject(putObjectRequest);
            return "https://" + bucketName + ".s3.amazonaws.com/" + s3FileName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}