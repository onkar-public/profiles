package com.teamteach.profilemgmt.infra.rest;

import com.teamteach.profilemgmt.domain.ports.in.IProfileMgmt;
import com.teamteach.profilemgmt.infra.api.IProfileResource;
import com.teamteach.profilemgmt.shared.AbstractAppController;
import com.teamteach.profilemgmt.domain.command.BasicProfileCreationCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
class ProfileResource extends AbstractAppController implements IProfileResource {

    final IProfileMgmt profileMgmt;

    @Override
    public void createBasicProfile(@Valid BasicProfileCreationCommand profileCreationCommand) {
        profileMgmt.createBasicProfile(profileCreationCommand);
    }
}
