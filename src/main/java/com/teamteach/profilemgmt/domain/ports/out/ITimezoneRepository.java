package com.teamteach.profilemgmt.domain.ports.out;

import java.util.HashMap;
import java.util.List;

import com.teamteach.profilemgmt.domain.models.TimezoneModel;

public interface ITimezoneRepository {
   TimezoneModel saveTimezone(TimezoneModel timezoneModel);
   boolean removeTimezone(String timezoneId);
   List<TimezoneModel> getTimezone(HashMap<String,String> searchCriteria, HashMap<String,String> excludeCriteria);
}
