package com.courtcircuits;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

public class Ability {
    private String ability;
    private int damage;

    public Ability(String ability, int damage) {
        this.ability = ability;
        this.damage = damage;
    }

    public String getAbility() {
        return ability;
    }

    public int getDamage() {
        return damage;
    }

    public static Ability fromJson(JsonValue json) {
        return new Ability(((JsonObject) json).getString("ability"), ((JsonObject) json).getInt("damage"));
    }

    @Override
    public String toString() {
        return "Ability{" +
                "ability='" + ability + '\'' +
                ", damage=" + damage +
                '}';
    }
}
