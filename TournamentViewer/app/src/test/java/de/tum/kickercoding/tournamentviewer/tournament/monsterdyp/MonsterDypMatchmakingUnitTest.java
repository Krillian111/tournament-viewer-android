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
		List<Game> games = MonsterDypMatchmaking.getInstance().generateRound(players, false, new ArrayList<Game>());
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
		List<Game> games = MonsterDypMatchmaking.getInstance().generateRound(players, true, new ArrayList<Game>());
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

	// test for generating sample distribution (console output) for 16 players (uncomment if needed and set
	// generateTeam to public)
	// can be used to fiddle with parameters (std,avg) of gaussian
	/*@Test
	public void testDistributionForPartners() {
		// goals shot encodes player
		Player p0 = new Player("p0", 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);//100% win rate
		Player p1 = new Player("p1", 9, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0);// 95%
		Player p2 = new Player("p2", 8, 2, 0, 0, 0, 0, 2, 0, 0, 0, 0);// 90%
		Player p3 = new Player("p3", 7, 3, 0, 0, 0, 0, 3, 0, 0, 0, 0);// 85%
		Player p4 = new Player("p4", 6, 4, 0, 0, 0, 0, 4, 0, 0, 0, 0);// 80%
		Player p5 = new Player("p5", 5, 5, 0, 0, 0, 0, 5, 0, 0, 0, 0);// 75%
		Player p6 = new Player("p6", 4, 6, 0, 0, 0, 0, 6, 0, 0, 0, 0);// 70%
		Player p7 = new Player("p7", 3, 7, 0, 0, 0, 0, 7, 0, 0, 0, 0);// 65%
		Player p8 = new Player("p8", 10, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0);//60% win rate
		Player p9 = new Player("p9", 9, 1, 0, 0, 0, 0, 9, 0, 0, 0, 0);// 55%
		Player p10 = new Player("p10", 8, 2, 0, 0, 0, 0, 10, 0, 0, 0, 0);// 50%
		Player p11 = new Player("p11", 7, 3, 0, 0, 0, 0, 11, 0, 0, 0, 0);// 45%
		Player p12 = new Player("p12", 6, 4, 0, 0, 0, 0, 12, 0, 0, 0, 0);// 40%
		Player p13 = new Player("p13", 5, 5, 0, 0, 0, 0, 13, 0, 0, 0, 0);// 35%
		Player p14 = new Player("p14", 4, 6, 0, 0, 0, 0, 14, 0, 0, 0, 0);// 30%
		Player p15 = new Player("p15", 3, 7, 0, 0, 0, 0, 15, 0, 0, 0, 0);// 20%
		List<Player> players = new ArrayList<>(Arrays.asList(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12,
				p13, p14, p15));
		int nPlayers = 16;
		int[] freq = new int[nPlayers];
		int[][] freqPerPlayer = new int[nPlayers][nPlayers];
		for (int i = 0;i < 1000;i++) {
			List<Player> tempPlayers = new ArrayList<>(players);
			while (tempPlayers.size() > 1) {
				List<Player> generatedTeam = MonsterDypMatchmaking.getInstance().generateTeam(tempPlayers);
				// goals shot encodes player
				int firstPlayer = generatedTeam.get(0).getGoalsShot();
				int secondPlayer = generatedTeam.get(1).getGoalsShot();
				freq[firstPlayer]++;
				freqPerPlayer[firstPlayer][secondPlayer]++;
				freq[secondPlayer]++;
				freqPerPlayer[secondPlayer][firstPlayer]++;
				tempPlayers.removeAll(generatedTeam);
			}
		}
		for (int i = 0;i < nPlayers;i++) {
			System.out.println("Player" + i + ":" + freq[i]);
		}
		for (int i = 0;i < nPlayers;i++) {
			for (int j = 0;j < nPlayers;j++) {
				System.out.print(freqPerPlayer[i][j] + "\t");
			}
			System.out.println("");
		}
	}*/
}
