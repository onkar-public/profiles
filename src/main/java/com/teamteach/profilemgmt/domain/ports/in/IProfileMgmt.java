package com.teamteach.profilemgmt.domain.ports.in;

import com.teamteach.profilemgmt.domain.command.*;
import com.teamteach.profilemgmt.domain.responses.*;

public interface IProfileMgmt {
    ObjectResponseDto createBasicProfile(BasicProfileCreationCommand signUpCommand);
    ObjectResponseDto addChild(AddChildCommand addChildCommand);
    ParentProfileResponseDto getProfile(String ownerId);
    ObjectResponseDto editProfile(String profileId, EditProfileCommand editProfileCommand);
}
