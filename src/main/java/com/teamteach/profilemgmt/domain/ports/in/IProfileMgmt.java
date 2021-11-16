package com.teamteach.profilemgmt.domain.ports.in;

import com.teamteach.profilemgmt.domain.command.*;
import com.teamteach.profilemgmt.domain.models.ProfileModel;
import com.teamteach.profilemgmt.domain.responses.*;

import org.springframework.web.multipart.MultipartFile;

public interface IProfileMgmt {
    ObjectResponseDto createBasicProfile(BasicProfileCreationCommand signUpCommand);
    //ObjectResponseDto addChild(AddChildCommand addChildCommand);
    ParentProfileResponseDto getProfile(String ownerId,String status);
    WTBDetailsResponse getWTBDetails(String ownerId);
    ObjectResponseDto editProfile(String profileId, EditProfileCommand editProfileCommand);
    ObjectResponseDto addChild(AddChildCommand addChildCommand);
    ObjectResponseDto saveTeamTeachFile(MultipartFile file, String id);
    ObjectResponseDto addTimezone(AddTimezoneCommand addTimezoneCommand);
    ObjectResponseDto deleteTimezone(String timezoneId);
}
