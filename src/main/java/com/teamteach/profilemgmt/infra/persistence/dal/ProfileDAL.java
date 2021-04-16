package com.teamteach.profilemgmt.infra.persistence.dal;

import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import com.teamteach.commons.connectors.rabbit.core.IMessagingPort;
import com.teamteach.profilemgmt.domain.command.BasicProfileCreationCommand;
import com.teamteach.profilemgmt.domain.models.ProfileModel;
import com.teamteach.profilemgmt.domain.ports.out.IProfileRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.MongoTemplate;

@Component
@RequiredArgsConstructor
public class ProfileDAL  implements IProfileRepository {
    final MongoTemplate mongoTemplate;
    final IMessagingPort messagingPort;

    @PostConstruct
    void initMQ() {
        messagingPort.registerGeneralResponseListener("event.profileupdate", BasicProfileCreationCommand.class, queueConsumer);
    }

    Consumer<BasicProfileCreationCommand> queueConsumer = new Consumer<BasicProfileCreationCommand>() {
        @Override
        public void accept(BasicProfileCreationCommand userProfile) {
            System.out.println(userProfile.getFname()+" "+userProfile.getLname());
        }
    };
    
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