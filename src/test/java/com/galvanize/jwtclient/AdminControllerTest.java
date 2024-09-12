package com.galvanize.jwtclient;

import com.galvanize.jwtclient.identity.IdentityRole;
import com.galvanize.jwtclient.identity.IdentityService;
import com.galvanize.jwtclient.identity.IdentityUser;
import com.galvanize.jwtclient.identity.IdentityUserDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    IdentityService identityService;



    //PUT: /api/admin/roles/{role}/{username} Add a user to a role
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void assignAdminRole_returnsUserWithAdminRole() throws Exception {
        /*
        1. given an admin user and a path variable -> role and path variable -> username
        2. when identity service is called w the same parameters
        3. then user is given that new role
         */
        IdentityUser identityUser = new IdentityUser(1L,"user@glab.com", "user@glab.com", "General", "User", "password");
        List<IdentityRole> roles = List.of(new IdentityRole(1L, "ADMIN", "administrator"));

        when(identityService.addUserToRole(anyString(),anyString(),anyString())).thenReturn(new IdentityUserDetails(identityUser,roles));

        mockMvc.perform(put("/api/admin").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"username\": \"user@glab.com\", \"role\": \"ADMIN\"}")
                    .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());

    }




    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteAdminRole_returnsUserWithoutAdminRole() throws Exception {
        mockMvc.perform(delete("/api/admin/role/ADMIN/user@glab.com").with(csrf()).header("Authorization", "mockToken"))
                .andDo(print())
                .andExpect(status().isAccepted());
        verify(identityService).removeUserFromRole(anyString(),anyString(),anyString());
    }
}
