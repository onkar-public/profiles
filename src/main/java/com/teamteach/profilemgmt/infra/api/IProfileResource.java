package com.teamteach.profilemgmt.infra.api;

import com.teamteach.profilemgmt.domain.command.*;
import com.teamteach.profilemgmt.domain.command.BasicProfileCreationCommand;
import com.teamteach.profilemgmt.domain.models.ProfileModel;
import com.teamteach.profilemgmt.domain.responses.ObjectResponseDto;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RequestMapping("profiles")
public interface IProfileResource {

    @ApiIgnore
    @PostMapping("create")
    ResponseEntity<ProfileModel> createBasicProfile( @RequestBody @Valid BasicProfileCreationCommand userSignup);

    @ApiIgnore
    @PostMapping("{profileid}/picture")
    default void updateProfilePicture() {
    }

    @ApiIgnore
    @PutMapping("edit/{profileId}")
    ResponseEntity<ObjectResponseDto> editProfile(@PathVariable String profileId, @RequestBody EditProfileCommand editProfileCommand);

    @PostMapping("child")
    ResponseEntity<ObjectResponseDto> addChild(@RequestBody AddChildCommand addChildCommand);

    @GetMapping("{userId}")
    ResponseEntity<ObjectResponseDto> getProfile(@PathVariable String userId);
}
