package com.teamteach.profilemgmt.domain.command;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class AddChildrenCommand extends ValidatingCommand<BasicProfileCreationCommand>{
    @NotNull
    String fname;
    String lname;
    int birthyear;
    String info;
}
