package com.teamteach.profilemgmt.infra.persistence.dal;

import com.teamteach.profilemgmt.domain.models.ProfileModel;
import com.teamteach.profilemgmt.domain.ports.out.IProfileRepository;
import com.teamteach.profilemgmt.infra.persistence.repo.ProfileJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileDAL implements IProfileRepository {

    final ProfileJPARepository profileJPARepository;

    @Override
    public boolean profileExistsById(String profileId) {
        return profileJPARepository.existsByProfileid(profileId);
    }

    @Override
    public String setupInitialProfile(ProfileModel profileModel) {
        return null;
    }
}
