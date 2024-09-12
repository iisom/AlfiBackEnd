package com.galvanize.jwtclient;

import com.galvanize.jwtclient.security.JwtProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AlfieHubIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private HubsRepository hubsRepository;

    private TestingUtilities util;

    private Random random;
    private List<Hub> testHubs;

    @BeforeEach
    void setup() {
        this.random = new Random();
        this.testHubs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Hub hub = new Hub("Hub" + i, "Picture" + i);
            this.testHubs.add(hub);
        }
        hubsRepository.saveAll(this.testHubs);

        this.util = new TestingUtilities(restTemplate, jwtProperties);
    }

    @AfterEach
    void tearDown() {
        hubsRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return a list of hubs")
    void getHubs_exists_returnsHubsList() {
        String token = util.getToken("user", 1, List.of("ROLE_USER"));
        ResponseEntity<HubList> response = util.getRequest("/api/admin/hubs", HubList.class, token);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getHubs()).isNotEmpty();
    }

    @Test
    @DisplayName("Should return a hub by name")
    void getHubByName_exists_returnsHub() {
        Hub hub = testHubs.get(random.nextInt(testHubs.size()));
        String token = util.getToken("user", 1, List.of("ROLE_USER"));
        ResponseEntity<Hub> response = util.getRequest("/api/admin/hubs/" + hub.getName(), Hub.class, token);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(hub.getName());
    }

    @Test
    @DisplayName("Should return no content for non-existent hub")
    void getHubByName_notFound_returnsNoContent() {
        String token = util.getToken("user", List.of("ROLE_USER"));
        ResponseEntity<Hub> response = util.getRequest("/api/admin/hubs/NonExistentHub", Hub.class, token);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Should add a new hub")
    void addHub_returnsNewHubDetails() {
        Hub newHub = new Hub("NewHub", "NewPicture");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String token = util.getToken("adminUser", List.of("ROLE_ADMIN"));
        headers.setBearerAuth(token);

        HttpEntity<Hub> request = new HttpEntity<>(newHub, headers);
        ResponseEntity<Hub> response = restTemplate.postForEntity("/api/admin/hubs", request, Hub.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(newHub.getName());
    }

    @Test
    @DisplayName("Should delete a hub")
    void deleteHub_succeeds() {
        Hub hubToDelete = testHubs.get(random.nextInt(testHubs.size()));
        String token = util.getToken("adminUser", List.of("ROLE_ADMIN"));
        String token1 = util.getToken("user", List.of("ROLE_USER"));
        restTemplate.delete("/api/admin/hubs/" + hubToDelete.getName(), token);
        ResponseEntity<Hub> response = util.getRequest("/api/admin/hubs/" + hubToDelete.getName(), Hub.class, token1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
