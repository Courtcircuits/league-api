package com.courtcircuits;

import com.courtcircuits.exceptions.ApiException;
import com.courtcircuits.exceptions.ChampionAlreadyExistsException;
import io.quarkus.logging.Log;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import javax.print.attribute.standard.Media;
import java.io.StringReader;
import java.util.List;

@Path("/api")
public class LeagueResource {
    private final ChampionPool championPool = ChampionPool.getInstance(); //instanced champion pool like that just to make sure that we could change ChampionPool implementation at any moment e.g. for mocking purposes
    private final Game game= Game.getInstance();

    @POST
    @Path("/create")
    public Response create(String json) {
        try {
            Champion champion = Champion.fromJson(json);
            championPool.addChampion(champion);
            Log.info("Champion created: " + champion.getChampionName());
            return Response.status(200).entity(champion).build();
        }catch (ApiException e) {
            Log.error(e.getMessage());
            return buildError(e);
        }
    }

    @POST
    @Path("/modify")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modify(String json) {
        try {
            ModifyChampionRequest modifyChampionRequest = new ModifyChampionRequest().fromJson(json);
            Champion champion = championPool.modifyChampion(modifyChampionRequest);
            Log.info("Champion modified: " + champion.getChampionName());
            return Response.status(200).entity(champion).build();
        }catch(ApiException e) {
            Log.error(e.getMessage());
            return buildError(e);
        }
    }

    @POST
    @Path("/team")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTeam(String json) {
        try {
            Team team = Team.fromJson(json);
            game.addTeam(team);
            Log.info("Team created: " + team.getTeamName());
            return Response.status(200).entity(team).build();
        }catch(ApiException e) {
            Log.error(e.getMessage());
            return buildError(e);
        }
    }

    @PATCH
    @Path("/team")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addChampToTeam(String json) {
        try {
            Team.ChampionPick championPick = Team.ChampionPick.fromJson(json);
            String teamName = "";

            try(JsonReader jsonReader = Json.createReader(new StringReader(json))) {
                JsonObject jsonObject = jsonReader.readObject();
                teamName = jsonObject.getString("teamName");

            }catch (Exception e) {
                return Response.status(400).entity("Invalid json").build();
            }

            Team team = game.getTeamByName(teamName);

            team.addChampion(championPick);

            return Response.status(200).entity(team).build();
        }catch(ApiException e) {
            Log.error(e.getMessage());
            return buildError(e);
        }
    }

    @POST
    @Path("/begin")
    public Response beginGame() {
        try {
            Game.getInstance().startGame();
        }catch (ApiException e) {
            Log.error(e.getMessage());
            return buildError(e);
        }
        return Response.status(200).entity("Bienvenue dans la faille de l'invocateur").build();
    }

    @GET
    @Path("/searchByLane")
    public Response searchByLane(@QueryParam("lane") String lane) {
        try {
            return Response.status(200).entity(game.getChampions(Lanes.valueOf(lane))).build();
        }catch (IllegalArgumentException e) {
            return Response.status(400).entity("Invalid lane").build();
        }
    }

    @GET
    @Path("/predict")
    public Response predict() {
        List<Game.Matchup> matchups = game.predict();
        return Response.status(200).entity(matchups).build();
    }


    private Response buildError(ApiException e) {
        return Response.status(e.getStatusCode()).entity(e.getMessage()).build();
    }
}
