package com.teamteach.profilemgmt.infra.rest;

import com.teamteach.profilemgmt.domain.command.AddChildCommand;
import com.teamteach.profilemgmt.domain.ports.in.IProfileMgmt;
import com.teamteach.profilemgmt.infra.api.IProfileResource;
import com.teamteach.profilemgmt.shared.AbstractAppController;
import com.teamteach.profilemgmt.domain.command.BasicProfileCreationCommand;
import com.teamteach.profilemgmt.domain.models.ProfileModel;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
class ProfileResource extends AbstractAppController implements IProfileResource {

    final IProfileMgmt profileMgmt;

    @Override
//    @ApiOperation(value = "Creates the Basic Profile of an Individual", authorizations = { @Authorization(value="jwtToken") })
    public ResponseEntity<ProfileModel> createBasicProfile(@Valid BasicProfileCreationCommand profileCreationCommand) {
        return ResponseEntity.ok(profileMgmt.createBasicProfile(profileCreationCommand));
    }

    @Override
//    @ApiOperation(value = "Adds new children of an Individual (Parent)", authorizations = { @Authorization(value="jwtToken") })
    public void addChild(String profileid, @Valid AddChildCommand addChildCommand) {
        profileMgmt.addChild(profileid, addChildCommand);
    }
}
