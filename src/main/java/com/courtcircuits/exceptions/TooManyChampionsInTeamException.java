package com.courtcircuits.exceptions;

public class TooManyChampionsInTeamException extends ApiException{
    public TooManyChampionsInTeamException() {
        super("Too many champions in team");
    }
}
