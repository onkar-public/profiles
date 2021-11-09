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
    private String profiletype;
    private String relation;
    private String mobile;
    private String action;
    @Override
    protected void validateSelf() {
        super.validateSelf();
    }
}
