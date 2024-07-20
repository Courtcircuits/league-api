package com.courtcircuits;

import com.courtcircuits.exceptions.ChampionNameNotSpecified;
import com.courtcircuits.exceptions.InvalidJsonException;
import jakarta.json.*;

import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

public class ModifyChampionRequest {
    private String championName;
    private Roles role;
    private int lifePoints;
    private List<Ability> abilities;

    public ModifyChampionRequest(String championName, Roles role, int lifePoints, List<Ability> abilities) {
        this.championName = championName;
        this.role = role;
        this.lifePoints = lifePoints;
        this.abilities = abilities;
    }

    public ModifyChampionRequest() {}

    public ModifyChampionRequest fromJson(String json) throws ChampionNameNotSpecified, InvalidJsonException {
        try (JsonReader jsonReader = Json.createReader(new StringReader(json))) {
            JsonObject jsonObject = jsonReader.readObject();

            if (!jsonObject.containsKey("championName")) {
                throw new ChampionNameNotSpecified();
            }

            ModifyChampionRequestBuilder builder = new ModifyChampionRequestBuilder(jsonObject.getString("championName"));
            if(jsonObject.containsKey("role")) {
                builder.withNewRole(Roles.fromString(jsonObject.getString("role")));
            }
            if(jsonObject.containsKey("lifePoints")) {
                builder.withNewLifePoints(jsonObject.getInt("lifePoints"));
            }
            if(jsonObject.containsKey("abilities")) {
                JsonArray abilities = jsonObject.getJsonArray("abilities");
                List<Ability> abilitiesList = abilities.stream().map(Ability::fromJson).toList(); //this can easily go wrong
                System.out.println(abilitiesList);
                builder.withNewAbilities(abilitiesList);
            }

            return builder.build();

        }catch(Exception e) {
            throw new InvalidJsonException();
        }
    }

    public class ModifyChampionRequestBuilder {
        private String championName;
        private Roles role;
        private int lifePoints;
        private List<Ability> abilities;

        public ModifyChampionRequestBuilder(String name) {
            championName = name;
        }

        public ModifyChampionRequestBuilder withNewRole(Roles role) {
            this.role = role;
            return this;
        }

        public ModifyChampionRequestBuilder withNewAbilities(List<Ability> abilities) {
            this.abilities = abilities;
            return this;
        }

        public ModifyChampionRequestBuilder withNewLifePoints(int lifePoints) {
            this.lifePoints = lifePoints;
            return this;
        }

        public ModifyChampionRequest build() {
            return new ModifyChampionRequest(championName, role, lifePoints, abilities);
        }


    }

    public String getChampionName() {
        return championName;
    }

    public Roles getRole() {
        return role;
    }

    public int getLifePoints() {
        return lifePoints;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

}
