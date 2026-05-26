package com.liciot.devicems.dto;

public class BasicAuthenticationBody {
    String username;
    String password;

    public BasicAuthenticationBody(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public BasicAuthenticationBody() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
