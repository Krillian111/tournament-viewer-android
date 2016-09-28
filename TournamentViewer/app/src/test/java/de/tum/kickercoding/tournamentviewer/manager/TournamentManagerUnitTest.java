package de.tum.kickercoding.tournamentviewer.manager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import de.tum.kickercoding.tournamentviewer.entities.Game;
import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.entities.Tournament;
import de.tum.kickercoding.tournamentviewer.exceptions.TournamentManagerException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(android.util.Log.class)
public class TournamentManagerUnitTest {

	TournamentManager tournamentManager;

	Method commitGame;

	Tournament tournament;

	@Before
	public void initialize() {
		tournamentManager = TournamentManager.getInstance();
		try {
			// reset the player list
			Field tournamentField = TournamentManager.class.getDeclaredField("currentTournament");
			tournamentField.setAccessible(true);
			tournament = new Tournament();
			tournamentField.set(tournamentManager, tournament);

			commitGame = TournamentManager.class.getDeclaredMethod("commitGame", Game.class);
			commitGame.setAccessible(true);

			PowerMockito.mockStatic(android.util.Log.class);
			when(android.util.Log.d(anyString(), anyString())).thenReturn(1);

			List<Player> participants = Arrays.asList(new Player("Player1"), new Player("Player2"), new
					Player("Player3"), new Player("Player4"));
			// add players to tournament so that accesses dont fail
			tournament.addPlayer(participants.get(0));
			tournament.addPlayer(participants.get(1));
			tournament.addPlayer(participants.get(2));
			tournament.addPlayer(participants.get(3));

		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@After
	public void cleanUp() {
		List<Player> participants = Arrays.asList(new Player("Player1"), new Player("Player2"), new
				Player("Player3"), new Player("Player4"));
		tournament.removePlayer(participants.get(0));
		tournament.removePlayer(participants.get(1));
		tournament.removePlayer(participants.get(2));
		tournament.removePlayer(participants.get(3));
	}

	@Test
	public void commitUnfinishedGame() {

		// one on one game
		List<Player> participants = Arrays.asList(new Player("Player1"), new Player("Player2"));
		try {
			commitGame.invoke(tournamentManager, new Game(participants));
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof TournamentManagerException) {
				return;
			}
		} catch (Exception e) {
			fail(e.toString());
		}
		fail("Commit of unfinished one on one game did not fail");

		// two on two game
		participants = Arrays.asList(new Player("Player1"), new Player("Player2"), new Player("Player3"), new Player
				("Player4"));
		try {
			commitGame.invoke(tournamentManager, new Game(participants));
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof TournamentManagerException) {
				return;
			}
		} catch (Exception e) {
			fail(e.toString());
		}
		fail("Commit of unfinished two on two game did not fail");
	}

