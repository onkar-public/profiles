package com.teamteach.profilemgmt.domain.models;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@Document(collection = "timezones")
public class TimezoneModel {
    
    @Transient
    public static final String SEQUENCE_NAME = "timezones_sequence";

    @Id
    private String timezoneId;
    private String country;
    private String timezone;
}
