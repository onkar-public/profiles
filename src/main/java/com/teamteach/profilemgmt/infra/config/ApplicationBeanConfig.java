package com.teamteach.profilemgmt.infra.config;

import com.teamteach.profilemgmt.domain.ports.in.IProfileMgmt;
import com.teamteach.profilemgmt.domain.ports.out.IProfileRepository;
import com.teamteach.profilemgmt.domain.usecases.ProfileMgmtUseCases;
import com.teamteach.profilemgmt.infra.persistence.dal.ProfileDAL;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@RequiredArgsConstructor
public class ApplicationBeanConfig {

    final IProfileRepository profileRepository;
    final MongoTemplate mongoTemplate;

    @Bean("profileMgmtSvc")
    IProfileMgmt profileMgmt() {
        return new ProfileMgmtUseCases(profileRepository);
    }
    @Bean("profileDALayer")
    IProfileRepository profileDAL() {
        return new ProfileDAL(mongoTemplate);
    }
}
