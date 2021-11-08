package com.teamteach.profilemgmt.domain.command;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class AddChildCommand extends ValidatingCommand<BasicProfileCreationCommand>{
    @NotNull
    private String ownerId;
    private String fname;
    private String lname;
    private String birthYear;
    private String info;
    private MultipartFile profileImage;
    private String className;
    private String classYear;
    private String userType;
}
