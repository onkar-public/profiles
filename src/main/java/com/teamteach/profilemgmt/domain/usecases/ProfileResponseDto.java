package com.teamteach.profilemgmt.domain.usecases;

import com.teamteach.profilemgmt.domain.models.ProfileModel;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Data
public class ProfileResponseDto{
    ProfileModel parentProfile;
    List<ProfileModel> children;
    
    private final boolean success;
    private final String message;

    public ProfileResponseDto(){
        this.success = false;
        this.message = "";
    }

    @Builder
    public ProfileResponseDto(String parentId) {
        this.success=true;
        this.message="sent";
    }
}