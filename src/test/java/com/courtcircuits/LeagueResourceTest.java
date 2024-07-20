package com.courtcircuits;

import com.courtcircuits.exceptions.ChampionNotFoundException;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class LeagueResourceTest {
    @Test
    void testCreateEndpoint() {
        ChampionPool.getInstance().clear();
        String requestBody ="{\"championName\":\"Hecharim\",\"role\":\"COMBATTANT\",\"lifePoints\":100,\"abilities\":[{\"ability\":\"buveuse d'âme\",\"damage\":30}]}";
        given()
                .when()
                .body(requestBody)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .post("/api/create")
                .then()
                .statusCode(200);


        given()
                .when()
                .body(requestBody)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .post("/api/create")
                .then()
                .statusCode(400);

        ChampionPool.getInstance().clear();
    }

    @Test
    void testCreateEndpointWrongRole() {
        String requestBody = "{\"championName\":\"Hecharim\",\"role\":\"DUMB\",\"lifePoints\":100,\"abilities\":[{\"ability\":\"buveuse d'âme\",\"damage\":30}]}";
        given()
                .when()
                .body(requestBody)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .post("/api/create")
                .then()
                .statusCode(200);
    }

    @Nested
    @DisplayName("Test the modify endpoint")
    class ModifyEndpoint {
        @BeforeEach
        void createChampion() {
            String requestBody = "{\"championName\":\"Hecharim\",\"role\":\"COMBATTANT\",\"lifePoints\":100,\"abilities\":[{\"ability\":\"buveuse d'âme\",\"damage\":30}]}";
            given()
                    .when()
                    .body(requestBody)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .post("/api/create")
                    .then()
                    .statusCode(200);
        }

        @AfterEach
        void deleteChampion() {
            ChampionPool.getInstance().clear();
        }


        @Test
        void testModifyEndpoint() {
            String requestBody = "{\"championName\":\"Hecharim\",\"role\":\"ASSASSIN\"}";

            given()
                    .when()
                    .body(requestBody)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .post("/api/modify")
                    .then()
                    .statusCode(200);
        }

        @Test
        void testModifyEndpointNoName() {
            String requestBody = "{\"role\":\"ASSASSIN\"}";

            given()
                    .when()
                    .body(requestBody)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .post("/api/modify")
                    .then()
                    .statusCode(400);
        }

        @Test
        void testModifyEndpointWrongRole() {
            String requestBody = "{\"championName\":\"Hecharim\",\"role\":\"DUMB\"}";

            given()
                    .when()
                    .body(requestBody)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .post("/api/modify")
                    .then()
                    .statusCode(200);
        }

        @Test
        void testModifyEndpointAlreadyExistingAbility() {
            String requestBody = "{\"championName\":\"Hecharim\",\"role\":\"COMBATTANT\",\"lifePoints\":100,\"abilities\":[{\"ability\":\"buveuse d'âme\",\"damage\":30},{\"ability\":\"buveuse d'âme\",\"damage\":30}]}";

            given()
                    .when()
                    .body(requestBody)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .post("/api/modify")
                    .then()
                    .statusCode(400);
        }

        @Test
        void testModifyEndpointNewAbility() {
            String requestBody = "{\"championName\":\"Hecharim\",\"role\":\"COMBATTANT\",\"lifePoints\":100,\"abilities\":[{\"ability\":\"court tres vite\",\"damage\":30}]}";

            given()
                    .when()
                    .body(requestBody)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .post("/api/modify")
                    .then()
                    .statusCode(200);
        }
    }

    @Nested
    @DisplayName("Test creating team")
    class TeamTest {
        @BeforeEach
        void createChampionPool() {
            ChampionPool pool = ChampionPool.getInstance();
            try {
                pool.addChampion(new Champion(
                        "Hecharim",
                        Roles.COMBATTANT,
                        100,
                        List.of(new Ability("buveuse d'âme", 30))
                ));

                pool.addChampion(new Champion(
                        "Katarina",
                        Roles.ASSASSIN,
                        100,
                        List.of(new Ability("court tres vite", 30))
                ));

                pool.addChampion(new Champion(
                        "Janna",
                        Roles.SUPPORT,
                        100,
                        List.of(new Ability("soin", 30))
                ));

                pool.addChampion(new Champion(
                        "Jinx",
                        Roles.TIREUR,
                        100,
                        List.of(new Ability("tir", 30))
                ));

                pool.addChampion(new Champion(
                        "Veigar",
                        Roles.MAGE,
                        100,
                        List.of(new Ability("boule de feu", 30))
                ));

                pool.addChampion(new Champion(
                        "Garen",
                        Roles.COMBATTANT,
                        100,
                        List.of(new Ability("coup d'épée", 30))
                ));

                pool.addChampion(new Champion(
                        "Zed",
                        Roles.ASSASSIN,
                        100,
                        List.of(new Ability("ombre", 30))
                ));

                pool.addChampion(new Champion(
                        "Soraka",
                        Roles.SUPPORT,
                        100,
                        List.of(new Ability("soin", 30))
                ));

                pool.addChampion(new Champion(
                        "Miss Fortune",
                        Roles.TIREUR,
                        100,
                        List.of(new Ability("tir", 30))
                ));

                pool.addChampion(new Champion(
                        "Lux",
                        Roles.MAGE,
                        100,
                        List.of(new Ability("laser", 30))
                ));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        @AfterEach
        void deleteChampionPool() {
            Game.getInstance().clear();
            ChampionPool.getInstance().clear();
        }

        @Test
        void testCreateTeam() {
            String requestBody = "{\"teamName\":\"team1\",\"champions\":[{\"lane\":\"TOP\",\"championName\":\"Hecharim\"},{\"lane\":\"MID\",\"championName\":\"Katarina\"},{\"lane\":\"BOTTOM\",\"championName\":\"Janna\"},{\"lane\":\"BOTTOM\",\"championName\":\"Jinx\"},{\"lane\":\"MID\",\"championName\":\"Veigar\"}]}";
            given()
                    .when()
                    .body(requestBody)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .post("/api/team")
                    .then()
                    .statusCode(200);
        }

        @Test
        void testCreateTeamTooManyChamps() {
            String requestBody = "{\"teamName\":\"team1\",\"champions\":[{\"lane\":\"TOP\",\"championName\":\"Hecharim\"},{\"lane\":\"MID\",\"championName\":\"Katarina\"},{\"lane\":\"BOTTOM\",\"championName\":\"Janna\"},{\"lane\":\"BOTTOM\",\"championName\":\"Jinx\"},{\"lane\":\"MID\",\"championName\":\"Veigar\"},{\"lane\":\"MID\",\"championName\":\"Garen\"}]}";
            given()
                    .when()
                    .body(requestBody)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .post("/api/team")
                    .then()
                    .statusCode(400);
        }

        @Test
        void testCreateTeamAndAddChamp() {
            String requestBody = "{\"teamName\":\"team1\",\"champions\":[{\"lane\":\"TOP\",\"championName\":\"Hecharim\"},{\"lane\":\"MID\",\"championName\":\"Katarina\"},{\"lane\":\"BOTTOM\",\"championName\":\"Janna\"},{\"lane\":\"BOTTOM\",\"championName\":\"Jinx\"}]}";
            given()
                    .when()
                    .body(requestBody)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .post("/api/team")
                    .then()
                    .statusCode(200);

            try {
                Team team = Game.getInstance().getTeamByName("team1");

                Assertions.assertEquals(4, team.getChampions().size());
            }catch (Exception e) {
                e.printStackTrace();
            }
            String requestBody2 = "{\"teamName\":\"team1\",\"championName\":\"Garen\",\"lane\":\"TOP\"}";
            given()
                    .when()
                    .body(requestBody2)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .patch("/api/team")
                    .then()
                    .statusCode(200);
            try {
                Team team = Game.getInstance().getTeamByName("team1");

                Assertions.assertEquals(5, team.getChampions().size());
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Nested
    @DisplayName("Test starting a game")
    class TestStartingGame {
        @BeforeEach
        void createChampionPool() {
           Game.getInstance().clear();

            ChampionPool pool = ChampionPool.getInstance();
            pool.clear();
            try {
                pool.addChampion(new Champion(
                        "Hecharim",
                        Roles.COMBATTANT,
                        100,
                        List.of(new Ability("buveuse d'âme", 30))
                ));

                pool.addChampion(new Champion(
                        "Katarina",
                        Roles.ASSASSIN,
                        100,
                        List.of(new Ability("court tres vite", 30))
                ));

                pool.addChampion(new Champion(
                        "Janna",
                        Roles.SUPPORT,
                        100,
                        List.of(new Ability("soin", 30))
                ));

                pool.addChampion(new Champion(
                        "Jinx",
                        Roles.TIREUR,
                        100,
                        List.of(new Ability("tir", 30))
                ));

                pool.addChampion(new Champion(
                        "Veigar",
                        Roles.MAGE,
                        100,
                        List.of(new Ability("boule de feu", 30))
                ));

                pool.addChampion(new Champion(
                        "Garen",
                        Roles.COMBATTANT,
                        100,
                        List.of(new Ability("coup d'épée", 30))
                ));

                pool.addChampion(new Champion(
                        "Zed",
                        Roles.ASSASSIN,
                        100,
                        List.of(new Ability("ombre", 30))
                ));

                pool.addChampion(new Champion(
                        "Soraka",
                        Roles.SUPPORT,
                        100,
                        List.of(new Ability("soin", 30))
                ));

                pool.addChampion(new Champion(
                        "Miss Fortune",
                        Roles.TIREUR,
                        100,
                        List.of(new Ability("tir", 30))
                ));

                pool.addChampion(new Champion(
                        "Lux",
                        Roles.MAGE,
                        100,
                        List.of(new Ability("laser", 30))
                ));

                Game.getInstance().addTeam(new Team("team1", List.of(
                        new Team.ChampionPick("Hecharim", Lanes.TOP),
                        new Team.ChampionPick("Katarina", Lanes.MID),
                        new Team.ChampionPick("Janna", Lanes.BOTTOM),
                        new Team.ChampionPick("Jinx", Lanes.BOTTOM),
                        new Team.ChampionPick("Veigar", Lanes.MID)
                )));

                Game.getInstance().addTeam(new Team("team2", List.of(
                        new Team.ChampionPick("Garen", Lanes.TOP),
                        new Team.ChampionPick("Zed", Lanes.MID),
                        new Team.ChampionPick("Soraka", Lanes.BOTTOM),
                        new Team.ChampionPick("Miss Fortune", Lanes.BOTTOM),
                        new Team.ChampionPick("Lux", Lanes.MID)
                )));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        @AfterEach
        void deleteChampionPool() {
            Game.getInstance().clear();
            ChampionPool.getInstance().clear();
        }

        @Test
        void testStartGame() {
            given()
                    .when()
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .post("/api/begin")
                    .then()
                    .statusCode(200);
        }

        @Test
        void testStartGameNotEnoughChamps() {
            try {

                Game.getInstance().clear();
                Game.getInstance().addTeam(new Team("team1", List.of(
                        new Team.ChampionPick("Hecharim", Lanes.TOP),
                        new Team.ChampionPick("Katarina", Lanes.MID),
                        new Team.ChampionPick("Janna", Lanes.BOTTOM),
                        new Team.ChampionPick("Jinx", Lanes.BOTTOM)
                )));

                Game.getInstance().addTeam(new Team("team2", List.of(
                        new Team.ChampionPick("Garen", Lanes.TOP),
                        new Team.ChampionPick("Zed", Lanes.MID),
                        new Team.ChampionPick("Soraka", Lanes.BOTTOM),
                        new Team.ChampionPick("Miss Fortune", Lanes.BOTTOM),
                        new Team.ChampionPick("Lux", Lanes.MID)
                )));

                given()
                        .when()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .post("/api/begin")
                        .then()
                        .statusCode(400);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Test
        void testStartGameNotEnoughTeams() {
            try {
                Game.getInstance().clear();
                Game.getInstance().addTeam(new Team("team1", List.of(
                        new Team.ChampionPick("Hecharim", Lanes.TOP),
                        new Team.ChampionPick("Katarina", Lanes.MID),
                        new Team.ChampionPick("Janna", Lanes.BOTTOM),
                        new Team.ChampionPick("Jinx", Lanes.BOTTOM),
                        new Team.ChampionPick("Veigar", Lanes.MID)
                )));

                given()
                        .when()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .post("/api/begin")
                        .then()
                        .statusCode(400);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Nested
    @DisplayName("Test US4")
    class TestUS4 {
        @BeforeEach
        void createChampionPool() {
            Game.getInstance().clear();

            ChampionPool pool = ChampionPool.getInstance();
            pool.clear();
            try {
                pool.addChampion(new Champion(
                        "Hecharim",
                        Roles.COMBATTANT,
                        100,
                        List.of(new Ability("buveuse d'âme", 30))
                ));

                pool.addChampion(new Champion(
                        "Katarina",
                        Roles.ASSASSIN,
                        100,
                        List.of(new Ability("court tres vite", 30))
                ));

                pool.addChampion(new Champion(
                        "Janna",
                        Roles.SUPPORT,
                        100,
                        List.of(new Ability("soin", 30))
                ));

                pool.addChampion(new Champion(
                        "Jinx",
                        Roles.TIREUR,
                        100,
                        List.of(new Ability("tir", 30))
                ));

                pool.addChampion(new Champion(
                        "Veigar",
                        Roles.MAGE,
                        100,
                        List.of(new Ability("boule de feu", 30))
                ));

                pool.addChampion(new Champion(
                        "Garen",
                        Roles.COMBATTANT,
                        100,
                        List.of(new Ability("coup d'épée", 30))
                ));

                pool.addChampion(new Champion(
                        "Zed",
                        Roles.ASSASSIN,
                        100,
                        List.of(new Ability("ombre", 30))
                ));

                pool.addChampion(new Champion(
                        "Soraka",
                        Roles.SUPPORT,
                        100,
                        List.of(new Ability("soin", 30))
                ));

                pool.addChampion(new Champion(
                        "Miss Fortune",
                        Roles.TIREUR,
                        100,
                        List.of(new Ability("tir", 30))
                ));

                pool.addChampion(new Champion(
                        "Lux",
                        Roles.MAGE,
                        100,
                        List.of(new Ability("laser", 30))
                ));

                Game.getInstance().addTeam(new Team("team1", List.of(
                        new Team.ChampionPick("Hecharim", Lanes.TOP),
                        new Team.ChampionPick("Katarina", Lanes.MID),
                        new Team.ChampionPick("Janna", Lanes.BOTTOM),
                        new Team.ChampionPick("Jinx", Lanes.BOTTOM),
                        new Team.ChampionPick("Veigar", Lanes.MID)
                )));

                Game.getInstance().addTeam(new Team("team2", List.of(
                        new Team.ChampionPick("Garen", Lanes.TOP),
                        new Team.ChampionPick("Zed", Lanes.MID),
                        new Team.ChampionPick("Soraka", Lanes.BOTTOM),
                        new Team.ChampionPick("Miss Fortune", Lanes.BOTTOM),
                        new Team.ChampionPick("Lux", Lanes.MID)
                )));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        @AfterEach()
        void clear() {
            Game.getInstance().clear();
            ChampionPool.getInstance().clear();
        }

        @Test
        void testGetting200WhenSearchingByLane() {
            given()
                    .when()
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .get("/api/searchByLane?lane=TOP")
                    .then()
                    .statusCode(200);

            Set<Champion> shouldBe = Set.of(
                    new Champion(
                            "Hecharim",
                            Roles.COMBATTANT,
                            100,
                            List.of(new Ability("buveuse d'âme", 30))
                    ),
                    new Champion(
                            "Garen",
                            Roles.COMBATTANT,
                            100,
                            List.of(new Ability("coup d'épée", 30))
                    )
            );

            Set<Champion> isActually = Game.getInstance().getChampions(Lanes.TOP).stream().map(champion -> {
                try {
                    return ChampionPool.getInstance().getChampion(champion.getChampionName());
                } catch (ChampionNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toSet());

            Assertions.assertEquals(shouldBe, isActually);

        }


    }
}