package com.courtcircuits.exceptions;

public class ChampionAlreadyExistsException extends ApiException{
    public ChampionAlreadyExistsException (String championName) {
        super("Champion : "+championName+" already exists");
    }
}
