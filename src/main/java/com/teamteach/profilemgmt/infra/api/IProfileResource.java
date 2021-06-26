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

    @ApiIgnore
    @PostMapping("{profileId}")
    ResponseEntity<ObjectResponseDto> postProfile(@PathVariable String profileId, @RequestBody EditProfileCommand editProfileCommand);

    @PostMapping("child/{ownerId}")
    ResponseEntity<ObjectResponseDto> addChild( @PathVariable String ownerId, 
                                                @RequestParam(value = "profileImage", required=false) MultipartFile file,
                                                @RequestParam(value = "fname", required=true) String fname,
                                                @RequestParam(value = "lname", required=false) String lname,
                                                @RequestParam(value = "birthYear", required=true) String birthYear,
                                                @RequestParam(value = "info", required=false) String info);

    @PostMapping("picture/{profileId}")
    ResponseEntity<ObjectResponseDto> editProfileImage(@PathVariable String profileId, @RequestParam("file") MultipartFile file);

    @GetMapping("owner/{ownerId}")
    ResponseEntity<ObjectResponseDto> getProfile(@PathVariable String ownerId);

    @GetMapping("wtb/{ownerId}")
    ResponseEntity<ObjectResponseDto> getWTBDetails(@PathVariable String ownerId);

    @ApiIgnore
    @PostMapping("timezones")
    ResponseEntity<ObjectResponseDto> addTimezone( @RequestBody AddTimezoneCommand addTimezoneCommand);

    @ApiIgnore
    @DeleteMapping("timezones/{timezoneId}")
    ResponseEntity<ObjectResponseDto> deleteTimezone(@PathVariable String timezoneId);
}