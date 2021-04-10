package com.teamteach.profilemgmt.domain.models.vo;

public class ProfileImage {
    String mimetype;
    byte[] rawimage;

    public ProfileImage(String mimetype, byte[] contents) {
        this.mimetype = mimetype;
        this.rawimage = contents;
    }
}
