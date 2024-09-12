package com.galvanize.jwtclient;

public class HubNotFoundException extends RuntimeException {
    public HubNotFoundException(String message) {
        super(message);
    }
}
