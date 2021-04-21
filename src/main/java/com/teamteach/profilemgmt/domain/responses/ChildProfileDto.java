package com.teamteach.profilemgmt.domain.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ChildProfileDto {
    private String profileId;
    private String name;
}
