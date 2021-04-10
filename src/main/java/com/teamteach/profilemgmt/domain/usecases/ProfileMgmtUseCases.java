package com.teamteach.profilemgmt.domain.usecases;

import com.teamteach.profilemgmt.domain.mappers.ProfileCommandMapper;
import com.teamteach.profilemgmt.domain.ports.in.IProfileMgmt;
import com.teamteach.profilemgmt.domain.command.BasicProfileCreationCommand;
import com.teamteach.profilemgmt.domain.ports.out.IProfileRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProfileMgmtUseCases implements IProfileMgmt {

    final IProfileRepository profileRepository;
    final ProfileCommandMapper profileCommandMapper;

    @Override
    public void createBasicProfile(BasicProfileCreationCommand signUpCommand) {

    }
}
