package com.courtcircuits.exceptions;

public class InvalidJsonException extends ApiException{
    public InvalidJsonException() {
        super("Invalid json");
    }
}
