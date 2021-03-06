package com.teamteach.profilemgmt.domain.usecases;

import com.teamteach.profilemgmt.domain.command.*;
import com.teamteach.profilemgmt.domain.ports.in.IProfileMgmt;
import com.teamteach.profilemgmt.domain.models.*;
import com.teamteach.profilemgmt.domain.models.vo.IndividualType;
import com.teamteach.profilemgmt.domain.ports.out.IProfileRepository;
import com.teamteach.profilemgmt.domain.ports.out.ITimezoneRepository;
import com.teamteach.commons.connectors.rabbit.core.IMessagingPort;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.teamteach.profilemgmt.domain.responses.*;

import org.springframework.data.mongodb.core.MongoTemplate;
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
    final ITimezoneRepository timezoneRepository;
    final IMessagingPort messagingPort;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private WTBTokenService wtbTokenService;

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
            if(!userProfile.getProfiletype().equals("TEACHER")){
                messagingPort.sendMessage(userProfile, "event.createjournal");
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
                                                .relation("")
                                                .profileImage(default_image)
                                                .mobile(signUpCommand.getMobile())
                                                .build();
        if(signUpCommand.getProfiletype().equals("TEACHER")){
            profileModel.setUserType(new IndividualType(ProfileTypes.Teacher));
        }else{
            profileModel.setUserType(new IndividualType(ProfileTypes.Parent));
        }
        System.out.println("Creating " + profileModel.getUserType().getType().toString() + " for " + signUpCommand.getFname());
        return new ObjectResponseDto(true, "Success", profileRepository.saveProfile(profileModel));
    }

    public ObjectResponseDto deleteProfile(BasicProfileCreationCommand delProfile){
        boolean isRemoved = profileRepository.removeProfile(delProfile.getOwnerId());
        if(isRemoved == true){
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

    @Override
    public ParentProfileResponseDto getProfile(String ownerId, String status){
        HashMap<SearchKey,Object> searchCriteria = new HashMap<>();
        searchCriteria.put(new SearchKey("ownerId",true),ownerId);
        //searchCriteria.put(new SearchKey("userType.type",false),"Parent");
        List<ProfileModel> profiles = profileRepository.getProfile(searchCriteria, null);
        ProfileModel profileModel = profiles.isEmpty() ? null : profiles.get(0);
        if (profileModel == null) return null;
        searchCriteria = new HashMap<>();
        searchCriteria.put(new SearchKey("ownerId",true),ownerId);
        if(!status.equals("all")){
            searchCriteria.put(new SearchKey("active",false),status.equals("active"));
        }
        // searchCriteria.put(new SearchKey("userType.type",false),"Child");
        HashMap<String,String> excludeCriteria = new HashMap<>();
        excludeCriteria.put("userType.type","Parent");
        excludeCriteria.put("userType.type","Teacher");
        List<ProfileModel> children = profileRepository.getProfile(searchCriteria, excludeCriteria);
        List<ChildProfileDto> childIdList = new ArrayList<>();
        for (ProfileModel child : children) {
            String name = null ;
            if(child.getUserType().getType().toString().equals("Class")){
                name = child.getClassName();
                childIdList.add(new ChildProfileDto(child.getProfileId(), name, child.getBirthYear(), child.getInfo(), child.getProfileImage(),child.getClassYear(),null,child.getUserType().getType().toString()));
            }else{
                name = child.getFname();
                if (child.getLname() != null && !child.getLname().equals("undefined")) name += " " + child.getLname();
                childIdList.add(new ChildProfileDto(child.getProfileId(), name, child.getBirthYear(), child.getInfo(), child.getProfileImage(),child.getClassYear(),child.getClassName(),child.getUserType().getType().toString()));
            }
        }
    
        String[] timezones = getTimezones();

        ParentProfileResponseDto profile = ParentProfileResponseDto.builder()
                                                                         .fname(profileModel.getFname())
                                                                         .lname(profileModel.getLname())
                                                                         .email(profileModel.getEmail())
                                                                         .mobile(profileModel.getMobile())
                                                                         .countryCode(profileModel.getCountryCode())
                                                                         .callingCode(profileModel.getCallingCode())
                                                                         .timezone(profileModel.getTimezone())
                                                                         .relation(profileModel.getRelation())
                                                                         .children(childIdList)
                                                                         .userType(profileModel.getUserType().getType().toString())
                                                                         .profileId(profileModel.getProfileId())
                                                                         .profileImage(profileModel.getProfileImage())
                                                                         .timezones(timezones)
                                                                         .build();
        return profile;                                                                
    }

    @Override
    public WTBDetailsResponse getWTBDetails(String email) {
        return WTBDetailsResponse.builder()
                                .wtbToken(wtbTokenService.getWTBToken(email))
                                .build();        
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
        HashMap<SearchKey,Object> searchCriteria = new HashMap<>();
        searchCriteria.put(new SearchKey("profileId",false),profileId);
        List<ProfileModel> profiles = profileRepository.getProfile(searchCriteria, null);
        ProfileModel editModel = profiles.isEmpty() ? null : profiles.get(0);
        if (editModel == null) {
            return ObjectResponseDto.builder()
                                    .success(false)
                                    .message("No profile record found with given profileId and userType")
                                    .object(editModel)
                                    .build();
        }
        if (editProfileCommand.getFname() != null) {
            if (editProfileCommand.getUserType().equals("Child")) {
                searchCriteria = new HashMap<>();
                searchCriteria.put(new SearchKey("ownerId",true),editModel.getOwnerId());
                searchCriteria.put(new SearchKey("fname",true),editProfileCommand.getFname());
                HashMap<String,String> excludeCriteria = new HashMap<>();
                excludeCriteria.put("profileId",profileId);
                profiles = profileRepository.getProfile(searchCriteria, excludeCriteria);
                ProfileModel findChild = profiles.isEmpty() ? null : profiles.get(0);
                if(findChild != null) {
                    return ObjectResponseDto.builder()
                    .success(false)
                    .message("A parent can not have 2 children with same first name")
                    .object(editModel)
                    .build();
                }                    
            }
            if(!editProfileCommand.getUserType().equals("Class")){
                editModel.setFname(editProfileCommand.getFname());
            }
        }
        if (editProfileCommand.getLname() != null) {
            if(!editProfileCommand.getUserType().equals("Class")){
                editModel.setLname(editProfileCommand.getLname());
            }
        }
        if (editProfileCommand.getUserType() != null) {
            editModel.setUserType(IndividualType.createType(editProfileCommand.getUserType()));
        }
        if(editProfileCommand.getUserType().equals("Teacher") || editProfileCommand.getUserType().equals("Parent")){
            if (editProfileCommand.getEmail() != null) {
                editModel.setEmail(editProfileCommand.getEmail());
            }
            if (editProfileCommand.getRelation() != null) {
                editModel.setRelation(editProfileCommand.getRelation());
            }
            if (editProfileCommand.getCountryCode() != null) {
                editModel.setCountryCode(editProfileCommand.getCountryCode());
            }
            if (editProfileCommand.getCallingCode() != null) {
                editModel.setCallingCode(editProfileCommand.getCallingCode());
            }
            if (editProfileCommand.getTimezone() != null) {
                editModel.setTimezone(editProfileCommand.getTimezone());
            }
            if(editProfileCommand.getMobile() != null) {
                editModel.setMobile(editProfileCommand.getMobile());
            }
        } else{
            if (editProfileCommand.getInfo() != null) {
                editModel.setInfo(editProfileCommand.getInfo());
            }
        }
         if(editProfileCommand.getUserType().equals("Child")) {
            if (editProfileCommand.getBirthYear() != null) {
                editModel.setBirthYear(editProfileCommand.getBirthYear());
            }
        }
        if(editProfileCommand.getUserType().equals("Class") || editProfileCommand.getUserType().equals("Student")){
            if (editProfileCommand.getClassYear() != null) {
                editModel.setClassYear(editProfileCommand.getClassYear());
            }
            if (editProfileCommand.getClassName() != null) {
                editModel.setClassName(editProfileCommand.getClassName());
            } 
            if(editProfileCommand.getActive() != null){
                if(editProfileCommand.getActive()){
                    HashMap<SearchKey,Object> validationCriteria = new HashMap<>();
                    validationCriteria.put(new SearchKey("ownerId",true),editModel.getOwnerId());
                    validationCriteria.put(new SearchKey("userType.type",false),editModel.getUserType().getType().toString());
                    validationCriteria.put(new SearchKey("active",false),true);
                    List<ProfileModel> validationProfiles = profileRepository.getProfile(validationCriteria, null);
                    if(validationProfiles.size() > 9){
                        return ObjectResponseDto.builder()
                        .success(false)
                        .message("You cannot add more than 10 "+ editModel.getUserType().getType().toString())
                        .build(); 
                    }
                }
                editModel.setActive(editProfileCommand.getActive());
            }
        }
        profileRepository.saveProfile(editModel);
        return ObjectResponseDto.builder()
                                .success(true)
                                .message(editProfileCommand.getUserType()+" profile updated successfully")
                                .object(editModel)
                                .build();
    }

    @Override
    public ObjectResponseDto addChild(AddChildCommand addChildCommand) {
        if(addChildCommand.getUserType().equals("Child")){
            if (addChildCommand.getOwnerId() == null || addChildCommand.getFname() == null) {
                return ObjectResponseDto.builder()
                .success(false)
                .message("Please provide at least the ownerId and fname in the requestBody")
                .build();
            }
        } else if(addChildCommand.getUserType().equals("Student") || addChildCommand.getUserType().equals("Class")){
            if(addChildCommand.getOwnerId() == null || addChildCommand.getClassName() == null || addChildCommand.getClassYear() == null){
                return ObjectResponseDto.builder()
                .success(false)
                .message("Please provide at ownerId , className and classYear in the requestBody")
                .build();
            }
            HashMap<SearchKey,Object> validationCriteria = new HashMap<>();
            validationCriteria.put(new SearchKey("ownerId",true),addChildCommand.getOwnerId());
            validationCriteria.put(new SearchKey("userType.type",false),addChildCommand.getUserType());
            validationCriteria.put(new SearchKey("active",false),true);
            List<ProfileModel> validationProfiles = profileRepository.getProfile(validationCriteria, null);
            if(validationProfiles.size() > 9){
                return ObjectResponseDto.builder()
                .success(false)
                .message("You cannot add more than 10 "+ addChildCommand.getUserType())
                .build();
            }
        }else{
            return ObjectResponseDto.builder()
                .success(false)
                .message("Please provide proper userType in the requestBody")
                .build();
        }
        HashMap<SearchKey,Object> searchCriteria = new HashMap<>();
        searchCriteria.put(new SearchKey("ownerId",true),addChildCommand.getOwnerId());
        searchCriteria.put(new SearchKey("fname",true),addChildCommand.getFname());  
        ProfileModel findChild = null ;
        if(addChildCommand.getUserType().equals("Child")){
            List<ProfileModel> profiles = profileRepository.getProfile(searchCriteria, null);
            findChild = profiles.isEmpty() ? null : profiles.get(0);
        }
        ProfileModel profileModel = null;
        String profileId = sequenceGeneratorService.generateSequence(ProfileModel.SEQUENCE_NAME);
        if(findChild == null) {
            if(addChildCommand.getUserType().equals("Child")){
                profileModel = ProfileModel.builder()
                                        .profileId(profileId)
                                        .ownerId(addChildCommand.getOwnerId())
                                        .fname(addChildCommand.getFname())
                                        .lname(addChildCommand.getLname())
                                        .birthYear(addChildCommand.getBirthYear())
                                        .info(addChildCommand.getInfo())
                                        .userType(new IndividualType(ProfileTypes.Child))
                                        .build();
            }else if(addChildCommand.getUserType().equals("Student")){
                profileModel = ProfileModel.builder()
                                           .profileId(profileId)
                                           .ownerId(addChildCommand.getOwnerId())
                                           .fname(addChildCommand.getFname())
                                           .lname(addChildCommand.getLname())
                                           .info(addChildCommand.getInfo())
                                           .className(addChildCommand.getClassName())
                                           .classYear(addChildCommand.getClassYear())
                                           .userType(new IndividualType(ProfileTypes.Student))
                                           .active(true)
                                           .build();
            } else if(addChildCommand.getUserType().equals("Class")){
                profileModel = ProfileModel.builder()
                                            .profileId(profileId)
                                            .ownerId(addChildCommand.getOwnerId())
                                            .info(addChildCommand.getInfo())
                                            .className(addChildCommand.getClassName())
                                            .classYear(addChildCommand.getClassYear())
                                            .userType(new IndividualType(ProfileTypes.Class))
                                            .active(true)
                                            .build();
            }
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
            } else{
                profileModel.setProfileImage(default_image);
            }         
        } else {
            return new ObjectResponseDto(false, "Profile with same name cannot be added", null);
        }   
        profileRepository.saveProfile(profileModel);
        return ObjectResponseDto.builder()
                                .success(true)
                                .message("Profile added successfully")
                                .object(profileModel)
                                .build();
    }

    @Override
    public ObjectResponseDto saveTeamTeachFile(MultipartFile file, String id) {
        HashMap<SearchKey,Object> searchCriteria = new HashMap<>();
        searchCriteria.put(new SearchKey("profileId",false),id);
        List<ProfileModel> profiles = profileRepository.getProfile(searchCriteria, null);
        ProfileModel pictureModel = profiles.isEmpty() ? null : profiles.get(0);
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
        profileRepository.saveProfile(pictureModel);
        return ObjectResponseDto.builder()
                                .success(true)
                                .message("Profile image added successfully")
                                .object(pictureModel)
                                .build();
    }

    @Override
    public ObjectResponseDto addTimezone(AddTimezoneCommand addTimezoneCommand){
        HashMap<String,String> searchCriteria = new HashMap<>();
        searchCriteria.put("timezone",addTimezoneCommand.getTimezone());
        List<TimezoneModel> timezones = timezoneRepository.getTimezone(searchCriteria, null);
        TimezoneModel timezoneModel = timezones.isEmpty() ? null : timezones.get(0);
        if(timezoneModel == null){
            timezoneModel = TimezoneModel.builder()
                                         .timezoneId(sequenceGeneratorService.generateSequence(TimezoneModel.SEQUENCE_NAME))
                                         .country(addTimezoneCommand.getCountry())
                                         .timezone(addTimezoneCommand.getTimezone())
                                         .build();
            timezoneRepository.saveTimezone(timezoneModel);                                           
            return ObjectResponseDto.builder()
                                    .success(true)                                  
                                    .message("Timezones added successfully")
                                    .object(timezoneModel)
                                    .build();
        } else {
            return ObjectResponseDto.builder()
                                    .success(true)                                  
                                    .message("This timezone already exists")
                                    .object(timezoneModel)
                                    .build();
        }
    }

    @Override
    public ObjectResponseDto deleteTimezone(String timezoneId){
        boolean isRemoved = timezoneRepository.removeTimezone(timezoneId);
        if(isRemoved == true){
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
        List<TimezoneModel> allZones = timezoneRepository.getTimezone(null, null);
        String[] timezones = allZones.stream().map(z -> z.getTimezone()).toArray(size -> new String[allZones.size()]);        
        return timezones;
    }
}
