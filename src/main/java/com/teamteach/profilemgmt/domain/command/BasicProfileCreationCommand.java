package com.teamteach.profilemgmt.domain.command;

import com.teamteach.profilemgmt.shared.models.Roles;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.util.Collection;

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
