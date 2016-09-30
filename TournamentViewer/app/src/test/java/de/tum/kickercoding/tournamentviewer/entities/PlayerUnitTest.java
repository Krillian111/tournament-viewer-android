package de.tum.kickercoding.tournamentviewer.entities;

import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerUnitTest {

	@Test
	public void testBasicPlayerToJson() {
		Player p = new Player("name");
		String playerAsJson = p.toJson();
		assertEquals("{\"name\":\"name\",\"playedGames\":0,\"wonGames\":0,\"lostGames\":0," +
				"\"tiedGames\":0,\"rankingScore\":0.0}", playerAsJson);
	}

	@Test
	public void testAllFieldsToJson() {
		Player p1 = new Player("name1", 18, 4, 6, 8, 10);
		String p1AsJson = p1.toJson();
		assertEquals("{\"name\":\"name1\",\"playedGames\":18,\"wonGames\":4,\"lostGames\":6,\"tiedGames\":8," +
				"\"rankingScore\":10.0}", p1AsJson);

		Player p2 = new Player("name2", 7, 3, 2, 2, 0);
		String p2AsJson = p2.toJson();
		assertEquals("{\"name\":\"name2\",\"playedGames\":7,\"wonGames\":3,\"lostGames\":2,\"tiedGames\":2," +
				"\"rankingScore\":0.0}", p2AsJson);

		Player p3 = new Player("name3", 16, 2, 6, 8, 12.3456);
		String p3AsJson = p3.toJson();
		assertEquals("{\"name\":\"name3\",\"playedGames\":16,\"wonGames\":2,\"lostGames\":6,\"tiedGames\":8," +
				"\"rankingScore\":12.3456}", p3AsJson);
	}

	@Test
	public void testBasicFromJson() {
		String p1String = "{\"name\":\"name4\",\"playedGames\":20,\"wonGames\":3,\"lostGames\":7,\"tiedGames\":10," +
				"\"rankingScore\":14.3212}";
		Player p1 = Player.fromJson(p1String);
		assertEquals("name4", p1.getName());
		assertEquals(20, p1.getPlayedGames());
		assertEquals(3, p1.getWonGames());
		assertEquals(7, p1.getLostGames());
		assertEquals(10, p1.getTiedGames());
		assertEquals(14.3212, p1.getRankingScore(), 0.001);

		String p2String = "{\"name\":\"name5\",\"playedGames\":150,\"wonGames\":100,\"lostGames\":50," +
				"\"tiedGames\":0," +
				"\"rankingScore\":99.9999}";
		Player p2 = Player.fromJson(p2String);
		assertEquals("name5", p2.getName());
		assertEquals(150, p2.getPlayedGames());
		assertEquals(100, p2.getWonGames());
		assertEquals(50, p2.getLostGames());
		assertEquals(0, p2.getTiedGames());
		assertEquals(99.9999, p2.getRankingScore(), 0.001);
	}
}
