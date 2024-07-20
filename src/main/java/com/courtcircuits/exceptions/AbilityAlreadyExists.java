package com.courtcircuits.exceptions;

public class AbilityAlreadyExists extends ApiException{
    public AbilityAlreadyExists(String abilityName) {
        super("Ability already exists: " + abilityName);
    }
}
