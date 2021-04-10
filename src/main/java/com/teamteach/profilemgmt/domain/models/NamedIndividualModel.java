package com.teamteach.profilemgmt.domain.models;

import com.teamteach.profilemgmt.domain.models.vo.IndividualType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NamedIndividualModel {
    protected String fname;
    protected String lname;
    protected String identity;
    protected IndividualType usertype;
}
