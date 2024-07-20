package com.courtcircuits;

import com.courtcircuits.exceptions.AbilityAlreadyExists;
import com.courtcircuits.exceptions.ApiException;
import com.courtcircuits.exceptions.ChampionAlreadyPicked;
import com.courtcircuits.exceptions.InvalidJsonException;
import jakarta.json.*;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Champion {
    private String championName;
    private Roles role;
    private int lifePoints;
    private HashMap<String, Ability> abilities;
    private boolean picked;

    public Champion(String championName, Roles role, int lifePoints, List<Ability> abilities) {
        this.championName = championName;
        this.role = role;
        this.lifePoints = lifePoints;
        this.abilities = new HashMap<>();
        for (Ability ability : abilities) {
            this.abilities.put(ability.getAbility(), ability);
        }
    }

    public Champion(){}

    public String getChampionName() {
        return championName;
    }


    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public int getLifePoints() {
        return lifePoints;
    }

    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
    }

    public List<Ability> getAbilities() {
        return List.copyOf(abilities.values());
    }

    public void setAbilities(List<Ability> abilities) throws AbilityAlreadyExists {
        for (Ability ability : abilities) {
            if (this.abilities.containsKey(ability.getAbility())) {
                throw new AbilityAlreadyExists(ability.getAbility());
            }else {
                this.abilities.put(ability.getAbility(), ability);
            }
        }
    }

    public int getAbilityDamage() {
       List<Ability> abilities = getAbilities();
       return Math.max(abilities.stream().map(Ability::getDamage).reduce(0, Integer::sum), 0);
    }

    public static Champion fromJson(String json) throws InvalidJsonException {
        try(JsonReader jsonReader = Json.createReader(new StringReader(json))) {
            JsonObject jsonObject = jsonReader.readObject();
            Champion champion = new Champion();
            champion.championName = jsonObject.getString("championName");
            champion.role = Roles.fromString(jsonObject.getString("role"));
            champion.lifePoints = jsonObject.getInt("lifePoints");
            champion.abilities = new HashMap<>();
            JsonArray abilities = jsonObject.getJsonArray("abilities");
            for (JsonValue ability : abilities) {
                Ability newAbility = Ability.fromJson(ability);
                champion.abilities.put(newAbility.getAbility(), newAbility);
            }
            return champion;
        }catch (Exception e) {
            throw new InvalidJsonException();
        }
    }

    public void pick() throws ChampionAlreadyPicked{
        if (picked) {
            throw new ChampionAlreadyPicked(championName);
        }
        picked = true;
    }

    public void unpick() {
        picked = false;
    }

    @Override
    public String toString() {
        return "Champion{" +
                "championName='" + championName + '\'' +
                ", role=" + role +
                ", lifePoints=" + lifePoints +
                ", abilities=" + abilities +
                ", picked=" + picked +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Champion champion = (Champion) o;
        return Objects.equals(championName, champion.championName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(championName);
    }
}
