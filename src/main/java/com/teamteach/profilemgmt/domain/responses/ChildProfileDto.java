package com.teamteach.profilemgmt.domain.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ChildProfileDto {
    private String profileId;
    private String name;
    private String birthYear;
    private String info;
    private String profileImage;
    private String classYear;
    private String className;
    private String userType;
}
