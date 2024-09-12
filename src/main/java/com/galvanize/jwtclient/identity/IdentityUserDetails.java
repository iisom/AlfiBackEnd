package com.galvanize.jwtclient.identity;

import java.util.List;

public class IdentityUserDetails {
    private IdentityUser user;
    private List<IdentityRole> roles;

    public IdentityUserDetails() {}

    public IdentityUserDetails(IdentityUser user, List<IdentityRole> roles) {
        this.user = user;
        this.roles = roles;
    }


    public IdentityUser getUser() {
        return user;
    }

    public void setUser(IdentityUser user) {
        this.user = user;
    }

    public List<IdentityRole> getRoles() {
        return roles;
    }

    public void setRoles(List<IdentityRole> roles) {
        this.roles = roles;
    }
}
