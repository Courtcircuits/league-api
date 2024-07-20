package com.courtcircuits.exceptions;

public class TeamNotFoundException extends ApiException{
    public TeamNotFoundException(String teamName) {
        super("Team not found: " + teamName);
    }

    public int getStatusCode() {
        return 404;
    }
}
