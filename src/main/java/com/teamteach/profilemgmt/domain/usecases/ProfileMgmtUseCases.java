package com.teamteach.profilemgmt.domain.usecases;

import com.teamteach.profilemgmt.domain.command.*;
import com.teamteach.profilemgmt.domain.ports.in.IProfileMgmt;
import com.teamteach.profilemgmt.domain.models.*;
import com.teamteach.profilemgmt.domain.models.vo.IndividualType;
import com.teamteach.profilemgmt.domain.ports.out.IProfileRepository;
import com.teamteach.commons.connectors.rabbit.core.IMessagingPort;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.teamteach.profilemgmt.domain.responses.*;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.multipart.MultipartFile;

import java.util.function.Consumer;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
public class ProfileMgmtUseCases implements IProfileMgmt {

    final IProfileRepository profileRepository;
    final IMessagingPort messagingPort;

    @Autowired
    private FileUploadService fileUploadService;

    @Value("${defaultImage}")
	private String default_image;

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
            if(userProfile.getAction().equals("signup")){
                createBasicProfile(userProfile);
            } else if(userProfile.getAction().equals("delete")){
                deleteProfile(userProfile);
            }
        }
    };

    @Override
    public ObjectResponseDto createBasicProfile(BasicProfileCreationCommand signUpCommand) {
        ProfileModel profileModel = ProfileModel.builder()
                                                .profileId(sequenceGeneratorService.generateSequence(ProfileModel.SEQUENCE_NAME))
                                                .ownerId(signUpCommand.getOwnerId())
                                                .fname(signUpCommand.getFname())
                                                .email("")
                                                .lname(signUpCommand.getLname())
                                                .userType(new IndividualType(ProfileTypes.Parent))
                                                .relation("")
                                                .profileImage(default_image)
                                                .mobile(signUpCommand.getMobile())
                                                .build();
        System.out.println(profileModel);                                      
        return new ObjectResponseDto(true, "Success", profileRepository.saveProfile(profileModel));
    }

    public ObjectResponseDto deleteProfile(BasicProfileCreationCommand delProfile){
        Query query = new Query(Criteria.where("ownerId").is(delProfile.getOwnerId()));
        ProfileModel profileModel = mongoTemplate.findOne(query, ProfileModel.class);
        if(profileModel != null){
            mongoTemplate.remove(query, ProfileModel.class);
            return ObjectResponseDto.builder()
                                    .success(true)
                                    .message("Profile deleted successfully")
                                    .build();
        } else{
            return ObjectResponseDto.builder()
                                    .success(false)
                                    .message("Profile not found")
                                    .build();
        }
    }

    // @Override
    // public ObjectResponseDto addChild(AddChildCommand addChildCommand) {
    //     if (addChildCommand.getOwnerId() == null || addChildCommand.getFname() == null) {
    //         return ObjectResponseDto.builder()
    //         .success(false)
    //         .message("Please provide at least the ownerId and fname in the requestBody")
    //         .object(addChildCommand)
    //         .build();
    //     }
    //     Query query = new Query(Criteria.where("ownerId").is(addChildCommand.getOwnerId()).and("fname").is(addChildCommand.getFname()));
    //     ProfileModel findChild = mongoTemplate.findOne(query, ProfileModel.class);
    //     if(findChild == null) {
    //         ProfileModel profileModel = ProfileModel.builder()
    //                                                 .profileId(sequenceGeneratorService.generateSequence(ProfileModel.SEQUENCE_NAME))
    //                                                 .ownerId(addChildCommand.getOwnerId())
    //                                                 .fname(addChildCommand.getFname())
    //                                                 .lname(addChildCommand.getLname())
    //                                                 .birthYear(addChildCommand.getBirthYear())
    //                                                 .info(addChildCommand.getInfo())
    //                                                 .userType(new IndividualType(ProfileTypes.Child))
    //                                                 .build();
    //         return new ObjectResponseDto(true, "Success", profileRepository.addChild(profileModel));
    //     } else {
    //         return new ObjectResponseDto(false, "Child with same name cannot be added", null);
    //     }

    // }

    @Override
    public ParentProfileResponseDto getProfile(String ownerId){
        Query query = new Query(Criteria.where("ownerId").is(ownerId).and("userType.type").is("Parent"));
        ProfileModel parentProfileModel = mongoTemplate.findOne(query, ProfileModel.class);
        if (parentProfileModel == null) return null;
        query = new Query(Criteria.where("ownerId").is(ownerId).and("userType.type").is("Child"));
        List<ProfileModel> children = mongoTemplate.find(query, ProfileModel.class);
        List<ChildProfileDto> childIdList = new ArrayList<>();
        for (ProfileModel child : children) {
            childIdList.add(new ChildProfileDto(child.getProfileId(), child.getFname(), child.getBirthYear(), child.getInfo(), child.getProfileImage()));
        }

        String[] timezones = getTimezones();
        System.out.println(timezones);

        ParentProfileResponseDto parentProfile = ParentProfileResponseDto.builder()
                                                                         .fname(parentProfileModel.getFname())
                                                                         .lname(parentProfileModel.getLname())
                                                                         .email(parentProfileModel.getEmail())
                                                                         .mobile(parentProfileModel.getMobile())
                                                                         .countryCode(parentProfileModel.getCountryCode())
                                                                         .timezone(parentProfileModel.getTimezone())
                                                                         .relation(parentProfileModel.getRelation())
                                                                         .children(childIdList)
                                                                         .userType(parentProfileModel.getUserType().getType().toString())
                                                                         .profileId(parentProfileModel.getProfileId())
                                                                         .profileImage(parentProfileModel.getProfileImage())
                                                                         .timezones(timezones)
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
        if(!editProfileCommand.getUserType().equals("Child")){
            if (editProfileCommand.getEmail() != null) {
                editModel.setEmail(editProfileCommand.getEmail());
            }
            if (editProfileCommand.getRelation() != null) {
                editModel.setRelation(editProfileCommand.getRelation());
            }
            if (editProfileCommand.getCountryCode() != null) {
                editModel.setCountryCode(editProfileCommand.getCountryCode());
            }
            if (editProfileCommand.getTimezone() != null) {
                editModel.setTimezone(editProfileCommand.getTimezone());
            }
            if(editProfileCommand.getMobile() != null) {
                editModel.setMobile(editProfileCommand.getMobile());
            }
        } else {
            if (editProfileCommand.getBirthYear() != null) {
                editModel.setBirthYear(editProfileCommand.getBirthYear());
            }
            if (editProfileCommand.getInfo() != null) {
                editModel.setInfo(editProfileCommand.getInfo());
            }
        }
        
        mongoTemplate.save(editModel);
        return ObjectResponseDto.builder()
                                .success(true)
                                .message("Profile edited successfully")
                                .object(editModel)
                                .build();
    }

    @Override
    public ObjectResponseDto addChild(AddChildCommand addChildCommand) {
        if (addChildCommand.getOwnerId() == null || addChildCommand.getFname() == null) {
            return ObjectResponseDto.builder()
            .success(false)
            .message("Please provide at least the ownerId and fname in the requestBody")
            .build();
        }
        Query query = new Query(Criteria.where("ownerId").is(addChildCommand.getOwnerId()).and("fname").is(addChildCommand.getFname()));
        ProfileModel findChild = mongoTemplate.findOne(query, ProfileModel.class);
        ProfileModel profileModel = null;
        String profileId = sequenceGeneratorService.generateSequence(ProfileModel.SEQUENCE_NAME);
        if(findChild == null) {
            profileModel = ProfileModel.builder()
                                        .profileId(profileId)
                                        .ownerId(addChildCommand.getOwnerId())
                                        .fname(addChildCommand.getFname())
                                        .lname(addChildCommand.getLname())
                                        .birthYear(addChildCommand.getBirthYear())
                                        .info(addChildCommand.getInfo())
                                        .userType(new IndividualType(ProfileTypes.Child))
                                        .build();
            String url = null;
            if(addChildCommand.getProfileImage() != null){
                try {
                    String fileExt = FilenameUtils.getExtension(addChildCommand.getProfileImage().getOriginalFilename()).replaceAll("\\s", "");
                    String fileName = "profile_"+profileId+"."+fileExt;
                    url = fileUploadService.saveTeamTeachFile("profileImages", fileName.replaceAll("\\s", ""), IOUtils.toByteArray(addChildCommand.getProfileImage().getInputStream()));
                } catch (IOException ioe) {
                    return ObjectResponseDto.builder()
                                            .success(false)
                                            .message(ioe.getMessage())
                                            .build();
                }
                profileModel.setProfileImage(url);
            }            
        } else {
            return new ObjectResponseDto(false, "Child with same name cannot be added", null);
        }   
        mongoTemplate.save(profileModel);
        return ObjectResponseDto.builder()
                                .success(true)
                                .message("Profile image added successfully")
                                .object(profileModel)
                                .build();
    }

    @Override
    public ObjectResponseDto saveTeamTeachFile(MultipartFile file, String id) {
        Query query = new Query(Criteria.where("profileId").is(id));
        ProfileModel pictureModel = mongoTemplate.findOne(query, ProfileModel.class);
        if (pictureModel == null) {
            return ObjectResponseDto.builder()
                                    .success(false)
                                    .message("No profile record found with given profileId")
                                    .object(pictureModel)
                                    .build();
        }
        String url = null;
        try {
            String fileExt = FilenameUtils.getExtension(file.getOriginalFilename()).replaceAll("\\s", "");
            String fileName = "profile_"+id+"."+fileExt;
            url = fileUploadService.saveTeamTeachFile("profileImages", fileName.replaceAll("\\s", ""), IOUtils.toByteArray(file.getInputStream()));
        } catch (IOException ioe) {
            return ObjectResponseDto.builder()
                                    .success(false)
                                    .message(ioe.getMessage())
                                    .build();
        }
        pictureModel.setProfileImage(url);
        mongoTemplate.save(pictureModel);
        return ObjectResponseDto.builder()
                                .success(true)
                                .message("Profile image added successfully")
                                .object(pictureModel)
                                .build();
    }

    @Override
    public ObjectResponseDto addTimezone(AddTimezoneCommand addTimezoneCommand){
        Query query = new Query(Criteria.where("timezone").is(addTimezoneCommand.getTimezone()));
        TimezoneModel timezoneModel = mongoTemplate.findOne(query, TimezoneModel.class);
        if(timezoneModel == null){
            timezoneModel = TimezoneModel.builder()
                                         .timezoneId(sequenceGeneratorService.generateSequence(TimezoneModel.SEQUENCE_NAME))
                                         .country(addTimezoneCommand.getCountry())
                                         .timezone(addTimezoneCommand.getTimezone())
                                         .build();
            mongoTemplate.save(timezoneModel);                                           
            return ObjectResponseDto.builder()
                                    .success(true)                                  
                                    .message("Timezones added successfully")
                                    .object(timezoneModel)
                                    .build();
        } else {
            return ObjectResponseDto.builder()
                                    .success(true)                                  
                                    .message("This timezone already ezists")
                                    .object(timezoneModel)
                                    .build();
        }
    }

    @Override
    public ObjectResponseDto deleteTimezone(String timezoneId){
        Query query = new Query(Criteria.where("timezoneId").is(timezoneId));
        TimezoneModel timezoneModel = mongoTemplate.findOne(query, TimezoneModel.class);
        if(timezoneModel != null){
            mongoTemplate.remove(query, TimezoneModel.class);
            return ObjectResponseDto.builder()
                                    .success(true)                                  
                                    .message("Timezone deleted successfully")
                                    .build();
        } else {
            return ObjectResponseDto.builder()
                                    .success(false)                                  
                                    .message("Timezone not found")
                                    .build();
        }
    }

    public String[] getTimezones(){
        Query query = new Query();
        int i=0;
        List<TimezoneModel> allZones = mongoTemplate.find(query,TimezoneModel.class);
        String[] timezones = new String[allZones.size()];
        for(TimezoneModel zone: allZones){
            timezones[i] = zone.getTimezone();
            i++;
        }
        return timezones;
    }
}
