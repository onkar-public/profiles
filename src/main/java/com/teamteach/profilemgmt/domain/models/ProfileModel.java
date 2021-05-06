package com.teamteach.profilemgmt.domain.models;

import com.teamteach.profilemgmt.domain.models.vo.IndividualType;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@Document(collection = "profiles")
public class ProfileModel {
    
    @Transient
    public static final String SEQUENCE_NAME = "profiles_sequence";

    @Id
    protected String profileId;
    private String email;
    private String ownerId;
    private String info;
    private String birthYear;
    private String fname;
    private String lname;
    private IndividualType userType;
    private String relation;
    private String mobile;
    private String profileImage;
    private String countryCode;
    private String timezone;

    // public void setProfileImage(ProfileImage profileImage) {
    //     this.profileImage = profileImage;
    // }
}
