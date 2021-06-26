package com.teamteach.profilemgmt.domain.responses;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.*;

@Accessors(chain = true)
@Builder
@Data
public class WTBDetailsResponse {
    private String wtbToken;
}