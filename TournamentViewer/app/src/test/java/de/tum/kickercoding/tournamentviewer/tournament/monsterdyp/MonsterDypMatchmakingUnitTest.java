package de.tum.kickercoding.tournamentviewer.tournament.monsterdyp;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.tum.kickercoding.tournamentviewer.entities.Game;
import de.tum.kickercoding.tournamentviewer.entities.Player;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MonsterDypMatchmakingUnitTest {

	@Test
	public void distinctPlayersMatched2on2() {
		// prepare input
		Player p1 = new Player("p1", 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.0);
		Player p2 = new Player("p2", 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0.0);
		Player p3 = new Player("p3", 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0.0);
		Player p4 = new Player("p4", 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0.0);
		Player p5 = new Player("p5", 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0.0);
		Player p6 = new Player("p6", 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0.0);
		Player p7 = new Player("p7", 4, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0.0);
		Player p8 = new Player("p8", 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0.0);
		Player p9 = new Player("p9", 5, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0.0);
		List<Player> players = new ArrayList<>();
		players.add(p1);
		players.add(p2);
		players.add(p3);
		players.add(p4);
		players.add(p5);
		players.add(p6);
		players.add(p7);
		players.add(p8);
		players.add(p9);
		List<Game> games = MonsterDypMatchmaking.getInstance().generateRound(players, false);
		for (Game game : games) {
			for (Player p : game.getTeam1()) {
				players.remove(p);
			}
			for (Player p : game.getTeam2()) {
				players.remove(p);
			}
		}
		assertTrue(players.size() == 1);
		assertTrue(players.get(0).equals(p9));
	}

	@Test
	public void distinctPlayersMatched1on1() {
		// prepare input
		Player p1 = new Player("p1", 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.0);
		Player p2 = new Player("p2", 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0.0);
		Player p3 = new Player("p3", 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0.0);
		Player p4 = new Player("p4", 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0.0);
		Player p5 = new Player("p5", 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0.0);
		List<Player> players = new ArrayList<>();
		players.add(p1);
		players.add(p2);
		players.add(p3);
		players.add(p4);
		players.add(p5);
		List<Game> games = MonsterDypMatchmaking.getInstance().generateRound(players, true);
		for (Game game : games) {
			for (Player p : game.getTeam1()) {
				players.remove(p);
			}
			for (Player p : game.getTeam2()) {
				players.remove(p);
			}
		}
		assertTrue(players.size() == 1);
		assertTrue(players.get(0).equals(p5));
	}

	@Test
	public void selectPlayersWithLeastGames2on2() {
		// prepare input
		Player p1 = new Player("p1", 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.0);
		Player p2 = new Player("p2", 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0.0);
		Player p3 = new Player("p3", 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0.0);
		Player p4 = new Player("p4", 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0.0);
		Player p5 = new Player("p5", 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0.0);
		Player p6 = new Player("p6", 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0.0);
		List<Player> players = new ArrayList<>();
		players.add(p1);
		players.add(p2);
		players.add(p3);
		players.add(p4);
		players.add(p5);
		players.add(p6);
		// get private method
		MonsterDypMatchmaking matchmaking = MonsterDypMatchmaking.getInstance();
		Method methodSelectPlayers = null;
		try {
			methodSelectPlayers = matchmaking.getClass().getDeclaredMethod("selectPlayers", List.class, boolean
					.class, boolean.class);
		} catch (NoSuchMethodException e) {
			fail(e.toString());
			return;
		}
		// invoke method
		List<Player> list = null;
		try {
			methodSelectPlayers.setAccessible(true);
			list = (List) methodSelectPlayers.invoke(matchmaking, players, false, false);

		} catch (IllegalAccessException e) {
			fail(e.toString());
		} catch (InvocationTargetException e) {
			fail(e.toString());
		}
		// check result
		assertTrue(list.contains(p1));
		assertTrue(list.contains(p2));
		assertTrue(list.contains(p3));
		assertTrue(list.contains(p4));
		assertFalse(list.contains(p5));
		assertFalse(list.contains(p6));
	}

	@Test
	public void selectPlayersWithLeastGames1on1() {
		// prepare input
		Player p1 = new Player("p1", 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.0);
		Player p2 = new Player("p2", 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0.0);
		Player p3 = new Player("p3", 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0.0);
		List<Player> players = new ArrayList<>();
		players.add(p1);
		players.add(p2);
		players.add(p3);
		// get private method
		MonsterDypMatchmaking matchmaking = MonsterDypMatchmaking.getInstance();
		Method methodSelectPlayers = null;
		try {
			methodSelectPlayers = matchmaking.getClass().getDeclaredMethod("selectPlayers", List.class, boolean
					.class, boolean.class);
		} catch (NoSuchMethodException e) {
			fail(e.toString());
			return;
		}
		// invoke method
		List<Player> list = null;
		try {
			methodSelectPlayers.setAccessible(true);
			list = (List) methodSelectPlayers.invoke(matchmaking, players, true, false);

		} catch (IllegalAccessException e) {
			fail(e.toString());
		} catch (InvocationTargetException e) {
			fail(e.toString() + ", cause:" + e.getCause());
		}
		// check result
		assertTrue(list.contains(p1));
		assertTrue(list.contains(p2));
		assertFalse(list.contains(p3));
	}
}
