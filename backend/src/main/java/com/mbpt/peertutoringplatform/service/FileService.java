package com.mbpt.peertutoringplatform.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadImage(MultipartFile imageFile, String folderName);
}
