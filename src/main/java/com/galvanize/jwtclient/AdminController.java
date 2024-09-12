package com.galvanize.jwtclient;

import com.galvanize.jwtclient.identity.IdentityService;
import com.galvanize.jwtclient.identity.IdentityUserDetails;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {

    private IdentityService identityService;
//Constructor injection to call addUserRole method on identityService
    public AdminController(IdentityService identityService) {
        this.identityService = identityService;
    }

    @PutMapping("/api/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public IdentityUserDetails addAdminRole(@RequestBody AdminRoleRequest request, HttpServletRequest httpServletRequest){

        return identityService.addUserToRole(httpServletRequest.getHeader("Authorization"),request.getUsername(),request.getRole());
    }

    @DeleteMapping("/api/admin/role/{role}/{userName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity removeAdminRole( HttpServletRequest httpServletRequest, @PathVariable String role, @PathVariable String userName){
        identityService.removeUserFromRole(httpServletRequest.getHeader("Authorization"), role, userName);
        return ResponseEntity.accepted().build();
    }
}
