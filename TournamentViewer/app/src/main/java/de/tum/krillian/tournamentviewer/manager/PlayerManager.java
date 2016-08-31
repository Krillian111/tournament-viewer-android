package de.tum.krillian.tournamentviewer.manager;

import java.util.ArrayList;
import java.util.List;

import de.tum.krillian.tournamentviewer.entities.Game;
import de.tum.krillian.tournamentviewer.entities.Player;
import de.tum.krillian.tournamentviewer.exceptions.PlayerManagerException;

public class PlayerManager {

    private static List<Player> players = new ArrayList<Player>();

    private PlayerManager(){}

    /**
     * Add player to player list.
     * @param name The name of the player to add.
     * @throws PlayerManagerException
     */
    public static void addPlayer(String name) throws PlayerManagerException {
        if (name.contains("|")) {
            throw new PlayerManagerException("Invalid character: | (pipe symbol)");
        }

        Player newPlayer = new Player(name);
        if(players.contains(newPlayer)){
            throw new PlayerManagerException(String.format("Player %s already exists", name));
        }
        // TODO: check for | in name
        players.add(newPlayer);
    }

    public static void removePlayer(String name) throws PlayerManagerException {
        for(Player p : players) {
            if(p.getName().equals(name)) {
                players.remove(p);
                return;
            }
        }
        throw new PlayerManagerException("Failed to remove player");
    }

    public static int getNumberOfPlayers() {
        return players.size();
    }

    public static void commitGameResult(Game game){
        // TODO
    }

    // Used for testing only
    public static void clearPlayerList() {
        players.clear();
    }
}
