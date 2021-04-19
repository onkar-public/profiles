package com.teamteach.profilemgmt.domain.usecases;

import com.teamteach.profilemgmt.domain.command.*;
import com.teamteach.profilemgmt.domain.ports.in.IProfileMgmt;
import com.teamteach.profilemgmt.domain.models.*;
import com.teamteach.profilemgmt.domain.models.vo.IndividualType;
import com.teamteach.profilemgmt.domain.ports.out.IProfileRepository;
import com.teamteach.commons.connectors.rabbit.core.IMessagingPort;
import com.teamteach.profilemgmt.domain.usecases.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.teamteach.profilemgmt.domain.responses.ParentProfileResponseDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.function.Consumer;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class ProfileMgmtUseCases implements IProfileMgmt {

    final IProfileRepository profileRepository;
    final IMessagingPort messagingPort;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
	private MongoTemplate mongoTemplate;

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
                                                .profileId(sequenceGeneratorService.generateSequence(ProfileModel.SEQUENCE_NAME))
                                                .userId(signUpCommand.getUserId())
                                                .fname(signUpCommand.getFname())
                                                .lname(signUpCommand.getLname())
                                                .userType(new IndividualType(ProfileTypes.Parent))
                                                .build();
        return profileRepository.saveProfile(profileModel);
    }

    @Override
    public ProfileModel addChild(AddChildCommand addChildCommand) {
        ProfileModel profileModel = ProfileModel.builder()
                                                .profileId(sequenceGeneratorService.generateSequence(ProfileModel.SEQUENCE_NAME))
                                                .userId(addChildCommand.getParentId())
                                                .fname(addChildCommand.getFname())
                                                .lname(addChildCommand.getLname())
                                                .birthYear(addChildCommand.getBirthYear())
                                                .info(addChildCommand.getInfo())
                                                .userType(new IndividualType(ProfileTypes.Child))
                                                .build();
        return profileRepository.addChild(profileModel);
    }

    @Override
    public ParentProfileResponseDto getProfile(String userId){
        Query query = new Query(Criteria.where("userId").is(userId));
        ProfileModel profileModel = mongoTemplate.findOne(query, ProfileModel.class);
        ParentProfileResponseDto parentProfile = ParentProfileResponseDto.builder()
                                                                         .fname(profileModel.getFname())
                                                                         .lname(profileModel.getLname())
                                                                         .email(profileModel.getUserId())
                                                                         .userType(profileModel.getUserType().getType().toString())
                                                                         .build();
        return parentProfile;                                                                
    }
}
