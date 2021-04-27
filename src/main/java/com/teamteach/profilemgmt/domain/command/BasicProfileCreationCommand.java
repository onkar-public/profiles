package com.teamteach.profilemgmt.domain.command;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
public class BasicProfileCreationCommand extends ValidatingCommand<BasicProfileCreationCommand> {

    @NotNull
    private String fname;
    private String lname;
    private String email;
    @NotNull
    private String ownerId;
    private String profileType;
    private String relation;
    private String mobile;
    @Override
    protected void validateSelf() {
        super.validateSelf();
    }
}
