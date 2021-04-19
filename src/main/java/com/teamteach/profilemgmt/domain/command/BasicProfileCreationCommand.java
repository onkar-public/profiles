package com.teamteach.profilemgmt.domain.command;

import lombok.Getter;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class BasicProfileCreationCommand extends ValidatingCommand<BasicProfileCreationCommand> {

    @NotNull
    String fname;
    String lname;
    @NotNull
    String userId;
    String profileType;
    String relation;
    @Override
    protected void validateSelf() {
        super.validateSelf();
    }
}
