package de.tum.kickercoding.tournamentviewer.modes;

import java.util.List;

import de.tum.kickercoding.tournamentviewer.entities.Game;
import de.tum.kickercoding.tournamentviewer.entities.Player;

// TODO: add comments to methods/class
public interface Matchmaking {

    public List<Game> generateGames (List<Player> players);

}
