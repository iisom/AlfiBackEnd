package com.galvanize.jwtclient;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.ArgumentMatchers.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HubController.class)
public class HubControllerTests {

    @MockBean
    HubService hubService;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    @WithMockUser(roles = {"User"})
    void getHub_withHubs_returnsHubList() throws Exception {
        List<Hub> listOfHubs = new ArrayList<>();
        listOfHubs.add(new Hub("Atlanta", "https://example.com/image.png"));
        listOfHubs.add(new Hub( "New York", "https://example.com/image2.png"));
        when(hubService.getHubs()).thenReturn(listOfHubs);
        mockMvc.perform(get("/api/admin/hubs")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hubs", hasSize(2)))
                .andExpect(jsonPath("$.hubs[0].name").value("Atlanta"))
                .andExpect(jsonPath("$.hubs[1].name").value("New York"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getAllHubs_ByName() throws Exception {
        Hub hub = new Hub("Atlanta", "https://example.com/image");
        when(hubService.getHubByName(anyString())).thenReturn(hub);
        mockMvc.perform(get("/api/admin/hubs/Atlanta")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Atlanta"));}

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getHub_noHubs_returnsNoContent() throws Exception {
        when(hubService.getHubs()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/admin/hubs")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void addHub_valid_returnsHub() throws Exception {
        Hub hub = new Hub("Atlanta", "https://example.com/image");
        when(hubService.addHub(any(Hub.class))).thenReturn(hub);
        mockMvc.perform(post("/api/admin/hubs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(mapper.writeValueAsString(hub)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Atlanta"))
                .andExpect(jsonPath("$.picture").value("https://example.com/image"));
    }


    @Test
    @WithMockUser(roles = {"ADMIN"})
    void addHub_invalid_returnsNotFound() throws Exception {
        Hub hub = new Hub("Dallas", "https://example.com/image");
        when(hubService.addHub(any(Hub.class))).thenThrow(InvalidHubException.class);
        mockMvc.perform(post("/api/admin/hubs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(mapper.writeValueAsString(hub)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateHub_valid_RequestReturns_UpdatedHubName() throws Exception {
        List<Hub> hubs = new ArrayList<>();
        Hub updatedHub  = new Hub("Atlanta", "https://example.com/image");
        when(hubService.updateHub(anyString(), any())).thenReturn(updatedHub);
        updatedHub.setName("Dallas");
        when(hubService.updateHub(anyString(), any()))
                .thenReturn(updatedHub);
        mockMvc.perform(patch("/api/admin/hubs/Atlanta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content("{\"name\":\"Atlanta\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Dallas"))
                .andExpect(jsonPath("picture").value("https://example.com/image"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateHub_invalid_RequestReturns_UpdatedHubName() throws Exception {
        when(hubService.updateHub(anyString(), any())).thenThrow(new HubNotFoundException("Hub not found"));
        mockMvc.perform(patch("/api/admin/hubs/Florida")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content("{\"name\":\"Atlanta\", \"picture\":\"https://example.com/image\"}"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteHub_byId_returns202() throws Exception {
        doNothing().when(hubService).deleteHubById(3L);
        mockMvc.perform(delete("/api/admin/hubs/3")
                        .with(csrf()))
                .andExpect(status().isAccepted());
        verify(hubService).deleteHubById(3L);
    }


    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteHub_idDoesNotExist_returnsNotFound() throws Exception {
        doThrow(new HubNotFoundException("That is not a valid Hub ID."))
                .when(hubService).deleteHubById(999L);
        mockMvc.perform(delete("/api/admin/hubs/999")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }



}


