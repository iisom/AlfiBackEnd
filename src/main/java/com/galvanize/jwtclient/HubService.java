package com.galvanize.jwtclient;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HubService {

    HubsRepository hubsRepository;

    public HubService(HubsRepository hubsRepository) {
        this.hubsRepository = hubsRepository;
    }

    public List<Hub> getHubs() {
        return hubsRepository.findAll();
    }

    public Hub getHubByName(String name) {
        List<Hub> hubs = hubsRepository.findByName(name);
        return hubs.isEmpty() ? null : hubs.get(0);
    }

    public Hub addHub(Hub hub) {
        return hubsRepository.save(hub);
    }

    public Hub updateHub(String name, Hub updatedHub) {
        List<Hub> existingHubs = hubsRepository.findByName(name);
        if (existingHubs.isEmpty()) {
            return null;
        }
        Hub existingHub = existingHubs.get(0);
        existingHub.setName(updatedHub.getName());
        existingHub.setPicture(updatedHub.getPicture());
        return hubsRepository.save(existingHub);
    }

    public void deleteHub(String name) {
        List<Hub> hubs = hubsRepository.findByName(name);
        if (hubs == null || hubs.isEmpty()) {
            throw new HubNotFoundException("There isn't a hub with the name " + name);
        }
        for (Hub hub : hubs) {
            hubsRepository.delete(hub);
        }
    }

    public void deleteHubByName(String name) throws HubNotFoundException {
        List<Hub> hub = hubsRepository.findByName(name);
        if (hub.isEmpty()) {
            throw new HubNotFoundException("Hub not found with name: " + name);
        }
        hubsRepository.findByName(name);
    }



    public void deleteHubById(Long id) throws HubNotFoundException {
        Optional<Hub> hub = hubsRepository.findById(id);
        if (hub.isEmpty()) {
            throw new HubNotFoundException("Hub not found with id: " + id);
        }
        hubsRepository.delete(hub.get());
    }

}
//
//
//    }