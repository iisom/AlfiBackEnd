package com.galvanize.jwtclient;

public class CatNotFoundException extends RuntimeException {
    public CatNotFoundException(String message){
        super(message);
    }
}
