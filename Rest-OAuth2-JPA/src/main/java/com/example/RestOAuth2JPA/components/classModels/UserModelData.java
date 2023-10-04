package com.example.RestOAuth2JPA.components.classModels;

import java.util.ArrayList;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import com.example.RestOAuth2JPA.DTO.entities.FileDB;
import com.example.RestOAuth2JPA.DTO.entities.Personal_info;
import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.DTO.entities.doctor.Doctor;
import com.example.RestOAuth2JPA.DTO.entities.patient.Address;
import com.example.RestOAuth2JPA.DTO.entities.patient.Patient;
import com.example.RestOAuth2JPA.enums.Status;
import com.example.RestOAuth2JPA.services.fileUpload.FileUploadUtils;

public class UserModelData {

    public User user;

    public String firstName;

    public String lastName;

    public String verificationCode;

    public String street;

    public String city;

    public Integer postCode;

    public MultipartFile userImage;

    public String phoneNumber;

    public String department;

    public String positionName;

    public boolean isUserDoctor;

    public UserModelData(String firstName, String lastName, String verificationCode, String street,
            String city, Integer postCode, MultipartFile userImage, boolean isUserDoctor, 
            String phoneNumber, String department, String positionName, User user) {

        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.verificationCode = verificationCode;
        this.street = street;
        this.city = city;
        this.postCode = postCode;
        this.userImage = userImage;
        this.phoneNumber = phoneNumber;
        this.isUserDoctor = isUserDoctor;

        if (isUserDoctor) {
            this.department = department;
            this.positionName = positionName;
        }
    }

    public UserModelData() {
        
    }

    public User setCompleteUserData() {
        Address address = new Address(street, city, postCode);

        Personal_info info = new Personal_info(firstName, lastName, phoneNumber, address, verificationCode);
    
                //address.setPersonalInfo(info);
                //1 info.setAddress(address);
                
        var entity = isUserDoctor ? new Doctor(info, department, positionName) : new Patient(info, Status.Unknown);
        
        //info.setPerson(patient);
        //1 patient.setPersonalInfo(info);
        
        user.setEnabled(true);

        FileDB userImageFile = FileUploadUtils.mkdir_and_upload_file_and_return_imagePath(user.getUsername(), user.getRole(), userImage);
        user.setProfileImage(userImageFile);
        
        if (isUserDoctor) {
            entity = (Doctor) entity; // to check value
            user.setDoctor((Doctor)entity);
        }
        else {
            entity = (Patient) entity; //to check value
            user.setPatient((Patient)entity);
        }
        return user;
    }
}
