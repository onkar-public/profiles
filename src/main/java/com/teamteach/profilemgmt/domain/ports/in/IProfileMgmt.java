package com.teamteach.profilemgmt.domain.ports.in;

import com.teamteach.profilemgmt.domain.command.AddChildCommand;
import com.teamteach.profilemgmt.domain.command.BasicProfileCreationCommand;
import com.teamteach.profilemgmt.domain.models.ProfileModel;

public interface IProfileMgmt {
    ProfileModel createBasicProfile(BasicProfileCreationCommand signUpCommand);
    void addChild(String parentProfileId, AddChildCommand addChildCommand);
}
