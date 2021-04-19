package com.teamteach.profilemgmt.domain.models;

import com.teamteach.profilemgmt.domain.models.vo.IndividualType;
import com.teamteach.profilemgmt.domain.models.vo.ProfileImage;
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
    private String userId;
    private String info;
    private String birthYear;
    private String fname;
    private String lname;
    private IndividualType userType;
    private String relation;
    private ProfileImage profileImage;

    public void setProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }
}
