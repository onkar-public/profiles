package com.teamteach.profilemgmt.infra.persistence.dal;

import com.teamteach.profilemgmt.domain.models.ProfileModel;
import com.teamteach.profilemgmt.domain.models.SearchKey;
import com.teamteach.profilemgmt.domain.ports.out.IProfileRepository;

import com.teamteach.commons.utils.AnonymizeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Component
@RequiredArgsConstructor
public class ProfileDAL  implements IProfileRepository {
    final MongoTemplate mongoTemplate;
    
    @Override
    public boolean profileExistsById(String profileId) {
        return true;
    }

    @Override
    public String setupInitialProfile(ProfileModel profileModel) {
        return null;
    }

    @Override
    public ProfileModel getProfileByProfileId(String profileId) {
        return null;
    }

    @Override
    public ProfileModel saveProfile(ProfileModel profileModel) {
        // profileModel.setFname(AnonymizeService.anonymizeData(profileModel.getFname()));
        // profileModel.setLname(AnonymizeService.anonymizeData(profileModel.getLname()));
        ProfileModel newModel = mongoTemplate.save(profileModel);
        return newModel;
    }

    @Override
    public ProfileModel addChild(ProfileModel profileModel){
        ProfileModel newChildModel = saveProfile(profileModel);
        return newChildModel;
    }

    @Override
    public boolean removeProfile(String ownerId){
        Query query = new Query(Criteria.where("ownerId").is(ownerId));
        List<ProfileModel> profiles = mongoTemplate.findAllAndRemove(query, ProfileModel.class);
        return profiles.isEmpty() ? false : true;
    }

    @Override
    public List<ProfileModel> getProfile(HashMap<SearchKey,String> searchCriteria, HashMap<String,String> excludeCriteria){
        Query query = new Query();
        if(searchCriteria != null){
            for(Map.Entry<SearchKey,String> criteria : searchCriteria.entrySet()){
                if(criteria.getKey().isAnonymizable()){
                    query.addCriteria(Criteria.where(criteria.getKey().getField()).is(AnonymizeService.anonymizeData(criteria.getValue())));
                } else{
                    query.addCriteria(Criteria.where(criteria.getKey().getField()).is(criteria.getValue()));
                }
            }
        }
        if(excludeCriteria != null){
            for(Map.Entry<String,String> criteria : excludeCriteria.entrySet()){
                query.addCriteria(Criteria.where(criteria.getKey()).ne(criteria.getValue()));
            }
        }
        List<ProfileModel> profiles = mongoTemplate.find(query,ProfileModel.class);
        // for(ProfileModel profileModel : profiles){
        //     profileModel.setFname(AnonymizeService.deAnonymizeData(profileModel.getFname()));
        //     profileModel.setLname(AnonymizeService.deAnonymizeData(profileModel.getLname()));
        // }
        return profiles;
    } 
}