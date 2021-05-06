package com.teamteach.profilemgmt.domain.command;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class AddChildCommand extends ValidatingCommand<BasicProfileCreationCommand>{
    @NotNull
    private String ownerId;
    private String fname;
    private String lname;
    private String birthYear;
    private String info;
    private String profileImage;
}
