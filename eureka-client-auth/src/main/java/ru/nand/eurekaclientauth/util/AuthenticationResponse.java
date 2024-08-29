package ru.nand.eurekaclientauth.util;

import org.springframework.beans.factory.annotation.Autowired;

public class AuthenticationResponse {
    private final String jwt;

    @Autowired
    public AuthenticationResponse(String jwt){
        this.jwt = jwt;
    }

    public String getJwt(){
        return jwt;
    }
}
