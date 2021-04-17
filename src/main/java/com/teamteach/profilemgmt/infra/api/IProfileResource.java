package com.teamteach.profilemgmt.infra.api;

import com.teamteach.profilemgmt.domain.command.AddChildrenCommand;
import com.teamteach.profilemgmt.domain.command.BasicProfileCreationCommand;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;

@RequestMapping("profiles")
public interface IProfileResource {

    @PostMapping("create")
    void createBasicProfile( @RequestBody @Valid BasicProfileCreationCommand userSignup);

    @PostMapping("{profileid}/picture")
    default void updateProfilePicture() {

    }

    @PostMapping("{profileid}/children")
    default void addChildren( @PathVariable String profileid, @RequestBody @Valid AddChildrenCommand addChildrenCommand) {

    }

    @GetMapping("{profileid}")
    default void getProfileById(@PathVariable String profileid) {

    }

}
