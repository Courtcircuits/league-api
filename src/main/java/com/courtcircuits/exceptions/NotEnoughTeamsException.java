package com.courtcircuits.exceptions;

public class NotEnoughTeamsException extends ApiException{
    public NotEnoughTeamsException() {
        super("Not enough teams");
    }
}
