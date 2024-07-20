package com.courtcircuits.exceptions;

public class GameAlreadyStartedException extends ApiException{
    public GameAlreadyStartedException() {
        super("Game already started");
    }
}
