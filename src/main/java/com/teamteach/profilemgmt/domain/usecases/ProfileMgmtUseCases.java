package com.teamteach.profilemgmt.domain.usecases;

import com.teamteach.profilemgmt.domain.command.*;
import com.teamteach.profilemgmt.domain.ports.in.IProfileMgmt;
import com.teamteach.profilemgmt.domain.models.*;
import com.teamteach.profilemgmt.domain.models.vo.IndividualType;
import com.teamteach.profilemgmt.domain.ports.out.IProfileRepository;
import com.teamteach.commons.connectors.rabbit.core.IMessagingPort;
import com.teamteach.profilemgmt.domain.usecases.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.teamteach.profilemgmt.domain.responses.*;

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
                                                .relation(signUpCommand.getRelation())
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
        Query query = new Query(Criteria.where("userId").is(userId).and("userType.type").is("Parent"));
        ProfileModel parentProfileModel = mongoTemplate.findOne(query, ProfileModel.class);
        if (parentProfileModel == null) return null;
        query = new Query(Criteria.where("userId").is(userId).and("userType.type").is("Child"));
        List<ProfileModel> children = mongoTemplate.find(query, ProfileModel.class);
        List<String> childIdList = new ArrayList<>();
        for (ProfileModel child : children) {
            childIdList.add(child.getFname());
        }
        ParentProfileResponseDto parentProfile = ParentProfileResponseDto.builder()
                                                                         .fname(parentProfileModel.getFname())
                                                                         .lname(parentProfileModel.getLname())
                                                                         .email(parentProfileModel.getUserId())
                                                                         .children(childIdList)
                                                                         .userType(parentProfileModel.getUserType().getType().toString())
                                                                         .profileId(parentProfileModel.getProfileId())
                                                                         .build();
        return parentProfile;                                                                
    }

    @Override
    public ObjectResponseDto editProfile(String profileId, EditProfileCommand editProfileCommand) {
        Query query = new Query(Criteria.where("profileId").is(profileId));
        ProfileModel editModel = mongoTemplate.findOne(query, ProfileModel.class);
        editModel.setFname(editProfileCommand.getFname());
        editModel.setLname(editProfileCommand.getLname());
        if(editProfileCommand.getUserType() == "Parent") {
            editModel.setMobile(editProfileCommand.getMobile());
        }
        mongoTemplate.save(editModel);
        return ObjectResponseDto.builder()
                                .success(true)
                                .message("Profile edited successfully")
                                .object(editModel)
                                .build();
    }
}
