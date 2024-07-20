package com.courtcircuits;

import com.courtcircuits.exceptions.ChampionAlreadyPicked;
import com.courtcircuits.exceptions.ChampionNotFoundException;
import com.courtcircuits.exceptions.TooManyChampionsInTeamException;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.StringReader;
import java.util.*;

public class Team {
    private String teamName;
    private Map<Lanes, List<Champion>> champPosition;
    private Set<String> champNames;

    public Team(String teamName, List<ChampionPick> championList) throws ChampionNotFoundException, ChampionAlreadyPicked, TooManyChampionsInTeamException {
        this.teamName = teamName;
        this.champPosition = new HashMap<>();
        this.champNames = new HashSet<>();

        for (ChampionPick championPick : championList) {
            addChampion(championPick);
        }
    }

    public static Team fromJson(String json) throws ChampionNotFoundException, ChampionAlreadyPicked, TooManyChampionsInTeamException {
        try(JsonReader jsonReader = Json.createReader(new StringReader(json))) {
            JsonObject jsonObject = jsonReader.readObject();
            String teamName = jsonObject.getString("teamName");
            List<ChampionPick> championList = jsonObject.getJsonArray("champions").stream()
                    .map(jsonValue -> {
                        JsonObject jsonObjectList = (jakarta.json.JsonObject) jsonValue;
                        return new ChampionPick(jsonObjectList.getString("championName"), Lanes.valueOf(jsonObjectList.getString("lane")));
                    }).toList();
            return new Team(teamName, championList);
        }
    }


    public String getTeamName() {
        return teamName;
    }

    public List<Champion> getChampions(Lanes lane) {
        return champPosition.get(lane);
    }

    public List<ChampionPick> getChampions () {
        List<ChampionPick> champions = new ArrayList<>();
        for (Lanes lane : champPosition.keySet()) {
            for (Champion champion : champPosition.get(lane)) {
                champions.add(new ChampionPick(champion.getChampionName(), lane));
            }
        }
        return champions;
    }

    public void addChampion(ChampionPick championPick) throws TooManyChampionsInTeamException, ChampionNotFoundException, ChampionAlreadyPicked {
        if (champNames.size() >= 5) {
            throw new TooManyChampionsInTeamException();
        }
        Champion champion = ChampionPool.getInstance().pickChampion(championPick.championName);
        if (!champPosition.containsKey(championPick.lane)) {
            champPosition.put(championPick.lane, new ArrayList<>());
        }
        champPosition.get(championPick.lane).add(champion);
        champNames.add(championPick.championName);
    }

    public static class ChampionPick {
        private String championName;
        private Lanes lane;

        public ChampionPick(String championName, Lanes lane) {
            this.championName = championName;
            this.lane = lane;
        }

        public String getChampionName() {
            return championName;
        }

        public Lanes getLane() {
            return lane;
        }

        public static ChampionPick fromJson(String json) {
            try(JsonReader jsonReader = Json.createReader(new StringReader(json))) {
                JsonObject jsonObject = jsonReader.readObject();
                return new ChampionPick(jsonObject.getString("championName"), Lanes.valueOf(jsonObject.getString("lane")));
            }
        }
    }
}
