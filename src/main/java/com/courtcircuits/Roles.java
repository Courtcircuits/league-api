package com.courtcircuits;

public enum Roles {
    SUPPORT("SUPPORT"),
    COMBATTANT("COMBATTANT"),
    MAGE("MAGE"),
    TIREUR("TIREUR"),
    ASSASSIN("ASSASSIN"),
    AUTRE("AUTRE");


    Roles(String role) {
    }

    public static Roles fromString(String role) {
        return switch (role) {
            case "SUPPORT" -> SUPPORT;
            case "COMBATTANT" -> COMBATTANT;
            case "MAGE" -> MAGE;
            case "TIREUR" -> TIREUR;
            case "ASSASSIN" -> ASSASSIN;
            default -> AUTRE;
        };
    }
}
