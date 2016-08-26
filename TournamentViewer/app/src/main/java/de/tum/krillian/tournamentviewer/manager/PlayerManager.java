package de.tum.krillian.tournamentviewer.manager;

import java.util.ArrayList;
import java.util.List;

import de.tum.krillian.tournamentviewer.entities.Game;
import de.tum.krillian.tournamentviewer.entities.Player;
import de.tum.krillian.tournamentviewer.exceptions.PlayerListException;

public class PlayerManager {

    private static List<Player> players = new ArrayList<Player>();

    private PlayerManager(){}

    public static void addPlayer(String name) throws PlayerListException {
        Player newPlayer = new Player(name);

        if(players.contains(newPlayer)){
            throw new PlayerListException(String.format("Player %s already exists", name));
        }
        // TODO: check for | in name
        players.add(newPlayer);
    }

    public static void removePlayer(String name) throws PlayerListException {
        for(Player p : players) {
            if(p.getName().equals(name)) {
                players.remove(p);
                return;
            }
        }
        throw new PlayerListException("Failed to remove player");
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
