package de.tum.krillian.tournamentviewer;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import de.tum.krillian.tournamentviewer.entities.Game;
import de.tum.krillian.tournamentviewer.entities.Player;
import de.tum.krillian.tournamentviewer.exceptions.PlayerManagerException;
import de.tum.krillian.tournamentviewer.manager.PlayerManager;

import static org.junit.Assert.*;

public class PlayerManagerUnitTest {

    @Before
    public void initialize() {
        PlayerManager.clearPlayerList();
    }

    @Test
    public void addPlayer() throws Exception {
        for(int i = 0; i < 10; i++) {
            PlayerManager.addPlayer("TestPlayer" + i);
            assertEquals(i + 1, PlayerManager.getNumberOfPlayers());
        }
    }

    @Test
    public void addDuplicatedPlayer() throws Exception {
        PlayerManager.addPlayer("TestPlayer");
        try {
            PlayerManager.addPlayer("TestPlayer");
        } catch (PlayerManagerException e) {
            return;
        }
        fail("Duplicated player not recognized");
    }

    @Test
    public void addPlayerWithPipe() throws Exception {
        try {
            PlayerManager.addPlayer("TestP|ayer");
        } catch (PlayerManagerException e) {
            return;
        }
        fail("| (pipe symbol) not allowed in player name");
    }

    @Test
    public void removePlayer() throws Exception {
        for(int i = 0; i < 10; i++) {
            PlayerManager.addPlayer("TestPlayer" + i);
            PlayerManager.addPlayer("TestPlayer" + i + i);
            PlayerManager.addPlayer("TestPlayer" + i + i + i);

            PlayerManager.removePlayer("TestPlayer" + i + i);
            assertEquals(i + 2, PlayerManager.getNumberOfPlayers());

            PlayerManager.removePlayer("TestPlayer" + i + i + i);
            assertEquals(i + 1, PlayerManager.getNumberOfPlayers());
        }
    }

    @Test
    public void removeNonExistingPlayer() throws Exception {
        PlayerManager.addPlayer("TestPlayer");
        try {
            PlayerManager.removePlayer("NonExistingPlayer");
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
            PlayerManager.commitGameResult(new Game(participants));
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
            PlayerManager.commitGameResult(new Game(participants));
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
        PlayerManager.commitGameResult(game);
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
        PlayerManager.commitGameResult(game);
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
        PlayerManager.commitGameResult(game);
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
        PlayerManager.commitGameResult(game);
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
        PlayerManager.commitGameResult(game);
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
        PlayerManager.commitGameResult(game);
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