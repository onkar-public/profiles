package com.teamteach.profilemgmt.domain.ports.in;

import com.teamteach.profilemgmt.domain.command.*;
import com.teamteach.profilemgmt.domain.models.ProfileModel;
import com.teamteach.profilemgmt.domain.responses.*;

public interface IProfileMgmt {
    ProfileModel createBasicProfile(BasicProfileCreationCommand signUpCommand);
    ProfileModel addChild(AddChildCommand addChildCommand);
    ParentProfileResponseDto getProfile(String userId);
    ObjectResponseDto editProfile(String profileId, EditProfileCommand editProfileCommand);
}
