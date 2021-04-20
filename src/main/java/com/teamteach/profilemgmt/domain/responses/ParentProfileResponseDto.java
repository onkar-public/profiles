package com.teamteach.profilemgmt.domain.responses;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Builder
@Data
public class ParentProfileResponseDto {
    private String profileId;
    private String fname;
    private String lname;
    private String email;
    private String userType;
    private List<String> children;
    private String relation;
    private String mobile; 
}