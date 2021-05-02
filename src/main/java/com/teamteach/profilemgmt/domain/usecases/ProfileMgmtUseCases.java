package com.teamteach.profilemgmt.domain.usecases;

import com.teamteach.profilemgmt.domain.command.*;
import com.teamteach.profilemgmt.domain.ports.in.IProfileMgmt;
import com.teamteach.profilemgmt.domain.models.*;
import com.teamteach.profilemgmt.domain.models.vo.IndividualType;
import com.teamteach.profilemgmt.domain.ports.out.IProfileRepository;
import com.teamteach.commons.connectors.rabbit.core.IMessagingPort;
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
    public ObjectResponseDto createBasicProfile(BasicProfileCreationCommand signUpCommand) {
        ProfileModel profileModel = ProfileModel.builder()
                                                .profileId(sequenceGeneratorService.generateSequence(ProfileModel.SEQUENCE_NAME))
                                                .ownerId(signUpCommand.getOwnerId())
                                                .fname(signUpCommand.getFname())
                                                .email(signUpCommand.getEmail())
                                                .lname(signUpCommand.getLname())
                                                .userType(new IndividualType(ProfileTypes.Parent))
                                                .relation(signUpCommand.getRelation())
                                                .mobile(signUpCommand.getMobile())
                                                .countryCode("")
                                                .build();
        return new ObjectResponseDto(true, "Success", profileRepository.saveProfile(profileModel));
    }

    @Override
    public ObjectResponseDto addChild(AddChildCommand addChildCommand) {
        if (addChildCommand.getOwnerId() == null || addChildCommand.getFname() == null) {
            return ObjectResponseDto.builder()
            .success(false)
            .message("Please provide at least the ownerId and fname in the requestBody")
            .object(addChildCommand)
            .build();
        }
        Query query = new Query(Criteria.where("ownerId").is(addChildCommand.getOwnerId()).and("fname").is(addChildCommand.getFname()));
        ProfileModel findChild = mongoTemplate.findOne(query, ProfileModel.class);
        if(findChild == null) {
            ProfileModel profileModel = ProfileModel.builder()
                                                    .profileId(sequenceGeneratorService.generateSequence(ProfileModel.SEQUENCE_NAME))
                                                    .ownerId(addChildCommand.getOwnerId())
                                                    .fname(addChildCommand.getFname())
                                                    .lname(addChildCommand.getLname())
                                                    .birthYear(addChildCommand.getBirthYear())
                                                    .info(addChildCommand.getInfo())
                                                    .userType(new IndividualType(ProfileTypes.Child))
                                                    .build();
            return new ObjectResponseDto(true, "Success", profileRepository.addChild(profileModel));
        } else {
            return new ObjectResponseDto(false, "Child with same name cannot be added", null);
        }

    }

    @Override
    public ParentProfileResponseDto getProfile(String ownerId){
        Query query = new Query(Criteria.where("ownerId").is(ownerId).and("userType.type").is("Parent"));
        ProfileModel parentProfileModel = mongoTemplate.findOne(query, ProfileModel.class);
        if (parentProfileModel == null) return null;
        query = new Query(Criteria.where("ownerId").is(ownerId).and("userType.type").is("Child"));
        List<ProfileModel> children = mongoTemplate.find(query, ProfileModel.class);
        List<ChildProfileDto> childIdList = new ArrayList<>();
        for (ProfileModel child : children) {
            childIdList.add(new ChildProfileDto(child.getProfileId(), child.getFname()));
        }
        ParentProfileResponseDto parentProfile = ParentProfileResponseDto.builder()
                                                                         .fname(parentProfileModel.getFname())
                                                                         .lname(parentProfileModel.getLname())
                                                                         .email(parentProfileModel.getEmail())
                                                                         .mobile(parentProfileModel.getMobile())
                                                                         .countryCode(parentProfileModel.getCountryCode())
                                                                         .relation(parentProfileModel.getRelation())
                                                                         .children(childIdList)
                                                                         .userType(parentProfileModel.getUserType().getType().toString())
                                                                         .profileId(parentProfileModel.getProfileId())
                                                                         .build();
        return parentProfile;                                                                
    }

    @Override
    public ObjectResponseDto editProfile(String profileId, EditProfileCommand editProfileCommand) {
        if (editProfileCommand.getUserType() == null) {
            return ObjectResponseDto.builder()
            .success(false)
            .message("Please provide userType in the requestBody")
            .object(editProfileCommand)
            .build();
        }
        Query query = new Query(Criteria.where("profileId").is(profileId).and("userType.type").is(editProfileCommand.getUserType()));
        ProfileModel editModel = mongoTemplate.findOne(query, ProfileModel.class);
        if (editModel == null) {
            return ObjectResponseDto.builder()
            .success(false)
            .message("No profile record found with given profileId and userType")
            .object(editModel)
            .build();
        }
        if (editProfileCommand.getFname() != null) {
            editModel.setFname(editProfileCommand.getFname());
        }
        if (editProfileCommand.getLname() != null) {
            editModel.setLname(editProfileCommand.getLname());
        }
        if (editProfileCommand.getEmail() != null) {
            editModel.setEmail(editProfileCommand.getEmail());
        }
        if (editProfileCommand.getRelation() != null) {
            editModel.setRelation(editProfileCommand.getRelation());
        }
        if (editProfileCommand.getCountryCode() != null) {
            editModel.setCountryCode(editProfileCommand.getCountryCode());
        }
        if(!editProfileCommand.getUserType().equals("Child") && editProfileCommand.getMobile() != null) {
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