	@Test
	public void commitFinishedGame() {
		setTournamentOneOnOne(true);
		// one on one game - tied
		List<Player> participants = Arrays.asList(new Player("Player1"), new Player("Player2"));
		// add players to tournament so that accesses dont fail
		Game game = new Game(participants);
		game.setScoreTeam1(5);
		game.setScoreTeam2(5);
		game.setFinished(true);
		try {
			commitGame.invoke(tournamentManager, game);
		} catch (Exception e) {
			fail("e.toString():" + e.toString() + "; e.getCause():" + e.getCause());
		}
		try {
			assertEquals(1, tournamentManager.getPlayerByName("Player1").getPlayedGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player1").getTiedGames());
			assertEquals(0, tournamentManager.getPlayerByName("Player1").getWonGames());
			assertEquals(0, tournamentManager.getPlayerByName("Player1").getLostGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player2").getPlayedGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player2").getTiedGames());
			assertEquals(0, tournamentManager.getPlayerByName("Player2").getWonGames());
			assertEquals(0, tournamentManager.getPlayerByName("Player2").getLostGames());
		} catch (TournamentManagerException e) {
			fail(e.toString());
		}

		// one on one game - winner: team 1
		participants = Arrays.asList(new Player("Player1"), new Player("Player2"));

		game = new Game(participants);
		game.setScoreTeam1(6);
		game.setScoreTeam2(5);
		game.setFinished(true);
		try {
			commitGame.invoke(tournamentManager, game);
		} catch (Exception e) {
			fail(e.toString());
		}
		try {
			assertEquals(2, tournamentManager.getPlayerByName("Player1").getPlayedGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player1").getTiedGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player1").getWonGames());
			assertEquals(0, tournamentManager.getPlayerByName("Player1").getLostGames());
			assertEquals(2, tournamentManager.getPlayerByName("Player2").getPlayedGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player2").getTiedGames());
			assertEquals(0, tournamentManager.getPlayerByName("Player2").getWonGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player2").getLostGames());
		} catch (TournamentManagerException e) {
			fail(e.toString());
		}

		// one on one game - winner: team 2
		participants = Arrays.asList(new Player("Player1"), new Player("Player2"));
		game = new Game(participants);
		game.setScoreTeam1(5);
		game.setScoreTeam2(6);
		game.setFinished(true);
		try {
			commitGame.invoke(tournamentManager, game);
		} catch (Exception e) {
			fail(e.toString());
		}
		try {
			assertEquals(3, tournamentManager.getPlayerByName("Player1").getPlayedGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player1").getTiedGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player1").getWonGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player1").getLostGames());
			assertEquals(3, tournamentManager.getPlayerByName("Player2").getPlayedGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player2").getTiedGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player2").getWonGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player2").getLostGames());
		} catch (TournamentManagerException e) {
			fail(e.toString());
		}


		setTournamentOneOnOne(false);
		// two on two game - tied
		participants = Arrays.asList(new Player("Player1"), new Player("Player2"), new Player("Player3"), new Player
				("Player4"));
		game = new Game(participants);
		game.setScoreTeam1(5);
		game.setScoreTeam2(5);
		game.setFinished(true);
		try {
			commitGame.invoke(tournamentManager, game);
		} catch (Exception e) {
			fail(e.toString());
		}
		try {
			assertEquals(4, tournamentManager.getPlayerByName("Player1").getPlayedGames());
			assertEquals(2, tournamentManager.getPlayerByName("Player1").getTiedGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player1").getWonGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player1").getLostGames());
			assertEquals(4, tournamentManager.getPlayerByName("Player2").getPlayedGames());
			assertEquals(2, tournamentManager.getPlayerByName("Player2").getTiedGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player2").getWonGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player2").getLostGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player3").getPlayedGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player3").getTiedGames());
			assertEquals(0, tournamentManager.getPlayerByName("Player3").getWonGames());
			assertEquals(0, tournamentManager.getPlayerByName("Player3").getLostGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player4").getPlayedGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player4").getTiedGames());
			assertEquals(0, tournamentManager.getPlayerByName("Player4").getWonGames());
			assertEquals(0, tournamentManager.getPlayerByName("Player4").getLostGames());
		} catch (TournamentManagerException e) {
			fail(e.toString());
		}

		// two on two game - winner: team 1
		participants = Arrays.asList(new Player("Player1"), new Player("Player2"), new Player("Player3"), new Player
				("Player4"));
		game = new Game(participants);
		game.setScoreTeam1(6);
		game.setScoreTeam2(5);
		game.setFinished(true);
		try {
			commitGame.invoke(tournamentManager, game);
		} catch (Exception e) {
			fail(e.toString());
		}
		try {
			assertEquals(5, tournamentManager.getPlayerByName("Player1").getPlayedGames());
			assertEquals(2, tournamentManager.getPlayerByName("Player1").getTiedGames());
			assertEquals(2, tournamentManager.getPlayerByName("Player1").getWonGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player1").getLostGames());
			assertEquals(5, tournamentManager.getPlayerByName("Player2").getPlayedGames());
			assertEquals(2, tournamentManager.getPlayerByName("Player2").getTiedGames());
			assertEquals(2, tournamentManager.getPlayerByName("Player2").getWonGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player2").getLostGames());
			assertEquals(2, tournamentManager.getPlayerByName("Player3").getPlayedGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player3").getTiedGames());
			assertEquals(0, tournamentManager.getPlayerByName("Player3").getWonGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player3").getLostGames());
			assertEquals(2, tournamentManager.getPlayerByName("Player4").getPlayedGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player4").getTiedGames());
			assertEquals(0, tournamentManager.getPlayerByName("Player4").getWonGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player4").getLostGames());
		} catch (TournamentManagerException e) {
			fail(e.toString());
		}

		// two on two game - winner: team 2
		participants = Arrays.asList(new Player("Player1"), new Player("Player2"), new Player("Player3"), new Player
				("Player4"));
		game = new Game(participants);
		game.setScoreTeam1(5);
		game.setScoreTeam2(6);
		game.setFinished(true);
		try {
			commitGame.invoke(tournamentManager, game);
		} catch (Exception e) {
			fail(e.toString());
		}
		try {
			assertEquals(6, tournamentManager.getPlayerByName("Player1").getPlayedGames());
			assertEquals(2, tournamentManager.getPlayerByName("Player1").getTiedGames());
			assertEquals(2, tournamentManager.getPlayerByName("Player1").getWonGames());
			assertEquals(2, tournamentManager.getPlayerByName("Player1").getLostGames());
			assertEquals(6, tournamentManager.getPlayerByName("Player2").getPlayedGames());
			assertEquals(2, tournamentManager.getPlayerByName("Player2").getTiedGames());
			assertEquals(2, tournamentManager.getPlayerByName("Player2").getWonGames());
			assertEquals(2, tournamentManager.getPlayerByName("Player2").getLostGames());
			assertEquals(3, tournamentManager.getPlayerByName("Player3").getPlayedGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player3").getTiedGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player3").getWonGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player3").getLostGames());
			assertEquals(3, tournamentManager.getPlayerByName("Player4").getPlayedGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player4").getTiedGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player4").getWonGames());
			assertEquals(1, tournamentManager.getPlayerByName("Player4").getLostGames());
		} catch (TournamentManagerException e) {
			fail(e.toString());
		}
	}

	private void setTournamentOneOnOne(boolean oneOnOne) {
		try {
			Field oneOnOneField = Tournament.class.getDeclaredField("oneOnOne");
			oneOnOneField.setAccessible(true);
			oneOnOneField.setBoolean(tournament, oneOnOne);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
}
