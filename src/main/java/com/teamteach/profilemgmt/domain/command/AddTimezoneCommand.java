package com.teamteach.profilemgmt.domain.command;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class AddTimezoneCommand {
    @NotNull
    private String country;
    private String timezone;
}