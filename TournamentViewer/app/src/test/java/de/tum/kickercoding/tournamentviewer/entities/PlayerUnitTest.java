package de.tum.kickercoding.tournamentviewer.entities;

import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerUnitTest {

	@Test
	public void testBasicToAndFromJson() {
		Player p = new Player("name3", 2, 6, 8, 0, 0, 0, 0, 0, 0, 0, 12.3456);
		String pAsJson = p.toJson();
		Player pFromJson = Player.fromJson(pAsJson);
		assertEquals(p.getName(), pFromJson.getName());
		assertEquals(p.getWonGames(), pFromJson.getWonGames());
		assertEquals(p.getLostGames(), pFromJson.getLostGames());
		assertEquals(p.getTiedGames(), pFromJson.getTiedGames());
		assertEquals(p.getWonGamesInTournament(), pFromJson.getWonGamesInTournament());
		assertEquals(p.getLostGamesInTournament(), pFromJson.getLostGamesInTournament());
		assertEquals(p.getTiedGamesInTournament(), pFromJson.getTiedGamesInTournament());
		assertEquals(p.getMmr(), pFromJson.getMmr(), 0.001);
	}
}
