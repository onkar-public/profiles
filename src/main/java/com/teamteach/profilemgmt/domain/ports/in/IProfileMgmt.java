package com.teamteach.profilemgmt.domain.ports.in;

import com.teamteach.profilemgmt.domain.command.AddChildrenCommand;
import com.teamteach.profilemgmt.domain.command.BasicProfileCreationCommand;

public interface IProfileMgmt {
    void createBasicProfile(BasicProfileCreationCommand signUpCommand);
    void addChildren(String parentrofileId, AddChildrenCommand addChildrenCommand);
}
