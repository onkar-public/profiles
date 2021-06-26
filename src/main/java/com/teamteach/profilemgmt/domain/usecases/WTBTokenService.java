package com.teamteach.profilemgmt.domain.usecases;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service 

public class WTBTokenService {
    RestTemplate restTemplate = new RestTemplate();
    private final String wtpAPIPortal = "https://forum.parentvillage.ml/get_token.php";
    private final String API_KEY = "MR2tNUFI2WZ9LpGbF34k2EsyXrnNTWNaBYPL5zK9";

    public String getWTBToken(String email) {
        String url = wtpAPIPortal + "?email=" + email;
        HttpEntity <String> entity = new HttpEntity <> (null, null);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println(response);
        return response.getBody().toString();
    }
}