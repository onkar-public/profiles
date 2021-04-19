package com.teamteach.profilemgmt.domain.responses;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import com.teamteach.profilemgmt.domain.models.*;
import com.teamteach.profilemgmt.domain.models.vo.IndividualType;

@Accessors(chain = true)
@Builder
@Data
public class ParentProfileResponseDto {
    private String fname;
    private String lname;
    private String email;
    private IndividualType userType;
    private String[] children;
    private String relation;
    private String mobile; 
}