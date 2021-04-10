package com.teamteach.profilemgmt.infra.api;

import com.teamteach.profilemgmt.domain.command.BasicProfileCreationCommand;
import com.teamteach.profilemgmt.shared.models.PagedResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;

@RequestMapping("profiles")
public interface IProfileResource {

    @PostMapping("basic")
    void createBasicProfile( @RequestBody @Valid BasicProfileCreationCommand userSignup);

}
