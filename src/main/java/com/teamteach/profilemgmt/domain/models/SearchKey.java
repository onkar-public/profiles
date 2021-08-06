package com.teamteach.profilemgmt.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchKey {
    private String field;
    private boolean anonymizable;
}
