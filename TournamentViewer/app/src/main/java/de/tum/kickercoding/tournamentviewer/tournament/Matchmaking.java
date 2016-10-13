package de.tum.kickercoding.tournamentviewer.tournament;

import java.util.List;

import de.tum.kickercoding.tournamentviewer.entities.Game;
import de.tum.kickercoding.tournamentviewer.entities.Player;

// TODO: add comments to methods/class
public interface Matchmaking {

	public Game generateGame(List<Player> players, boolean oneOnOne, List<Game> pastGames);

	public List<Game> generateRound(List<Player> players, boolean oneOnOne, List<Game> pastGames);
}
