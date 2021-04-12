package com.teamteach.profilemgmt.domain.usecases;

import com.teamteach.profilemgmt.domain.command.AddChildrenCommand;
import com.teamteach.profilemgmt.domain.ports.in.IProfileMgmt;
import com.teamteach.profilemgmt.domain.command.BasicProfileCreationCommand;
import com.teamteach.profilemgmt.domain.models.ProfileModel;
import com.teamteach.profilemgmt.domain.models.ProfileTypes;
import com.teamteach.profilemgmt.domain.models.vo.IndividualType;
import com.teamteach.profilemgmt.domain.ports.out.IProfileRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProfileMgmtUseCases implements IProfileMgmt {

    final IProfileRepository profileRepository;

    @Override
    public void createBasicProfile(BasicProfileCreationCommand signUpCommand) {
        ProfileModel profileModel = new ProfileModel(signUpCommand.getUserid(), signUpCommand.getFname(), signUpCommand.getLname(), new IndividualType(ProfileTypes.Parent));
        profileRepository.saveProfile(profileModel);
    }

    @Override
    public void addChildren(String parentrofileId, AddChildrenCommand addChildrenCommand) {
        // Validate that the Parent Profile Id

        // Check for duplicate children

        //Add the child profile
    }
}
