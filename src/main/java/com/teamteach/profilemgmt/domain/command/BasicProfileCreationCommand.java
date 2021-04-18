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
    String fName;
    String lName;
    @NotNull
    String userId;
    String profileType;
    @Override
    protected void validateSelf() {
        super.validateSelf();
    }
}
