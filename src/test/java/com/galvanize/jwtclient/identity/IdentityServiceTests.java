package com.galvanize.jwtclient.identity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IdentityServiceTests {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    IdentityService identityService;

    HttpEntity<String> entity;
    String token = "mocked-token";
    @BeforeEach
    void setUp() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);  // Add the token to the header
        entity = new HttpEntity<>(headers);
    }


    @Test
    void testGetRoles() {
        // Given

        IdentityRoleList expectedRoles = new IdentityRoleList(List.of(new IdentityRole(1L, "ADMIN", "Administrator")));

        // Mock the exchange method
        when(restTemplate.exchange(
                anyString(), // Match any URL
                eq(HttpMethod.GET), // Match the exact HTTP method
                any(HttpEntity.class), // Match any HttpEntity
                eq(IdentityRoleList.class) // Match the exact response type
        )).thenReturn(new ResponseEntity<>(expectedRoles, HttpStatus.OK));

        // When
        IdentityRoleList response = identityService.getRoles(token);

        // Then
        assertEquals(expectedRoles, response);
    }

    // Add role to user
    @Test
    void testAddRollToUser() {
        IdentityUser user = new IdentityUser(1L, "username",
                "email@gmail.com", "firstName",
                "lastName", "password");
        List<IdentityRole> roles = List.of(new IdentityRole(1L, "ROLE_ADMIN", "administrator"));
        IdentityUserDetails details = new IdentityUserDetails(user, roles);
        when(restTemplate.exchange(
                anyString(), // Match any URL
                eq(HttpMethod.PUT), // Match the exact HTTP method
                any(HttpEntity.class), // Match any HttpEntity
                eq(IdentityUserDetails.class) // Match the exact response type
        )).thenReturn(new ResponseEntity<>(details, HttpStatus.OK));

        IdentityUserDetails userDetails = identityService.addUserToRole(token,"userName", "roleName");
        assertEquals(details, userDetails);
    }

    // Remove role from user

    @Test
    void testRemoveUserFromRole() {
        IdentityUser user = new IdentityUser(1L, "username",
                "email@gmail.com", "firstName",
                "lastName", "password");
        List<IdentityRole> roles = List.of(new IdentityRole(1L, "ROLE_ADMIN", "administrator"));
        IdentityUserDetails details = new IdentityUserDetails(user, roles);
        when(restTemplate.exchange(
                anyString(), // Match any URL
                eq(HttpMethod.DELETE), // Match the exact HTTP method
                any(HttpEntity.class), // Match any HttpEntity
                eq(IdentityUserDetails.class) // Match the exact response type
        )).thenReturn(new ResponseEntity<>(details, HttpStatus.OK));

        IdentityUserDetails userDetails = identityService.removeUserFromRole(token,"userName", "roleName");
        assertEquals(details, userDetails);
    }

    @Test
    void testAddRollToUser_userNotFound_throwsUserNotFoundException() {
        IdentityUser user = new IdentityUser(1L, "username",
                "email@gmail.com", "firstName",
                "lastName", "password");
        List<IdentityRole> roles = List.of(new IdentityRole(1L, "ROLE_ADMIN", "administrator"));
        IdentityUserDetails details = new IdentityUserDetails(user, roles);
        when(restTemplate.exchange(
                anyString(), // Match any URL
                eq(HttpMethod.PUT), // Match the exact HTTP method
                any(HttpEntity.class), // Match any HttpEntity
                eq(IdentityUserDetails.class) // Match the exact response type
        )).thenReturn(new ResponseEntity<>(details, HttpStatus.NO_CONTENT));

        assertThrows(UserNameNotFoundException.class, ()-> identityService.addUserToRole(token, "userName", "roleName"));
    }

    @Test
    void testRemoveRoleFromUser_userNotFound_throwsUserNotFoundException() {
        IdentityUser user = new IdentityUser(1L, "username",
                "email@gmail.com", "firstName",
                "lastName", "password");
        List<IdentityRole> roles = List.of(new IdentityRole(1L, "ROLE_ADMIN", "administrator"));
        IdentityUserDetails details = new IdentityUserDetails(user, roles);
        when(restTemplate.exchange(
                anyString(), // Match any URL
                eq(HttpMethod.DELETE), // Match the exact HTTP method
                any(HttpEntity.class), // Match any HttpEntity
                eq(IdentityUserDetails.class) // Match the exact response type
        )).thenReturn(new ResponseEntity<>(details, HttpStatus.NO_CONTENT));

        assertThrows(UserNameNotFoundException.class, ()-> identityService.removeUserFromRole(token, "userName", "roleName"));
    }
    @Test
    void testRemoveUser() {
        IdentityUser user = new IdentityUser(1L, "snoopy@glab.com",
                "snoopy@glab.com", "snoopy",
                "snoop", "password");

        when(restTemplate.exchange(
                anyString(), // Match any URL
                eq(HttpMethod.DELETE), // Match the exact HTTP method
                any(HttpEntity.class), // Match any HttpEntity
                eq(IdentityUser.class) // Match the exact response type
        )).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        IdentityUser userDetails = identityService.removeUser(token,"snoopy@glab.com");
        assertEquals(null,userDetails);
        //"User " + user.getUsername() + "removed as requested by admin@glab.com",userDetails
    }
}