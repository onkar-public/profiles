package com.teamteach.profilemgmt.infra.rest;

import com.teamteach.profilemgmt.domain.command.AddChildCommand;
import com.teamteach.profilemgmt.domain.ports.in.IProfileMgmt;
import com.teamteach.profilemgmt.domain.responses.ObjectResponseDto;
import com.teamteach.profilemgmt.domain.responses.ParentProfileResponseDto;
import com.teamteach.profilemgmt.infra.api.IProfileResource;
import com.teamteach.profilemgmt.shared.AbstractAppController;
import com.teamteach.profilemgmt.domain.command.BasicProfileCreationCommand;
import com.teamteach.profilemgmt.domain.command.EditProfileCommand;
import com.teamteach.profilemgmt.domain.models.ProfileModel;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
class ProfileResource extends AbstractAppController implements IProfileResource {

    final IProfileMgmt profileMgmt;

    @Override
    @ApiIgnore
    @ApiOperation(value = "Creates the Basic Profile of an Individual", authorizations = { @Authorization(value="jwtToken") })
    public ResponseEntity<ProfileModel> createBasicProfile(@Valid BasicProfileCreationCommand profileCreationCommand) {
        return ResponseEntity.ok(profileMgmt.createBasicProfile(profileCreationCommand));
    }

    @Override
    @ApiOperation(value = "Adds new child of a Parent", authorizations = { @Authorization(value="jwtToken") })
    public ResponseEntity<ObjectResponseDto> addChild(AddChildCommand addChildCommand) {
        System.out.println(addChildCommand.getFname() + " " + addChildCommand.getLname());
        ProfileModel childProfile = profileMgmt.addChild(addChildCommand);
        if (childProfile.getFname() == null || childProfile.getFname() == "" ) {
            return ResponseEntity.ok(new ObjectResponseDto(false, "Child addition failed", addChildCommand));
        } else {
            return ResponseEntity.ok(new ObjectResponseDto(true, "Child added", childProfile));
        }
    }

    @Override
    public ResponseEntity<ObjectResponseDto> getProfile(String userId) {
        ParentProfileResponseDto parentProfile = profileMgmt.getProfile(userId);
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
}
