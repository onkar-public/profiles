package com.teamteach.profilemgmt.domain.ports.out;

import com.teamteach.profilemgmt.domain.command.AddChildCommand;
import com.teamteach.profilemgmt.domain.models.ProfileModel;

public interface IProfileRepository {
   boolean profileExistsById(String profileId);
   String setupInitialProfile(ProfileModel profileModel);
   ProfileModel getProfileByProfileId(String profileId);
   ProfileModel saveProfile(ProfileModel profileModel);
   void addChild(String parentId, AddChildCommand childData);
}
