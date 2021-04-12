package com.teamteach.profilemgmt.infra.persistence.dal;

import com.teamteach.profilemgmt.domain.models.ProfileModel;
import com.teamteach.profilemgmt.domain.ports.out.IProfileRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.MongoTemplate;

@Component
@RequiredArgsConstructor
public class ProfileDAL  implements IProfileRepository {
    final MongoTemplate mongoTemplate;

    @Override
    public boolean profileExistsById(String profileId) {
        return true;
    }

    @Override
    public String setupInitialProfile(ProfileModel profileModel) {
        return null;
    }

    @Override
    public ProfileModel getProfileByProfileId(String profileId) {
        return null;
    }

    @Override
    public void saveProfile(ProfileModel profileModel) {
        mongoTemplate.save(profileModel);
    }
}