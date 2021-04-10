package com.teamteach.profilemgmt.infra.persistence.entities;

import com.teamteach.profilemgmt.domain.models.ProfileTypes;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "profiles")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ProfileEntity extends AbstractBaseEntity{

    @EqualsAndHashCode.Include
    private String profileid;
    private String email;
    private String fname;
    private String lname;

    @Enumerated(EnumType.STRING)
    private ProfileTypes type;

    @ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="parent_id")
    private ProfileEntity parent;

    @OneToMany(mappedBy="parent")
    private Set<ProfileEntity> children = new HashSet<>();

}
