package de.tum.kickercoding.tournamentviewer.entities;

import org.junit.Test;

import java.util.Arrays;

import de.tum.kickercoding.tournamentviewer.util.TournamentMode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TournamentUnitTest {

	@Test
	public void testToJson() {
		Tournament t = new Tournament();
		t.setMaxScore(7);
		t.setNumberOfGames(2);
		t.setOneOnOne(false);
		t.setMode(TournamentMode.MONSTERDYP);
		Player p1 = new Player("p1");
		Player p2 = new Player("p2", 6, 1, 2, 3, 4.0);
		Player p3 = new Player("p3");
		Player p4 = new Player("p4");
		t.addPlayer(p1);
		t.addPlayer(p2);
		t.addPlayer(p3);
		t.addPlayer(p4);
		t.addGame(new Game(Arrays.asList(p1, p2, p3, p4)));
		String tournamentAsJson = t.toJson();
		assertEquals("{\"players\":[" +
						"{\"name\":\"p1\",\"playedGames\":0,\"wonGames\":0,\"lostGames\":0," +
						"\"tiedGames\":0,\"rankingScore\":0.0}," +
						"{\"name\":\"p2\",\"playedGames\":6,\"wonGames\":1," +
						"\"lostGames\":2,\"tiedGames\":3,\"rankingScore\":4.0}," +
						"{\"name\":\"p3\",\"playedGames\":0," +
						"\"wonGames\":0,\"lostGames\":0,\"tiedGames\":0,\"rankingScore\":0.0}," +
						"{\"name\":\"p4\"," +
						"\"playedGames\":0,\"wonGames\":0,\"lostGames\":0,\"tiedGames\":0,\"rankingScore\":0.0}" +
						"]," +
						"\"games\":[" +
						"{\"participants\":[" +
						"{\"name\":\"p1\",\"playedGames\":0,\"wonGames\":0,\"lostGames\":0,\"tiedGames\":0," +
						"\"rankingScore\":0.0}," +
						"{\"name\":\"p2\",\"playedGames\":6,\"wonGames\":1,\"lostGames\":2,\"tiedGames\":3," +
						"\"rankingScore\":4.0}," +
						"{\"name\":\"p3\",\"playedGames\":0,\"wonGames\":0,\"lostGames\":0,\"tiedGames\":0," +
						"\"rankingScore\":0.0}," +
						"{\"name\":\"p4\",\"playedGames\":0,\"wonGames\":0,\"lostGames\":0,\"tiedGames\":0," +
						"\"rankingScore\":0.0}" +
						"]," +
						"\"scoreTeam1\":0,\"scoreTeam2\":0,\"finished\":false,\"oneOnOne\":false," +
						"\"resultCommitted\":false}" +
						"]," +
						"\"maxScore\":7,\"numberOfGames\":2,\"oneOnOne\":false,\"finished\":false," +
						"\"mode\":\"MONSTERDYP\"}"
				, tournamentAsJson);
	}

	@Test
	public void testFromJson() {
		String tournamentAsJson = "{\"players\":[" +
				"{\"name\":\"p1\",\"playedGames\":0,\"wonGames\":0,\"lostGames\":0," +
				"\"tiedGames\":0,\"rankingScore\":0.0}," +
				"{\"name\":\"p2\",\"playedGames\":6,\"wonGames\":1," +
				"\"lostGames\":2,\"tiedGames\":3,\"rankingScore\":4.0}," +
				"{\"name\":\"p3\",\"playedGames\":0," +
				"\"wonGames\":0,\"lostGames\":0,\"tiedGames\":0,\"rankingScore\":0.0}," +
				"{\"name\":\"p4\"," +
				"\"playedGames\":0,\"wonGames\":0,\"lostGames\":0,\"tiedGames\":0,\"rankingScore\":0.0}" +
				"]," +
				"\"games\":[" +
				"{\"participants\":[" +
				"{\"name\":\"p1\",\"playedGames\":0,\"wonGames\":0,\"lostGames\":0,\"tiedGames\":0," +
				"\"rankingScore\":0.0}," +
				"{\"name\":\"p2\",\"playedGames\":6,\"wonGames\":1,\"lostGames\":2,\"tiedGames\":3," +
				"\"rankingScore\":4.0}," +
				"{\"name\":\"p3\",\"playedGames\":0,\"wonGames\":0,\"lostGames\":0,\"tiedGames\":0," +
				"\"rankingScore\":0.0}," +
				"{\"name\":\"p4\",\"playedGames\":0,\"wonGames\":0,\"lostGames\":0,\"tiedGames\":0," +
				"\"rankingScore\":0.0}" +
				"]," +
				"\"scoreTeam1\":0,\"scoreTeam2\":0,\"finished\":false,\"oneOnOne\":false," +
				"\"resultCommitted\":false}" +
				"]," +
				"\"maxScore\":7,\"numberOfGames\":2,\"oneOnOne\":false,\"finished\":false,\"mode\":\"MONSTERDYP\"}";
		Tournament t = Tournament.fromJson(tournamentAsJson);
		assertEquals(7, t.getMaxScore());
		assertEquals(2, t.getNumberOfGames());
		assertFalse(t.isOneOnOne());
		assertEquals(4, t.getPlayers().size());
		assertEquals(1, t.getGames().size());
		assertEquals(false, t.isFinished());
		assertEquals(TournamentMode.MONSTERDYP, t.getMode());
		assertTrue(new Player("p1").equals(t.getGame(0).getTeam1().get(0)));
		assertTrue(new Player("p2").equals(t.getGame(0).getTeam1().get(1)));
		assertTrue(new Player("p3").equals(t.getGame(0).getTeam2().get(0)));
		assertTrue(new Player("p4").equals(t.getGame(0).getTeam2().get(1)));
		assertTrue(new Player("p1").equals(t.getPlayers().get(0)));
		assertTrue(new Player("p2").equals(t.getPlayers().get(1)));
		assertTrue(new Player("p3").equals(t.getPlayers().get(2)));
		assertTrue(new Player("p4").equals(t.getPlayers().get(3)));
		// check attributes of p2 from players (has non zero values)
		Player p2FromPlayers = t.getPlayers().get(1);
		assertEquals(6, p2FromPlayers.getPlayedGames());
		assertEquals(1, p2FromPlayers.getWonGames());
		assertEquals(2, p2FromPlayers.getLostGames());
		assertEquals(3, p2FromPlayers.getTiedGames());
		assertEquals(4.0, p2FromPlayers.getRankingScore(), 0.001);
		// check attributes of p2 from game (has non zero values); should be the same
		Player p2FromGame = t.getGame(0).getTeam1().get(1);
		assertEquals(6, p2FromGame.getPlayedGames());
		assertEquals(1, p2FromGame.getWonGames());
		assertEquals(2, p2FromGame.getLostGames());
		assertEquals(3, p2FromGame.getTiedGames());
		assertEquals(4.0, p2FromGame.getRankingScore(), 0.001);
	}

	@Test
	public void testToAndFromJson() {
		Tournament t = new Tournament();
		t.setMaxScore(7);
		t.setNumberOfGames(2);
		t.setOneOnOne(false);
		t.setFinished(true);
		t.setMode(TournamentMode.MONSTERDYP);
		Player p1 = new Player("p1");
		Player p2 = new Player("p2", 6, 1, 2, 3, 4.0);
		Player p3 = new Player("p3");
		Player p4 = new Player("p4");
		t.addPlayer(p1);
		t.addPlayer(p2);
		t.addPlayer(p3);
		t.addPlayer(p4);
		t.addGame(new Game(Arrays.asList(p1, p2, p3, p4)));
		String tournamentAsJson = t.toJson();
		Tournament tFromJson = Tournament.fromJson(tournamentAsJson);
		assertEquals(tFromJson.getMaxScore(), t.getMaxScore());
		assertEquals(tFromJson.getNumberOfGames(), t.getNumberOfGames());
		assertEquals(tFromJson.isOneOnOne(), t.isOneOnOne());
		assertEquals(tFromJson.isFinished(), t.isFinished());
		assertEquals(tFromJson.getPlayers().size(), t.getPlayers().size());
		assertEquals(tFromJson.getGames().size(), t.getGames().size());
		// check attributes of p2 from players (has non zero values); only in players as more specific checks are
		// already done in testFromJson()
		Player p2Original = t.getPlayers().get(1);
		Player p2FromJson = tFromJson.getPlayers().get(1);
		assertEquals(p2FromJson.getPlayedGames(), p2Original.getPlayedGames());
		assertEquals(p2FromJson.getWonGames(), p2Original.getWonGames());
		assertEquals(p2FromJson.getLostGames(), p2Original.getLostGames());
		assertEquals(p2FromJson.getTiedGames(), p2Original.getTiedGames());
		assertEquals(p2FromJson.getRankingScore(), p2Original.getRankingScore(), 0.001);
	}
}
