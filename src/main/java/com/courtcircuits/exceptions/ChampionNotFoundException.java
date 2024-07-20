package com.courtcircuits.exceptions;

public class ChampionNotFoundException extends ApiException{
    public ChampionNotFoundException(String championName) {
        super("Champion not found: " + championName);
    }

    public int getStatusCode() {
        return 404;
    }
}
