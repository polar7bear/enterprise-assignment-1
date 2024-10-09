package com.ssg.assignment.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service {

    public String uploadFile(MultipartFile file) {
        return "/image.png";
    }
}
