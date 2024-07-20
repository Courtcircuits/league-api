package com.courtcircuits;

import com.courtcircuits.exceptions.*;

import java.util.HashMap;
import java.util.Locale;

public class ChampionPool {
    private HashMap<String, Champion> champions;

    private ChampionPool() {
        champions = new HashMap<>();
    }

    private static final class ChampionPoolHolder { //thread safe singleton holder
        private static final ChampionPool instance = new ChampionPool();
    }

    public static ChampionPool getInstance() {
        return ChampionPoolHolder.instance;
    }

    public void addChampion(Champion champion) throws ChampionAlreadyExistsException, GameAlreadyStartedException {
        if(Game.getInstance().isStarted()) {
            throw new GameAlreadyStartedException();
        }
        if (champDoesExist(champion)) {
            throw new ChampionAlreadyExistsException(champion.getChampionName());
        }
        champions.put(modifyKey(champion.getChampionName()), champion);
    }

    public boolean champDoesExist(Champion champion) {
        return champions.containsKey(modifyKey(champion.getChampionName()));
    }

    public Champion getChampion(String championName) throws ChampionNotFoundException {
        Champion champion = champions.get(modifyKey(championName));
        if (champion == null) {
            throw new ChampionNotFoundException(championName);
        }
        return champion;
    }

    private String modifyKey(String key) {
        return key.toUpperCase(Locale.ROOT);
    }

    public Champion modifyChampion(ModifyChampionRequest modifyChampionRequest) throws ChampionNotFoundException, AbilityAlreadyExists {
        Champion champion = this.getChampion(modifyChampionRequest.getChampionName());
        if (modifyChampionRequest.getRole() != null) {
            champion.setRole(modifyChampionRequest.getRole());
        }
        if (modifyChampionRequest.getLifePoints() != 0) {
            champion.setLifePoints(modifyChampionRequest.getLifePoints());
        }
        if (modifyChampionRequest.getAbilities() != null) {
            champion.setAbilities(modifyChampionRequest.getAbilities());
        }
        return champion;
    }

    public Champion pickChampion(String championName) throws ChampionNotFoundException, ChampionAlreadyPicked {
        Champion champion = this.getChampion(championName);
        champion.pick();
        return champion;
    }

    public void clear() {
        champions.clear();
    }
}
