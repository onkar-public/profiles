package com.teamteach.profilemgmt.infra.persistence.repo;

import com.teamteach.profilemgmt.infra.persistence.entities.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProfileJPARepository extends JpaRepository<ProfileEntity, Long> {

    ProfileEntity findByProfileid(String pid);

    boolean existsByProfileid(String pid);

    @Query("from ProfileEntity p where p.parent.id = :parentid")
    Set<ProfileEntity> loadChildProfiles(String parentid);
}
