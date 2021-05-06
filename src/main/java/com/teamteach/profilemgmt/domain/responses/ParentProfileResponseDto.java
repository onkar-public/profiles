package com.teamteach.profilemgmt.domain.responses;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.*;

@Accessors(chain = true)
@Builder
@Data
public class ParentProfileResponseDto {
    private String profileId;
    private String fname;
    private String lname;
    private String email;
    private String userType;
    private List<ChildProfileDto> children;
    private String relation;
    private String mobile; 
    private String countryCode;
    private String timezone;
    private String profileImage;
    private String[] timezones;
   
}