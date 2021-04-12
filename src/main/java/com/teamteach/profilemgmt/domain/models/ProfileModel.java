package com.teamteach.profilemgmt.domain.models;

import com.teamteach.profilemgmt.domain.models.vo.IndividualType;
import com.teamteach.profilemgmt.domain.models.vo.ProfileImage;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "profiles")
public class ProfileModel {
    String userid;
    String fname;
    String lname;
    IndividualType usertype;
    ProfileImage profileImage;

    public ProfileModel(String userid, String fname, String lname, IndividualType usertype) {
        this.userid = userid;
        this.fname = fname;
        this.lname = lname;
        this.usertype = usertype;
    }
    public void setProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }
}
