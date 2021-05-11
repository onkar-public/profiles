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
    @ApiIgnore
    @ApiOperation(value = "Creates the Basic Profile of an Individual", authorizations = { @Authorization(value="jwtToken") })
    public ResponseEntity<ObjectResponseDto> createBasicProfile(@Valid BasicProfileCreationCommand profileCreationCommand) {
        return ResponseEntity.ok(profileMgmt.createBasicProfile(profileCreationCommand));
    }

    // @Override
    // @ApiOperation(value = "Adds new child of a Parent", authorizations = { @Authorization(value="jwtToken") })
    // public ResponseEntity<ObjectResponseDto> addChild(AddChildCommand addChildCommand) {
    //     ObjectResponseDto childProfile = profileMgmt.addChild(addChildCommand);
    //     return ResponseEntity.ok(childProfile);
    // }

    @Override
    public ResponseEntity<ObjectResponseDto> getProfile(String ownerId) {
        ParentProfileResponseDto parentProfile = profileMgmt.getProfile(ownerId);
        if (parentProfile == null) {
            return ResponseEntity.ok(new ObjectResponseDto(false, "Parent profile not found", null));
        } else {
            return ResponseEntity.ok(new ObjectResponseDto(true, "Parent profile retrieved", parentProfile));
        }
    }

    @Override
    public ResponseEntity<ObjectResponseDto> editProfile(String profileId, EditProfileCommand editProfileCommand) {
		return ResponseEntity.ok(profileMgmt.editProfile(profileId,editProfileCommand));
    }

    @Override
    @ApiOperation(value = "Adds new child of a Parent", authorizations = { @Authorization(value="jwtToken") })
    public ResponseEntity<ObjectResponseDto> addChild( String ownerId, 
                                                        MultipartFile profileImage,
                                                        String fname,
                                                        String lname,
                                                        String birthYear,
                                                        String info) {
        AddChildCommand addChildCommand = new AddChildCommand(ownerId,fname,lname,birthYear,info,profileImage);                                                           
		return ResponseEntity.ok(profileMgmt.addChild(addChildCommand));
    }

    @Override
    public ResponseEntity<ObjectResponseDto> editProfileImage(String profileId, MultipartFile file) {
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

