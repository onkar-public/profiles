package com.teamteach.profilemgmt.infra.api;

import com.teamteach.profilemgmt.domain.command.AddChildrenCommand;
import com.teamteach.profilemgmt.domain.command.BasicProfileCreationCommand;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Collections;

@RequestMapping("profiles")
public interface IProfileResource {

    @PostMapping("create")
    void createBasicProfile( @RequestBody @Valid BasicProfileCreationCommand userSignup);

    @ApiIgnore
    @PostMapping("{profileid}/picture")
    default void updateProfilePicture() {

    }

    @ApiIgnore
    @PostMapping("{profileid}/children")
    default void addChildren( @PathVariable String profileid, @RequestBody @Valid AddChildrenCommand addChildrenCommand) {

    }

    @ApiIgnore
    @PostMapping("{profileid}/picture")
    @GetMapping("{profileid}")
    default void getProfileById(@PathVariable String profileid) {

    }

}
