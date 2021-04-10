package com.teamteach.profilemgmt.domain.command;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class BasicProfileCreationCommand extends ValidatingCommand<BasicProfileCreationCommand> {

    @NotNull
    String fname;
    String lname;
    @NotNull
    String email;
    String profiletype;
    @Override
    protected void validateSelf() {
        super.validateSelf();
    }
}
