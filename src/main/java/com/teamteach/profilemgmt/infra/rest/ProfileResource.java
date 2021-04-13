package com.teamteach.profilemgmt.infra.rest;

import com.teamteach.profilemgmt.domain.command.AddChildrenCommand;
import com.teamteach.profilemgmt.domain.ports.in.IProfileMgmt;
import com.teamteach.profilemgmt.infra.api.IProfileResource;
import com.teamteach.profilemgmt.shared.AbstractAppController;
import com.teamteach.profilemgmt.domain.command.BasicProfileCreationCommand;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
class ProfileResource extends AbstractAppController implements IProfileResource {

    final IProfileMgmt profileMgmt;

    @Override
//    @ApiOperation(value = "Creates the Basic Profile of an Individual", authorizations = { @Authorization(value="jwtToken") })
    public void createBasicProfile(@Valid BasicProfileCreationCommand profileCreationCommand) {
        profileMgmt.createBasicProfile(profileCreationCommand);
    }

    @Override
//    @ApiOperation(value = "Adds new children of an Individual (Parent)", authorizations = { @Authorization(value="jwtToken") })
    public void addChildren(String profileid, @Valid AddChildrenCommand addChildrenCommand) {
        profileMgmt.addChildren(profileid,addChildrenCommand);
    }
}
