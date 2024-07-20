package com.courtcircuits.exceptions;

public class NotEnoughChampsException extends ApiException{
    public NotEnoughChampsException(String teamName) {
        super("Not enough champions in team : "+teamName);
    }
}
