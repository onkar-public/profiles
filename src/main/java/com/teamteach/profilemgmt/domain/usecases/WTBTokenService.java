package com.teamteach.profilemgmt.domain.usecases;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import java.io.IOException;

@Service 

public class WTBTokenService {
    RestTemplate restTemplate = new RestTemplate();
    private final String wtpAPIPortal = "https://forum.parentvillage.ml/get_token.php";
    private final String API_KEY = "MR2tNUFI2WZ9LpGbF34k2EsyXrnNTWNaBYPL5zK9";

    public String getWTBToken(String email) {
        HttpEntity <String> entity = new HttpEntity <> (null, null);
        try {
            String eurl = "https://parentvillage.websitetoolbox.com/register/setauthtoken?type=json&apikey=MR2tNUFI2WZ9LpGbF34k2EsyXrnNTWNaBYPL5zK9&email="+email;
            ResponseEntity<String> eresponse = restTemplate.exchange(eurl, HttpMethod.GET, entity, String.class);
            JsonNode respoJsonNode = new ObjectMapper().readTree(eresponse.getBody());
            return respoJsonNode.get("authtoken").asText();
        }
        catch (IOException e) {
            System.out.println("Exception while logging into websitetoolbox");
            return "";
        }
    }
}