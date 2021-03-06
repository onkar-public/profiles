package com.teamteach.profilemgmt.domain.ports.out;

import java.util.HashMap;
import java.util.List;

import com.teamteach.profilemgmt.domain.models.ProfileModel;
import com.teamteach.profilemgmt.domain.models.SearchKey;

public interface IProfileRepository {
   boolean profileExistsById(String profileId);
   String setupInitialProfile(ProfileModel profileModel);
   ProfileModel getProfileByProfileId(String profileId);
   ProfileModel saveProfile(ProfileModel profileModel);
   ProfileModel addChild(ProfileModel profileModel);
   boolean removeProfile(String ownerId);
   List<ProfileModel> getProfile(HashMap<SearchKey,Object> searchCriteria, HashMap<String,String> excludeCriteria);
}