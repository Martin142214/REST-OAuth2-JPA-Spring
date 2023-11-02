package com.example.RestOAuth2JPA.services.fileUpload;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.RestOAuth2JPA.DTO.entities.FileDB;
import com.example.RestOAuth2JPA.DTO.repositories.secondary.IFileDBRepository;

@Service
public class FileDbService {

    @Autowired IFileDBRepository fileDBRepository;
    
    @Autowired
    public FileDbService() {
        
    }

    @Transactional(rollbackFor = {SQLException.class})
    public void saveNewFileDB(FileDB fileDB) throws SQLException {
        if (fileDB != null) {
            if (fileDB.ifAllFieldsAreNotEmpty()) {
                fileDBRepository.save(fileDB);         
            }
        }
    }
}
