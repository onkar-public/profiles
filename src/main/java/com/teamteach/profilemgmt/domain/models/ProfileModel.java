package com.teamteach.profilemgmt.domain.models;

import com.teamteach.profilemgmt.domain.models.vo.ProfileImage;

public class ProfileModel {
    NamedIndividualModel individual;
    ProfileImage profileImage;

    public String getIdentity() {
        return individual.getIdentity();
    }

    public ProfileModel(NamedIndividualModel individual) {
        this.individual = individual;
    }
}
