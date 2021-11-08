package com.teamteach.profilemgmt.infra.rest;

import com.teamteach.profilemgmt.domain.command.*;
import com.teamteach.profilemgmt.domain.ports.in.IProfileMgmt;
import com.teamteach.profilemgmt.domain.responses.*;
import com.teamteach.profilemgmt.infra.api.IProfileResource;
import com.teamteach.profilemgmt.shared.AbstractAppController;
import com.teamteach.profilemgmt.domain.models.*;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
class ProfileResource extends AbstractAppController implements IProfileResource {

    final IProfileMgmt profileMgmt;

    @Override
    public ResponseEntity<ObjectResponseDto> createBasicProfile(@Valid BasicProfileCreationCommand profileCreationCommand) {
        return ResponseEntity.ok(profileMgmt.createBasicProfile(profileCreationCommand));
    }

    @Override
    public ResponseEntity<ObjectResponseDto> getProfile(String ownerId) {
        ParentProfileResponseDto profile = profileMgmt.getProfile(ownerId);
        if (profile == null) {
            return ResponseEntity.ok(new ObjectResponseDto(false, "Profile not found", null));
        } else {
            return ResponseEntity.ok(new ObjectResponseDto(true, "Profile retrieved", profile));
        }
    }

    @Override
    public ResponseEntity<ObjectResponseDto> getWTBDetails(String ownerId) {
        WTBDetailsResponse wtbDetailsResponse = profileMgmt.getWTBDetails(ownerId);
        if (wtbDetailsResponse == null) {
            return ResponseEntity.ok(new ObjectResponseDto(false, "WTB profile not found", null));
        } else {
            return ResponseEntity.ok(new ObjectResponseDto(true, "WTB profile retrieved", wtbDetailsResponse));
        }
    }

    @Override
    public ResponseEntity<ObjectResponseDto> editProfile(String profileId, EditProfileCommand editProfileCommand) {
        System.out.println("Editing profile with id: " + profileId);
		return ResponseEntity.ok(profileMgmt.editProfile(profileId,editProfileCommand));
    }

    @Override
    public ResponseEntity<ObjectResponseDto> postProfile(String profileId, EditProfileCommand editProfileCommand) {
        System.out.println("Editing profile with id: " + profileId);
		return ResponseEntity.ok(profileMgmt.editProfile(profileId,editProfileCommand));
    }

    @Override
    @ApiOperation(value = "Adds new child of a Parent", authorizations = { @Authorization(value="jwtToken") })
    public ResponseEntity<ObjectResponseDto> addChild( String ownerId, 
                                                        MultipartFile profileImage,
                                                        String fname,
                                                        String lname,
                                                        String birthYear,
                                                        String info,
                                                        String className,
                                                        String classYear,
                                                        String userType) {
        AddChildCommand addChildCommand = new AddChildCommand(ownerId,fname,lname,birthYear,info,profileImage,className,classYear,userType);                                                           
		return ResponseEntity.ok(profileMgmt.addChild(addChildCommand));
    }

    @Override
    public ResponseEntity<ObjectResponseDto> editProfileImage(String profileId, MultipartFile file) {
		System.out.println("Uploading image for: " + profileId);
        return ResponseEntity.ok(profileMgmt.saveTeamTeachFile(file, profileId));
    }

    @Override
    public ResponseEntity<ObjectResponseDto> addTimezone(AddTimezoneCommand addTimezoneCommand){
        return ResponseEntity.ok(profileMgmt.addTimezone(addTimezoneCommand));
    }

    @Override
    public ResponseEntity<ObjectResponseDto> deleteTimezone(String timezoneId){
        return ResponseEntity.ok(profileMgmt.deleteTimezone(timezoneId));
    }
}

