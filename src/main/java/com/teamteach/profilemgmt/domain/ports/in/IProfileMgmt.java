package com.teamteach.profilemgmt.domain.ports.in;

import com.teamteach.profilemgmt.domain.command.*;
import com.teamteach.profilemgmt.domain.responses.*;

import org.springframework.web.multipart.MultipartFile;

public interface IProfileMgmt {
    ObjectResponseDto createBasicProfile(BasicProfileCreationCommand signUpCommand);
    ObjectResponseDto addChild(AddChildCommand addChildCommand);
    ParentProfileResponseDto getProfile(String ownerId);
    ObjectResponseDto editProfile(String profileId, EditProfileCommand editProfileCommand);
    String saveTeamTeachFile(MultipartFile file, String id);
}
