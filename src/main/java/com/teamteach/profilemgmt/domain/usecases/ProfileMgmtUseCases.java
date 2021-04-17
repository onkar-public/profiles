package com.teamteach.profilemgmt.domain.usecases;

import com.teamteach.profilemgmt.domain.command.*;
import com.teamteach.profilemgmt.domain.ports.in.IProfileMgmt;
import com.teamteach.profilemgmt.domain.models.*;
import com.teamteach.profilemgmt.domain.models.vo.IndividualType;
import com.teamteach.profilemgmt.domain.ports.out.IProfileRepository;
import com.teamteach.commons.connectors.rabbit.core.IMessagingPort;

import java.util.function.Consumer;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProfileMgmtUseCases implements IProfileMgmt {

    final IProfileRepository profileRepository;
    final IMessagingPort messagingPort;

    @PostConstruct
    void initMQ() {
        messagingPort.registerGeneralResponseListener("event.profileupdate", BasicProfileCreationCommand.class, queueConsumer);
    }

    Consumer<BasicProfileCreationCommand> queueConsumer = new Consumer<BasicProfileCreationCommand>() {
        @Override
        public void accept(BasicProfileCreationCommand userProfile) {
            createBasicProfile(userProfile);
        }
    };

    @Override
    public ProfileModel createBasicProfile(BasicProfileCreationCommand signUpCommand) {
        ProfileModel profileModel = new ProfileModel(signUpCommand.getUserid(), "", "", "", signUpCommand.getFname(), signUpCommand.getLname(), new IndividualType(ProfileTypes.Parent));
        return profileRepository.saveProfile(profileModel);
    }

    @Override
    public void addChild(String parentProfileId, AddChildCommand addChildCommand) {
        // Validate that the Parent Profile Id

        // Check for duplicate children

        //Add the child profile
        ProfileModel profileModel = new ProfileModel("",addChildCommand.getParentId(),addChildCommand.getInfo(),addChildCommand.getBirthYear(),addChildCommand.getFName(),addChildCommand.getLName(),null);
        profileRepository.addChild(parentProfileId,addChildCommand);
    }
}
