package com.galvanize.jwtclient;

public class AdminRoleRequest {
    private String role;
    private String username;

    public AdminRoleRequest(String role, String username) {
        this.role = role.toUpperCase();
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role.toUpperCase();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "AdminRoleRequest{" +
                "role='" + role + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
