package com.galvanize.jwtclient;

import java.util.ArrayList;
import java.util.List;

public class RoleList {
    private List<Role> roles;
    public RoleList(List<Role> roles) {
        this.roles = roles;
    }
    public RoleList() {
        this.roles = new ArrayList<>();
    }
    public List<Role> getRoles() {
        return roles;
    }
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    public boolean isEmpty(){
        return this.roles.isEmpty();
    }
    @Override
    public String toString(){
        return "RolesList{" +
                "roles="+ roles +
                '}';
    }
}
