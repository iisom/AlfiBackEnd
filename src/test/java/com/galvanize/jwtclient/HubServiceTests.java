package com.galvanize.jwtclient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class HubServiceTests {

    HubService hubService;

    @Mock
    HubsRepository hubsRepository;

    @BeforeEach
    void setUp() {
        hubService = new HubService(hubsRepository);
    }

    @Test
    void getAllHubs_exists_returnsHubsList() {
        List<Hub> hubs = Arrays.asList(
                new Hub("Atlanta", "https://example.com/image"),
                new Hub("Dallas", "https://example.com/image")
        );
        when(hubsRepository.findAll()).thenReturn(hubs);
        List<Hub> resultHubs = hubService.getHubs();
        assertThat(resultHubs).isNotNull();
        assertThat(resultHubs).hasSize(2);
        assertThat(resultHubs).extracting(Hub::getName)
                .containsExactly("Atlanta", "Dallas");
        assertThat(resultHubs).extracting(Hub::getPicture)
                .containsExactly("https://example.com/image", "https://example.com/image");
    }

    @Test
    void getAllHubs_notExist_returnsEmptyList() {
        List<Hub> emptyHubList = new ArrayList<>();
        when(hubsRepository.findAll()).thenReturn(emptyHubList);
        List<Hub> resultHubs = hubService.getHubs();
        assertThat(resultHubs).isNotNull();
        assertThat(resultHubs).isEmpty();
    }

    @Test
    void getHubSearch_withName_Name() {
        Hub hub = new Hub("Atlanta", "https://example.com/image");
        List<Hub> hubList = new ArrayList<>();
        hubList.add(hub);
        when(hubsRepository.findByName(anyString())).thenReturn(hubList);
        Hub result = hubService.getHubByName("Atlanta");
        assertEquals(hub, result);
    }

    @Test
    void testGetHubByName_NotFound() {
        Hub result = hubService.getHubByName("NonExisting");
        assertEquals(null, result);
    }

    @Test
    void addHub_returnsHub() {
        Hub hub = new Hub("Atlanta", "https://example.com/image");
        when(hubsRepository.save(any())).thenReturn(hub);
        Hub savedHub = hubService.addHub(hub);
        assertThat(savedHub).isNotNull();
        assertThat(savedHub.getName()).isEqualTo("Atlanta");
    }

    @Test
    void updateHub_Name_updatesHubSuccessfully() {
        Hub existingHub = new Hub("Atlanta", "https://example.com/image");
        Hub updatedHub = new Hub("Dallas", "https://example.com/image2");
        List<Hub> existingHubList = Collections.singletonList(existingHub);
        when(hubsRepository.findByName("Atlanta")).thenReturn(existingHubList);
        when(hubsRepository.save(existingHub)).thenReturn(updatedHub);
        Hub result = hubService.updateHub("Atlanta", updatedHub);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Dallas");
        assertThat(result.getPicture()).isEqualTo("https://example.com/image2");
    }

    @Test
    void deleteHub_HubExists_DeletesHub() {
        Hub existingHub = new Hub("ExistingHub", "https://example.com/image");
        List<Hub> existingHubList = Collections.singletonList(existingHub);
        when(hubsRepository.findByName("ExistingHub")).thenReturn(existingHubList);
        doNothing().when(hubsRepository).delete(existingHub);
        hubService.deleteHub("ExistingHub");
        verify(hubsRepository).delete(existingHub);
    }

    @Test
    void deleteHub_HubNotFound_ThrowsException() {
        String hubName = "NonExistingHub";
        when(hubsRepository.findByName(hubName)).thenReturn(Collections.emptyList());
        assertThatThrownBy(() -> hubService.deleteHub(hubName))
                .isInstanceOf(HubNotFoundException.class)
                .hasMessage("There isn't a hub with the name " + hubName);
        verify(hubsRepository, never()).delete(any(Hub.class));
    }
}
