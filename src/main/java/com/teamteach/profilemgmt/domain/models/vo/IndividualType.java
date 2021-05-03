package com.teamteach.profilemgmt.domain.models.vo;


import com.teamteach.profilemgmt.domain.models.ProfileTypes;

import lombok.Getter;

@Getter
public class IndividualType {
    ProfileTypes type;

    public static IndividualType createType(String input) {
        return new IndividualType(determineType(input));
    }

    private static ProfileTypes determineType(String input) {
        return ProfileTypes.Parent;
    }

    public IndividualType(ProfileTypes type) {
        this.type = type;
    }

}
