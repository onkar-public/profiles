package com.teamteach.profilemgmt.domain.command;


import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class EditProfileCommand extends ValidatingCommand<BasicProfileCreationCommand> {
    private String mobile;
    private String fname;
    private String lname;
    private String userType;
}