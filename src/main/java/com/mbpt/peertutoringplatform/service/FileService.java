package com.mbpt.peertutoringplatform.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    String uploadImage(MultipartFile imageFile);
//    List<FileEntity> getAllFiles();
}
