package com.teamteach.profilemgmt.shared.security.jwt;

import java.security.Key;

public interface ISecretKeyProvider {
    Key getKey();
}
