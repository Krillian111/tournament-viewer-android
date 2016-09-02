package de.tum.kickercoding.tournamentviewer.manager;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
    public void addPlayer() throws Exception {
        for(int i = 0; i < 10; i++) {
            playerManager.addPlayer("TestPlayer" + i);
            assertEquals(i + 1, playerManager.getNumberOfPlayers());
        }
    }

    @Test
    public void addDuplicatedPlayer() throws Exception {
        playerManager.addPlayer("TestPlayer");
        try {
            playerManager.addPlayer("TestPlayer");
        } catch (PlayerManagerException e) {
            return;
        }
        fail("Duplicated player not recognized");
    }

    @Test
    public void addPlayerWithPipe() throws Exception {
        try {
            playerManager.addPlayer("TestP|ayer");
        } catch (PlayerManagerException e) {
            return;
        }
        fail("| (pipe symbol) not allowed in player name");
    }

    @Test
    public void removePlayer() throws Exception {
        for(int i = 0; i < 10; i++) {
            playerManager.addPlayer("TestPlayer" + i);
            playerManager.addPlayer("TestPlayer" + i + i);
            playerManager.addPlayer("TestPlayer" + i + i + i);

            playerManager.removePlayer("TestPlayer" + i + i);
            assertEquals(i + 2, playerManager.getNumberOfPlayers());

            playerManager.removePlayer("TestPlayer" + i + i + i);
            assertEquals(i + 1, playerManager.getNumberOfPlayers());
        }
    }

    @Test
    public void removeNonExistingPlayer() throws Exception {
        playerManager.addPlayer("TestPlayer");
        try {
            playerManager.removePlayer("NonExistingPlayer");
        } catch (PlayerManagerException e) {
            return;
        }
        fail("Removing non-existing player did not fail");
    }

    @Test
    public void commitUnfinishedGame() throws Exception {
        // one on one game
        List<Player> participants = new ArrayList<Player>();
        participants.add(new Player("Player1"));
        participants.add(new Player("Player2"));
        try {
            playerManager.commitGameResult(new Game(participants));
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("Commit of unfinished one on one game did not fail");

        // two on two game
        participants.clear();
        participants.add(new Player("Player1"));
        participants.add(new Player("Player2"));
        participants.add(new Player("Player3"));
        participants.add(new Player("Player4"));
        try {
            playerManager.commitGameResult(new Game(participants));
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("Commit of unfinished two on two game did not fail");
    }

    @Test
    public void commitFinishedGame() throws Exception {
        List<Player> participants;
        Player player1;
        Player player2;
        Player player3;
        Player player4;
        Game game;

        // one on one game - tied
        participants = new ArrayList<Player>();
        player1 = new Player("Player1");
        player2 = new Player("Player2");
        participants.add(player1);
        participants.add(player2);
        game = new Game(participants);
        game.setScoreTeam1(5);
        game.setScoreTeam1(5);
        game.setFinished(true);
        playerManager.commitGameResult(game);
        assertEquals(1, player1.getPlayedGames());
        assertEquals(1, player1.getTiedGames());
        assertEquals(0, player1.getWonGames());
        assertEquals(0, player1.getLostGames());
        assertEquals(1, player2.getPlayedGames());
        assertEquals(1, player2.getTiedGames());
        assertEquals(0, player2.getWonGames());
        assertEquals(0, player2.getLostGames());

        // one on one game - winner: team 1
        participants = new ArrayList<Player>();
        player1 = new Player("Player1");
        player2 = new Player("Player2");
        participants.add(player1);
        participants.add(player2);
        game = new Game(participants);
        game.setScoreTeam1(6);
        game.setScoreTeam1(5);
        game.setFinished(true);
        playerManager.commitGameResult(game);
        assertEquals(1, player1.getPlayedGames());
        assertEquals(0, player1.getTiedGames());
        assertEquals(1, player1.getWonGames());
        assertEquals(0, player1.getLostGames());
        assertEquals(1, player1.getPlayedGames());
        assertEquals(0, player1.getTiedGames());
        assertEquals(0, player1.getWonGames());
        assertEquals(1, player1.getLostGames());

        // one on one game - winner: team 2
        participants = new ArrayList<Player>();
        player1 = new Player("Player1");
        player2 = new Player("Player2");
        participants.add(player1);
        participants.add(player2);
        game = new Game(participants);
        game.setScoreTeam1(5);
        game.setScoreTeam1(6);
        game.setFinished(true);
        playerManager.commitGameResult(game);
        assertEquals(1, player1.getPlayedGames());
        assertEquals(0, player1.getTiedGames());
        assertEquals(0, player1.getWonGames());
        assertEquals(1, player1.getLostGames());
        assertEquals(1, player1.getPlayedGames());
        assertEquals(0, player1.getTiedGames());
        assertEquals(1, player1.getWonGames());
        assertEquals(0, player1.getLostGames());

        // one on one game - tied
        participants = new ArrayList<Player>();
        player1 = new Player("Player1");
        player2 = new Player("Player2");
        player3 = new Player("Player3");
        player4 = new Player("Player4");
        participants.add(player1);
        participants.add(player2);
        participants.add(player3);
        participants.add(player4);
        game = new Game(participants);
        game.setScoreTeam1(5);
        game.setScoreTeam1(5);
        game.setFinished(true);
        playerManager.commitGameResult(game);
        assertEquals(1, player1.getPlayedGames());
        assertEquals(1, player1.getTiedGames());
        assertEquals(0, player1.getWonGames());
        assertEquals(0, player1.getLostGames());
        assertEquals(1, player2.getPlayedGames());
        assertEquals(1, player2.getTiedGames());
        assertEquals(0, player2.getWonGames());
        assertEquals(0, player2.getLostGames());
        assertEquals(1, player3.getPlayedGames());
        assertEquals(1, player3.getTiedGames());
        assertEquals(0, player3.getWonGames());
        assertEquals(0, player3.getLostGames());
        assertEquals(1, player4.getPlayedGames());
        assertEquals(1, player4.getTiedGames());
        assertEquals(0, player4.getWonGames());
        assertEquals(0, player4.getLostGames());

        // one on one game - winner: team 1
        participants = new ArrayList<Player>();
        player1 = new Player("Player1");
        player2 = new Player("Player2");
        player3 = new Player("Player3");
        player4 = new Player("Player4");
        participants.add(player1);
        participants.add(player2);
        participants.add(player3);
        participants.add(player4);
        game = new Game(participants);
        game.setScoreTeam1(6);
        game.setScoreTeam1(5);
        game.setFinished(true);
        playerManager.commitGameResult(game);
        assertEquals(1, player1.getPlayedGames());
        assertEquals(0, player1.getTiedGames());
        assertEquals(1, player1.getWonGames());
        assertEquals(0, player1.getLostGames());
        assertEquals(1, player2.getPlayedGames());
        assertEquals(0, player2.getTiedGames());
        assertEquals(1, player2.getWonGames());
        assertEquals(0, player2.getLostGames());
        assertEquals(1, player3.getPlayedGames());
        assertEquals(0, player3.getTiedGames());
        assertEquals(0, player3.getWonGames());
        assertEquals(1, player3.getLostGames());
        assertEquals(1, player4.getPlayedGames());
        assertEquals(0, player4.getTiedGames());
        assertEquals(0, player4.getWonGames());
        assertEquals(1, player4.getLostGames());

        // one on one game - winner: team 2
        participants = new ArrayList<Player>();
        player1 = new Player("Player1");
        player2 = new Player("Player2");
        player3 = new Player("Player3");
        player4 = new Player("Player4");
        participants.add(player1);
        participants.add(player2);
        participants.add(player3);
        participants.add(player4);
        game = new Game(participants);
        game.setScoreTeam1(5);
        game.setScoreTeam1(6);
        game.setFinished(true);
        playerManager.commitGameResult(game);
        assertEquals(1, player1.getPlayedGames());
        assertEquals(0, player1.getTiedGames());
        assertEquals(0, player1.getWonGames());
        assertEquals(1, player1.getLostGames());
        assertEquals(1, player2.getPlayedGames());
        assertEquals(0, player2.getTiedGames());
        assertEquals(0, player2.getWonGames());
        assertEquals(1, player2.getLostGames());
        assertEquals(1, player3.getPlayedGames());
        assertEquals(0, player3.getTiedGames());
        assertEquals(1, player3.getWonGames());
        assertEquals(0, player3.getLostGames());
        assertEquals(1, player4.getPlayedGames());
        assertEquals(0, player4.getTiedGames());
        assertEquals(1, player4.getWonGames());
        assertEquals(0, player4.getLostGames());
    }
}