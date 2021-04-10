package com.teamteach.profilemgmt.infra.config;

import com.teamteach.profilemgmt.domain.mappers.ProfileCommandMapper;
import com.teamteach.profilemgmt.domain.ports.in.IProfileMgmt;
import com.teamteach.profilemgmt.domain.ports.out.IProfileRepository;
import com.teamteach.profilemgmt.domain.usecases.ProfileMgmtUseCases;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApplicationBeanConfig {

    final IProfileRepository profileRepository;
    final ProfileCommandMapper profileCommandMapper;

    @Bean("profileMgmtSvc")
    IProfileMgmt profileMgmt() {
        return new ProfileMgmtUseCases(profileRepository, profileCommandMapper);
    }
}
