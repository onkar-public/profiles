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
    private String relation;
    private String email;
    private String countryCode;
    private String callingCode;
    private String timezone;
    private String birthYear;
    private String info;
    private String classYear;
    private String className;
}