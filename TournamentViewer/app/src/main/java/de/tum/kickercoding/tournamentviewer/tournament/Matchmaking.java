package de.tum.kickercoding.tournamentviewer.tournament;

import java.util.List;

import de.tum.kickercoding.tournamentviewer.entities.Game;
import de.tum.kickercoding.tournamentviewer.entities.Player;

public interface Matchmaking {

	/**
	 * Generate a single game, independent from the number of players supplied.
	 * Past games are for decreasing the probability of same teams occurring too frequently
	 * Players with less games are prioritized during player selection.
	 */
	public Game generateGame(List<Player> players, boolean oneOnOne, List<Game> pastGames);

	/**
	 * Generate as many games as possible such that no player plays twice (i.e. for 2v2: roundedDown(#player/4))
	 * independent from the number of players supplied.
	 * Past games are for decreasing the probability of same teams occurring too frequently
	 * Players with less games are prioritized during player selection.
	 */
	public List<Game> generateRound(List<Player> players, boolean oneOnOne, List<Game> pastGames);
}
