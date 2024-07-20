package com.courtcircuits.exceptions;

public abstract class ApiException extends Exception{
    public ApiException(String message){
        super(message);
    }

    //default error behavior
    public int getStatusCode(){
        return 400;
    }
}
