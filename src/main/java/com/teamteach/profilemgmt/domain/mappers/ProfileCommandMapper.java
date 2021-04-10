package com.teamteach.profilemgmt.domain.mappers;

import com.teamteach.profilemgmt.domain.command.BasicProfileCreationCommand;
import com.teamteach.profilemgmt.domain.models.NamedIndividualModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class ProfileCommandMapper {

    @Mapping(target = "identity", source = "email")
    @Mapping(target = "usertype", ignore = true)
    public abstract NamedIndividualModel toNamedIndividualModel(BasicProfileCreationCommand profileCreationCommand);


}
