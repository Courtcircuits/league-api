package com.courtcircuits.exceptions;

public class TooManyTeamsException extends ApiException{
    public TooManyTeamsException() {
        super("Too many teams");
    }
}
