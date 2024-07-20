package com.courtcircuits.exceptions;

public class ChampionAlreadyPicked extends ApiException{
    public ChampionAlreadyPicked(String championName) {
        super("Champion already picked: " + championName);
    }
}
