package com.galvanize.jwtclient.identity;

import java.util.List;

public class IdentityRoleList {
    private List<IdentityRole> roles;

    public IdentityRoleList() {}

    public IdentityRoleList(List<IdentityRole> roles) {
        this.roles = roles;
    }

    public List<IdentityRole> getRoles() {
        return roles;
    }

    public void setRoles(List<IdentityRole> roles) {
        this.roles = roles;
    }
}
