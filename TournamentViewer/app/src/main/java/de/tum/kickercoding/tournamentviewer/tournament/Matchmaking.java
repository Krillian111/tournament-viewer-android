package de.tum.kickercoding.tournamentviewer.tournament;

import java.util.List;

import de.tum.kickercoding.tournamentviewer.entities.Game;
import de.tum.kickercoding.tournamentviewer.entities.Player;

// TODO: add comments to methods/class
public interface Matchmaking {

    public Game generateGame2on2 (List<Player> players);

    public List<Game> generateRound2on2 (List<Player> players);

	public Game generateGame1on1 (List<Player> players);

	public List<Game> generateRound1on1 (List<Player> players);
}
