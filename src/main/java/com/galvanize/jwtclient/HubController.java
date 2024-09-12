package com.galvanize.jwtclient;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin("*")
public class HubController {

    private HubService hubService;

    public HubController(HubService hubService) {
        this.hubService = hubService;
    }

    @GetMapping("/api/admin/hubs")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<HubList> getHub() {
        List<Hub> hubs = hubService.getHubs();
        if (hubs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(new HubList(hubs));
    }

    @GetMapping("/api/admin/hubs/{name}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Hub> getHub(@PathVariable String name) {
        Hub hub = hubService.getHubByName(name);
        return hub == null ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(hub);
    }


    @PostMapping("/api/admin/hubs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hub> addHub(@RequestBody Hub hub){
        try{
            hub = hubService.addHub(hub);
        }catch (InvalidHubException e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(hub);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void invalidHubExceptionHandler(InvalidHubException e){

    }

    @PatchMapping("/api/admin/hubs/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hub> updateHub(@PathVariable String name, @RequestBody Hub hub){
        try{
            hub = hubService.updateHub(name, hub);
        }catch (InvalidHubException y){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(hub);
    }

//    @ExceptionHandler(HubNotFoundException.class)
//    public ResponseEntity<String> handleHubNotFoundException(HubNotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//    }

    @DeleteMapping("/api/admin/hubs/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteHub(@PathVariable long id) {
      hubService.deleteHubById(id);
    }

    @ExceptionHandler(HubNotFoundException.class)
    public ResponseEntity<String> handleHubNotFoundException(HubNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }


}
