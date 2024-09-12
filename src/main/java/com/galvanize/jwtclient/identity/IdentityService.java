package com.galvanize.jwtclient.identity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class IdentityService {
    private static final Logger logger = LoggerFactory.getLogger(IdentityService.class);

    @Value("${identityurl}")
    private String IDENTITY_URL;
    private final RestTemplate restTemplate;

    public IdentityService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // For validation purposes
    public String getIdentityHealth() {
        return restTemplate.getForObject(IDENTITY_URL + "/actuator/health", String.class);
    }

    public IdentityRoleList getRoles(String token) {
        String url = IDENTITY_URL + "/api/admin/roles";
        ResponseEntity<IdentityRoleList> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, getEntity(token), IdentityRoleList.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                if(response.getStatusCode().is4xxClientError()){
                    logger.info("Error: " + response.getStatusCode());
                    throw new SecurityException("Error: " + response.getStatusCode());
                }else{
                    logger.info("Error: " + response.getStatusCode());
                    throw new RuntimeException("Error: " + response.getStatusCode());
                }
            }
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public IdentityUserDetails addUserToRole(String token, String userName, String roleName) {
        String url = IDENTITY_URL + "/api/admin/roles/" + roleName + "/" + userName;

        ResponseEntity<IdentityUserDetails> response;
        response = restTemplate.exchange(url, HttpMethod.PUT, getEntity(token), IdentityUserDetails.class);
        if (response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
            return response.getBody();
        } else if (response.getStatusCode().isSameCodeAs(HttpStatus.NO_CONTENT)){
            throw new UserNameNotFoundException();
        } else {
            logger.info("Error: " + response.getStatusCode());
            throw new SecurityException("Error: " + response.getStatusCode());
        }
    }

    public IdentityUserDetails removeUserFromRole(String token, String userName, String roleName) {
        String url = IDENTITY_URL + "/api/admin/roles/" + roleName + "/" + userName;

        ResponseEntity<IdentityUserDetails> response = restTemplate.exchange(
                url, HttpMethod.DELETE, getEntity(token), IdentityUserDetails.class);

        if (response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
            return response.getBody();
        }else if (response.getStatusCode().isSameCodeAs(HttpStatus.NO_CONTENT)){
            throw new UserNameNotFoundException();
        }else{
            logger.info("Error: " + response.getStatusCode());
            throw new SecurityException("Error: " + response.getStatusCode());
        }
    }
    public IdentityUser removeUser(String token, String userName) {
        String url = IDENTITY_URL + "/api/admin/users/" + userName;
        ResponseEntity<IdentityUser> response = restTemplate.exchange(
                url, HttpMethod.DELETE, getEntity(token), IdentityUser.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            logger.info("Error: " + response.getStatusCode());
            throw new SecurityException("Error: " + response.getStatusCode());
        }
    }

        private HttpEntity<String> getEntity(String token){
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);  // Add the token to the header
            return new HttpEntity<>(headers);
        }

}
