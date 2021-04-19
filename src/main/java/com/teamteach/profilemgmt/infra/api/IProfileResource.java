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

    @PostMapping("create")
    ResponseEntity<ProfileModel> createBasicProfile( @RequestBody @Valid BasicProfileCreationCommand userSignup);

    @ApiIgnore
    @PostMapping("{profileid}/picture")
    default void updateProfilePicture() {

    }

    @ApiIgnore
    @PostMapping("child")
    ResponseEntity<ObjectResponseDto> addChild(@RequestBody @Valid AddChildCommand addChildCommand);

    @ApiIgnore
    @GetMapping("{profileid}")
    default void getProfileById(@PathVariable String profileid) {

    }

}
