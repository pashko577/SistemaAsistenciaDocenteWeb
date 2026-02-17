package com.example.actividades_app.config.exception;

public class RoleAlreadyExistsException extends RuntimeException{
    public RoleAlreadyExistsException(String message){
        super(message);
    }

}
