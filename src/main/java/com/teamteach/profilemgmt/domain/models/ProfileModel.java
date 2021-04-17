package com.teamteach.profilemgmt.domain.models;

import com.teamteach.profilemgmt.domain.models.vo.IndividualType;
import com.teamteach.profilemgmt.domain.models.vo.ProfileImage;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@Document(collection = "profiles")
public class ProfileModel {
    String userid;
    String ownerId;
    String info;
    String birthYear;
    String fname;
    String lname;
    IndividualType usertype;
    //ProfileImage profileImage;

    // public void setProfileImage(ProfileImage profileImage) {
    //     this.profileImage = profileImage;
    // }
}
