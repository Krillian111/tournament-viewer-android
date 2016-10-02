package de.tum.kickercoding.tournamentviewer.entities;

import org.junit.Test;

import java.util.Arrays;

import de.tum.kickercoding.tournamentviewer.util.TournamentMode;

import static org.junit.Assert.assertEquals;

public class TournamentUnitTest {

	@Test
	public void testToAndFromJson() {
		Tournament t = new Tournament();
		t.setMaxScore(7);
		t.setNumberOfGames(2);
		t.setOneOnOne(false);
		t.setFinished(true);
		t.setMode(TournamentMode.MONSTERDYP);
		Player p1 = new Player("p1");
		Player p2 = new Player("p2", 1, 2, 3, 0, 0, 0, 4.0);
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
		assertEquals(p2FromJson.getWonGames(), p2Original.getWonGames());
		assertEquals(p2FromJson.getLostGames(), p2Original.getLostGames());
		assertEquals(p2FromJson.getTiedGames(), p2Original.getTiedGames());
		assertEquals(p2FromJson.getWonGamesInTournament(), p2Original.getWonGamesInTournament());
		assertEquals(p2FromJson.getLostGamesInTournament(), p2Original.getLostGamesInTournament());
		assertEquals(p2FromJson.getTiedGamesInTournament(), p2Original.getTiedGamesInTournament());
		assertEquals(p2FromJson.getMmr(), p2Original.getMmr(), 0.001);
	}
}
