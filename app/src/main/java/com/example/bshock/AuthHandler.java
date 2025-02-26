package com.example.bshock;

public class AuthHandler {

    String username;
    String apikey;
    String code; // share code
    String name; // Name used in debug logging

    public AuthHandler(String username, String apikey, String code, String name) {
        this.username = username;
        this.apikey = apikey;
        this.code = code;
        this.name = name;
    }

    public AuthHandler(){

    }

    public String getUsername() {
        return username;
    }

    public String getApikey() {
        return apikey;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
