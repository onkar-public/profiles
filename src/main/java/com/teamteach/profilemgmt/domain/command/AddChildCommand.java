package com.teamteach.profilemgmt.domain.command;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class AddChildCommand extends ValidatingCommand<BasicProfileCreationCommand>{
    @NotNull
    String parentId;
    String fname;
    String lname;
    String birthYear;
    String info;
}
