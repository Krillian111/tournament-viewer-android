package de.tum.kickercoding.tournamentviewer.manager;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import de.tum.kickercoding.tournamentviewer.Constants;
import de.tum.kickercoding.tournamentviewer.entities.Game;
import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.exceptions.PlayerManagerException;

import static org.junit.Assert.*;


public class PlayerManagerUnitTest {

    private PlayerManager playerManager;

    @Before
    public void initialize() {
        playerManager = PlayerManager.getInstance();
        try {
            // reset the player list
            Field privatePlayerList = PlayerManager.class.getDeclaredField("players");
            privatePlayerList.setAccessible(true);
            privatePlayerList.set(playerManager, new ArrayList<Player>());
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void addPlayer() {
        for(int i = 0; i < 10; i++) {
            try {
                playerManager.addPlayer("TestPlayer" + i);
            } catch (PlayerManagerException e) {
                fail(e.toString());
            }
            assertEquals(i + 1, playerManager.getNumberOfPlayers());
        }
    }

    @Test
    public void addDuplicatedPlayer() {
        try {
            playerManager.addPlayer("TestPlayer");
        } catch (PlayerManagerException e) {
            fail(e.toString());
        }
        try {
            playerManager.addPlayer("TestPlayer");
        } catch (PlayerManagerException e) {
            return;
        }
        fail("Duplicated player not recognized");
    }

    @Test
    public void addPlayerWithDelimiter(){
        try {
            playerManager.addPlayer("TestPlayer" + Constants.DELIMITER);
        } catch (PlayerManagerException e) {
            return;
        }
        fail(String.format("Symbol %s not allowed in player name", Constants.DELIMITER));
    }

    @Test
    public void removePlayer() {
        for(int i = 0; i < 10; i++) {
            try {
                playerManager.addPlayer("TestPlayer" + i);
                playerManager.addPlayer("TestPlayer" + i + i);
                playerManager.addPlayer("TestPlayer" + i + i + i);

                playerManager.removePlayer("TestPlayer" + i + i);
                assertEquals(i + 2, playerManager.getNumberOfPlayers());

                playerManager.removePlayer("TestPlayer" + i + i + i);
                assertEquals(i + 1, playerManager.getNumberOfPlayers());
            } catch (PlayerManagerException e) {
                fail(e.toString());
            }
        }
    }

    @Test
    public void removeNonExistingPlayer(){
        try {
            playerManager.addPlayer("TestPlayer");
        } catch (PlayerManagerException e) {
            fail(e.toString());
        }
        try {
            playerManager.removePlayer("NonExistingPlayer");
        } catch (PlayerManagerException e) {
            return;
        }
        fail("Removing non-existing player did not fail");
    }

    @Test
    public void commitUnfinishedGame() {
        // one on one game
        Player[] participants = new Player[]{new Player("Player1"),new Player("Player2")};
        try {
            playerManager.commitGameResult(new Game(participants));
        } catch (PlayerManagerException e) {
            return;
        }
        fail("Commit of unfinished one on one game did not fail");

        // two on two game
        participants = new Player[]{new Player("Player1"),new Player("Player2"),new Player("Player3"),new Player("Player4")};
        try {
            playerManager.commitGameResult(new Game(participants));
        } catch (PlayerManagerException e) {
            return;
        }
        fail("Commit of unfinished two on two game did not fail");
    }

    @Test
    public void commitFinishedGame() {
        try {
            playerManager.addPlayer("Player1");
            playerManager.addPlayer("Player2");
            playerManager.addPlayer("Player3");
            playerManager.addPlayer("Player4");
        } catch (PlayerManagerException e) {
            fail(e.toString());
        }

        Player[] participants;
        Player player1;
        Player player2;
        Player player3;
        Player player4;
        Game game;

        // one on one game - tied
        participants = new Player[]{new Player("Player1"),new Player("Player2")};

        game = new Game(participants);
        game.setScoreTeam1(5);
        game.setScoreTeam2(5);
        game.setFinished(true);
        try {
            playerManager.commitGameResult(game);
            assertEquals(1, playerManager.getPlayerByName("Player1").getPlayedGames());
            assertEquals(1, playerManager.getPlayerByName("Player1").getTiedGames());
            assertEquals(0, playerManager.getPlayerByName("Player1").getWonGames());
            assertEquals(0, playerManager.getPlayerByName("Player1").getLostGames());
            assertEquals(1, playerManager.getPlayerByName("Player2").getPlayedGames());
            assertEquals(1, playerManager.getPlayerByName("Player2").getTiedGames());
            assertEquals(0, playerManager.getPlayerByName("Player2").getWonGames());
            assertEquals(0, playerManager.getPlayerByName("Player2").getLostGames());
        } catch (PlayerManagerException e) {
            fail(e.toString());
        }

        // one on one game - winner: team 1
        participants = new Player[]{new Player("Player1"),new Player("Player2")};
        game = new Game(participants);
        game.setScoreTeam1(6);
        game.setScoreTeam2(5);
        game.setFinished(true);
        try {
            playerManager.commitGameResult(game);
            assertEquals(2, playerManager.getPlayerByName("Player1").getPlayedGames());
            assertEquals(1, playerManager.getPlayerByName("Player1").getTiedGames());
            assertEquals(1, playerManager.getPlayerByName("Player1").getWonGames());
            assertEquals(0, playerManager.getPlayerByName("Player1").getLostGames());
            assertEquals(2, playerManager.getPlayerByName("Player2").getPlayedGames());
            assertEquals(1, playerManager.getPlayerByName("Player2").getTiedGames());
            assertEquals(0, playerManager.getPlayerByName("Player2").getWonGames());
            assertEquals(1, playerManager.getPlayerByName("Player2").getLostGames());
        } catch (PlayerManagerException e) {
            fail(e.toString());
        }

        // one on one game - winner: team 2
        participants = new Player[]{new Player("Player1"),new Player("Player2")};
        game = new Game(participants);
        game.setScoreTeam1(5);
        game.setScoreTeam2(6);
        game.setFinished(true);
        try {
            playerManager.commitGameResult(game);
            assertEquals(3, playerManager.getPlayerByName("Player1").getPlayedGames());
            assertEquals(1, playerManager.getPlayerByName("Player1").getTiedGames());
            assertEquals(1, playerManager.getPlayerByName("Player1").getWonGames());
            assertEquals(1, playerManager.getPlayerByName("Player1").getLostGames());
            assertEquals(3, playerManager.getPlayerByName("Player2").getPlayedGames());
            assertEquals(1, playerManager.getPlayerByName("Player2").getTiedGames());
            assertEquals(1, playerManager.getPlayerByName("Player2").getWonGames());
            assertEquals(1, playerManager.getPlayerByName("Player2").getLostGames());
        } catch (PlayerManagerException e) {
            fail(e.toString());
        }

        // one on one game - tied
        participants = new Player[]{new Player("Player1"),new Player("Player2"),new Player("Player3"),new Player("Player4")};
        game = new Game(participants);
        game.setScoreTeam1(5);
        game.setScoreTeam2(5);
        game.setFinished(true);
        try {
            playerManager.commitGameResult(game);
            assertEquals(4, playerManager.getPlayerByName("Player1").getPlayedGames());
            assertEquals(2, playerManager.getPlayerByName("Player1").getTiedGames());
            assertEquals(1, playerManager.getPlayerByName("Player1").getWonGames());
            assertEquals(1, playerManager.getPlayerByName("Player1").getLostGames());
            assertEquals(4, playerManager.getPlayerByName("Player2").getPlayedGames());
            assertEquals(2, playerManager.getPlayerByName("Player2").getTiedGames());
            assertEquals(1, playerManager.getPlayerByName("Player2").getWonGames());
            assertEquals(1, playerManager.getPlayerByName("Player2").getLostGames());
            assertEquals(1, playerManager.getPlayerByName("Player3").getPlayedGames());
            assertEquals(1, playerManager.getPlayerByName("Player3").getTiedGames());
            assertEquals(0, playerManager.getPlayerByName("Player3").getWonGames());
            assertEquals(0, playerManager.getPlayerByName("Player3").getLostGames());
            assertEquals(1, playerManager.getPlayerByName("Player4").getPlayedGames());
            assertEquals(1, playerManager.getPlayerByName("Player4").getTiedGames());
            assertEquals(0, playerManager.getPlayerByName("Player4").getWonGames());
            assertEquals(0, playerManager.getPlayerByName("Player4").getLostGames());
        } catch (PlayerManagerException e) {
            fail(e.toString());
        }

        // one on one game - winner: team 1
        participants = new Player[]{new Player("Player1"),new Player("Player2"),new Player("Player3"),new Player("Player4")};
        game = new Game(participants);
        game.setScoreTeam1(6);
        game.setScoreTeam2(5);
        game.setFinished(true);
        try {
            playerManager.commitGameResult(game);
            assertEquals(5, playerManager.getPlayerByName("Player1").getPlayedGames());
            assertEquals(2, playerManager.getPlayerByName("Player1").getTiedGames());
            assertEquals(2, playerManager.getPlayerByName("Player1").getWonGames());
            assertEquals(1, playerManager.getPlayerByName("Player1").getLostGames());
            assertEquals(5, playerManager.getPlayerByName("Player2").getPlayedGames());
            assertEquals(2, playerManager.getPlayerByName("Player2").getTiedGames());
            assertEquals(2, playerManager.getPlayerByName("Player2").getWonGames());
            assertEquals(1, playerManager.getPlayerByName("Player2").getLostGames());
            assertEquals(2, playerManager.getPlayerByName("Player3").getPlayedGames());
            assertEquals(1, playerManager.getPlayerByName("Player3").getTiedGames());
            assertEquals(0, playerManager.getPlayerByName("Player3").getWonGames());
            assertEquals(1, playerManager.getPlayerByName("Player3").getLostGames());
            assertEquals(2, playerManager.getPlayerByName("Player4").getPlayedGames());
            assertEquals(1, playerManager.getPlayerByName("Player4").getTiedGames());
            assertEquals(0, playerManager.getPlayerByName("Player4").getWonGames());
            assertEquals(1, playerManager.getPlayerByName("Player4").getLostGames());
        } catch (PlayerManagerException e) {
            fail(e.toString());
        }

        // one on one game - winner: team 2
        participants = new Player[]{new Player("Player1"),new Player("Player2"),new Player("Player3"),new Player("Player4")};
        game = new Game(participants);
        game.setScoreTeam1(5);
        game.setScoreTeam2(6);
        game.setFinished(true);
        try {
            playerManager.commitGameResult(game);
            assertEquals(6, playerManager.getPlayerByName("Player1").getPlayedGames());
            assertEquals(2, playerManager.getPlayerByName("Player1").getTiedGames());
            assertEquals(2, playerManager.getPlayerByName("Player1").getWonGames());
            assertEquals(2, playerManager.getPlayerByName("Player1").getLostGames());
            assertEquals(6, playerManager.getPlayerByName("Player2").getPlayedGames());
            assertEquals(2, playerManager.getPlayerByName("Player2").getTiedGames());
            assertEquals(2, playerManager.getPlayerByName("Player2").getWonGames());
            assertEquals(2, playerManager.getPlayerByName("Player2").getLostGames());
            assertEquals(3, playerManager.getPlayerByName("Player3").getPlayedGames());
            assertEquals(1, playerManager.getPlayerByName("Player3").getTiedGames());
            assertEquals(1, playerManager.getPlayerByName("Player3").getWonGames());
            assertEquals(1, playerManager.getPlayerByName("Player3").getLostGames());
            assertEquals(3, playerManager.getPlayerByName("Player4").getPlayedGames());
            assertEquals(1, playerManager.getPlayerByName("Player4").getTiedGames());
            assertEquals(1, playerManager.getPlayerByName("Player4").getWonGames());
            assertEquals(1, playerManager.getPlayerByName("Player4").getLostGames());
        } catch (PlayerManagerException e) {
            fail(e.toString());
        }
    }
}