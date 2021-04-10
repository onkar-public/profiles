package com.teamteach.profilemgmt.infra.api;

import com.teamteach.profilemgmt.domain.command.BasicProfileCreationCommand;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;

@RequestMapping("profiles")
public interface IProfileResource {

    @PostMapping("basic")
    void createBasicProfile( @RequestBody @Valid BasicProfileCreationCommand userSignup);

    @PostMapping("{profileid}/picture")
    default void updateProfilePicture() {

    }

    @GetMapping("{profileid}")
    default void getProfileById(@PathVariable String profileid) {

    }

}
