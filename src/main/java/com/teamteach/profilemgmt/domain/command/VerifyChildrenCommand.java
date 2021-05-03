package com.teamteach.profilemgmt.domain.command;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
public class VerifyChildrenCommand extends ValidatingCommand<BasicProfileCreationCommand>{
    @NotNull
    private String ownerId;
    private String[] children;
}