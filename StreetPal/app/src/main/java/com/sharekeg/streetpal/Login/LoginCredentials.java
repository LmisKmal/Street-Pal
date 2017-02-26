package com.sharekeg.streetpal.Login;

/**
 * Created by MMenem on 2/23/2017.
 */
public class LoginCredentials {

    private String username;
    private String password;
    private String token;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
