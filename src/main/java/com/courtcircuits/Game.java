package com.courtcircuits;

import com.courtcircuits.exceptions.NotEnoughChampsException;
import com.courtcircuits.exceptions.NotEnoughTeamsException;
import com.courtcircuits.exceptions.TeamNotFoundException;
import com.courtcircuits.exceptions.TooManyTeamsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private Team team1;
    private Team team2;
    private static Game instance;
    private boolean started;

    private Game() {
    }

    private static final class GameHolder {
        private static final Game INSTANCE = new Game();
    }

    public static Game getInstance() {
        return GameHolder.INSTANCE;
    }


    public void addTeam(Team team) throws TooManyTeamsException {
        if (team1 != null && team2 != null) {
            throw new TooManyTeamsException();
        }
        if (team1 == null) {
            team1 = team;
        } else {
            team2 = team;
        }


    }

    public List<Champion> getChampions(Lanes lane){
        List<Champion> champions = new ArrayList<>();
        if (team1 != null) champions.addAll(team1.getChampions(lane));
        if (team2 != null) champions.addAll(team2.getChampions(lane));
        return champions;
    }

    public Team getTeamByName(String teamName) throws TeamNotFoundException {
        if (team1.getTeamName().equals(teamName)) {
            return team1;
        } else if (team2.getTeamName().equals(teamName)) {
            return team2;
        }
        throw new TeamNotFoundException(teamName);
    }

    public void clear() {
        started = false;
        team1 = null;
        team2 = null;
    }

    public void startGame() throws NotEnoughTeamsException, NotEnoughChampsException {
        if (team1 == null || team2 == null) {
            throw new NotEnoughTeamsException();
        }

        if(team1.getChampions().size() != 5) {
            throw new NotEnoughChampsException(team1.getTeamName());
        }

        if(team2.getChampions().size() != 5) {
            throw new NotEnoughChampsException(team2.getTeamName());
        }
        started = true;
    }

    public List<Matchup> predict() {
        HashMap<Lanes, Matchup> matchups = new HashMap<>();
        for (Lanes lane : Lanes.values()) {
            Matchup curMatchup = new Matchup(team1.getChampions(lane), team2.getChampions(lane), team1.getTeamName(), team2.getTeamName());
            curMatchup.playMatchup();
            matchups.put(lane, curMatchup);
        }
        return new ArrayList<>(matchups.values());
    }

    class Matchup {
        private List<Champion> laneTeam1;
        private List<Champion> laneTeam2;
        private Lanes lane;
        private String winner;
        private String team1;
        private String team2;

        public Matchup(List<Champion> laneTeam1, List<Champion> laneTeam2, String team1, String team2) {
            this.laneTeam1 = laneTeam1;
            this.laneTeam2 = laneTeam2;
            this.team1 = team1;
            this.team2 = team2;
        }

        public String getWinner() {
            return winner;
        }

        public Lanes getLane() {
            return lane;
        }

        /**
         * Play the matchup
         * @return true if team1 wins, false if team2 wins
         */
        public void playMatchup() {

            List<Champion> team1 = new ArrayList<>(laneTeam1);
            List<Champion> team2 = new ArrayList<>(laneTeam2);

            while(!team1.isEmpty() && !team2.isEmpty()) {
                for(Champion champ : team1) {
                    for(Champion champ2 : team2) {
                        champ2.setLifePoints(champ2.getLifePoints() - champ.getAbilityDamage());
                    }
                }
                for (Champion champ : team2) {
                    for (Champion champ2 : team1) {
                        champ2.setLifePoints(champ2.getLifePoints() - champ.getAbilityDamage());
                    }
                }

                team1.removeIf(champ -> champ.getLifePoints() <= 0);
                team2.removeIf(champ -> champ.getLifePoints() <= 0);
            }

            if(team1.isEmpty() && team2.isEmpty()) {
               winner = "Tie";
            }
            if(team1.isEmpty()) {
                this.winner = this.team2;
            } else {
                this.winner = this.team1;
            }



        }
    }


    public boolean isStarted() {
        return started;
    }
}
