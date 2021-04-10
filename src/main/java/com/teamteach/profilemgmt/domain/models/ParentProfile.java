package com.teamteach.profilemgmt.domain.models;

import java.util.Collection;

public class ParentProfile extends ProfileModel{

    Collection<ProfileModel> children;

    public ParentProfile(NamedIndividualModel individual) {
        super(individual);
    }

    public void addChildren() {

    }

}
