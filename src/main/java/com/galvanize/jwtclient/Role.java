package com.galvanize.jwtclient;

public class Role {
    public String roleTitle;

    public Role(String roleTitle) {
        this.roleTitle = roleTitle;
    }
    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }
    public String getRoleTitle(){
        return roleTitle;
    }
}
