package com.teamteach.profilemgmt.domain.ports.out;

import com.teamteach.profilemgmt.domain.models.ProfileModel;

public interface IProfileRepository {
   boolean profileExistsById(String profileId);
   String setupInitialProfile(ProfileModel profileModel);
}
