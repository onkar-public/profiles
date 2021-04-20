package com.teamteach.profilemgmt.domain.command;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class EditProfileCommand extends ValidatingCommand<BasicProfileCreationCommand> {
    private String mobile;
    private String fname;
    private String lname;
    private String userType;
}