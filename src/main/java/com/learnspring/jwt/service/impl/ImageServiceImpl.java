package com.learnspring.jwt.service.impl;

import com.learnspring.jwt.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {
    @Override
    public String uploadImage(String path, MultipartFile file, Long userId) throws IOException {

        // get original file name.
        String originalFilename = file.getOriginalFilename();

        // generate unique file name to store in server.
        String randomId = UUID.randomUUID().toString();
        String finalFileName = "USER_PROFILE$" + String.valueOf(userId) + "-" + randomId.concat(originalFilename
                .substring(originalFilename.lastIndexOf(".")));

        // full path of file
        String filePath = path + File.separator + finalFileName;

        // create folder if not created
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }

        // file copy in folder
        Files.copy(file.getInputStream(), Paths.get(filePath));

        return finalFileName;
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {

        String fullPath = path + File.separator + fileName;
        InputStream is = new FileInputStream(fullPath);
        return is;
    }
}
