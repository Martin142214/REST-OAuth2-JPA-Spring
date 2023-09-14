package com.example.RestOAuth2JPA.services.fileUpload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.RestOAuth2JPA.DTO.entities.FileDB;
import com.example.RestOAuth2JPA.DTO.entities.auth.Role;

public class FileUploadUtils {

    public static FileDB mkdir_and_upload_file_and_return_imagePath(String username, Role role, MultipartFile userImage) {
        String imagesPath = System.getProperty("images.path");

        String newFileDirectoryName = FileUploadUtils.concatenate("profile", "-", username.toLowerCase());
        File newFileDirectory = new File(imagesPath, newFileDirectoryName);

        FileDB fileDB = new FileDB();
        try {
            if(newFileDirectory.mkdir()){
                String userImageName = StringUtils.cleanPath(userImage.getOriginalFilename());
    
                String userImagePath = FileUploadUtils.concatenate("/images", "/", newFileDirectoryName, "/", userImageName);
    
                FileUploadUtils.uploadImage(userImage, userImageName, newFileDirectory);
    
                fileDB = new FileDB(userImageName, userImage.getContentType(), userImagePath);       
            }   
        } catch (Exception e) {
            throw new Error(e.getMessage());
        }
        return fileDB;
    }

    public static void uploadImage(MultipartFile image, String imageName, File newDirectory) {
        try {
            FileUploadUtils.saveFile(newDirectory.getAbsolutePath(), imageName, image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void saveFile(String uploadDir, String fileName,
            MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
         
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
         
        InputStream inputStream = multipartFile.getInputStream();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);     
    }

    private static String concatenate(String... s)
    {   
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length; i++) {
            sb = sb.append(s[i]);
        }
 
        return sb.toString();
    }
}
