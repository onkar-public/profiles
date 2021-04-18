package com.teamteach.profilemgmt.domain.usecases;

import com.teamteach.profilemgmt.domain.command.*;
import com.teamteach.profilemgmt.domain.ports.in.IProfileMgmt;
import com.teamteach.profilemgmt.domain.models.*;
import com.teamteach.profilemgmt.domain.models.vo.IndividualType;
import com.teamteach.profilemgmt.domain.ports.out.IProfileRepository;
import com.teamteach.commons.connectors.rabbit.core.IMessagingPort;
import com.teamteach.profilemgmt.domain.usecases.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Consumer;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProfileMgmtUseCases implements IProfileMgmt {

    final IProfileRepository profileRepository;
    final IMessagingPort messagingPort;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

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
        ProfileModel profileModel = ProfileModel.builder()
                                                .userId(sequenceGeneratorService.generateSequence(ProfileModel.SEQUENCE_NAME))
                                                .fName(signUpCommand.getFName())
                                                .lName(signUpCommand.getLName())
                                                .userType(new IndividualType(ProfileTypes.Parent))
                                                .build();
        return profileRepository.saveProfile(profileModel);
    }

    @Override
    public ProfileModel addChild(AddChildCommand addChildCommand) {
        // Validate that the Parent Profile Id

        // Check for duplicate children

        //Add the child profile
        ProfileModel profileModel = ProfileModel.builder()
                                                .userId(sequenceGeneratorService.generateSequence(ProfileModel.SEQUENCE_NAME))
                                                .ownerId(addChildCommand.getParentId())
                                                .fName(addChildCommand.getFName())
                                                .lName(addChildCommand.getLName())
                                                .birthYear(addChildCommand.getBirthYear())
                                                .info(addChildCommand.getInfo())
                                                .userType(new IndividualType(ProfileTypes.Child))
                                                .build();
        return profileRepository.addChild(profileModel);
    }
}
