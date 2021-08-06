package com.teamteach.profilemgmt.infra.persistence.dal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teamteach.profilemgmt.domain.models.TimezoneModel;
import com.teamteach.profilemgmt.domain.ports.out.ITimezoneRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Component
@RequiredArgsConstructor
public class TimezoneDAL  implements ITimezoneRepository {
    final MongoTemplate mongoTemplate;
    @Override
    public TimezoneModel saveTimezone(TimezoneModel timezoneModel) {
        TimezoneModel newModel = mongoTemplate.save(timezoneModel);
        return newModel;
    }
    @Override
    public boolean removeTimezone(String timezoneId){
        Query query = new Query(Criteria.where("timezoneId").is(timezoneId));
        List<TimezoneModel> timezones = mongoTemplate.findAllAndRemove(query, TimezoneModel.class);
        return timezones.isEmpty() ? false : true;
    }
    @Override
    public List<TimezoneModel> getTimezone(HashMap<String,String> searchCriteria, HashMap<String,String> excludeCriteria){
        Query query = new Query();
        if(searchCriteria != null){
            for(Map.Entry<String,String> criteria : searchCriteria.entrySet()){
                query.addCriteria(Criteria.where(criteria.getKey()).is(criteria.getValue()));
            }
        }
        if(excludeCriteria != null){
            for(Map.Entry<String,String> criteria : excludeCriteria.entrySet()){
                query.addCriteria(Criteria.where(criteria.getKey()).ne(criteria.getValue()));
            }
        }
        List<TimezoneModel> timezones = mongoTemplate.find(query,TimezoneModel.class);
        return timezones;
    } 
}