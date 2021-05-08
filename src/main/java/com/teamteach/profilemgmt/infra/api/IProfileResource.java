package com.teamteach.profilemgmt.infra.api;

import com.teamteach.profilemgmt.domain.command.*;
import com.teamteach.profilemgmt.domain.command.BasicProfileCreationCommand;
import com.teamteach.profilemgmt.domain.responses.ObjectResponseDto;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RequestMapping("profiles")
public interface IProfileResource {

    @ApiIgnore
    @PostMapping("create")
    ResponseEntity<ObjectResponseDto> createBasicProfile( @RequestBody @Valid BasicProfileCreationCommand userSignup);

    @PutMapping("{profileId}")
    ResponseEntity<ObjectResponseDto> editProfile(@PathVariable String profileId, @RequestBody EditProfileCommand editProfileCommand);

    @PostMapping("child/{ownerId}")
    ResponseEntity<ObjectResponseDto> addChild( @PathVariable String ownerId, 
                                                @RequestParam("profileImage") MultipartFile file,
                                                @RequestParam("fname") String fname,
                                                @RequestParam("lname") String lname,
                                                @RequestParam("birthYear") String birthYear,
                                                @RequestParam("info") String info);

    // @PostMapping("child")
    // ResponseEntity<ObjectResponseDto> addChild(@RequestBody AddChildCommand addChildCommand);

    @PostMapping("picture/{profileId}")
    ResponseEntity<ObjectResponseDto> editProfileImage(@PathVariable String profileId, @RequestParam("file") MultipartFile file);

    @GetMapping("owner/{ownerId}")
    ResponseEntity<ObjectResponseDto> getProfile(@PathVariable String ownerId);
}