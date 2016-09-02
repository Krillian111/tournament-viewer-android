package de.tum.kickercoding.tournamentviewer.manager;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import de.tum.kickercoding.tournamentviewer.entities.Game;
import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.exceptions.PlayerManagerException;
import de.tum.kickercoding.tournamentviewer.exceptions.PreferenceFileException;


// TODO: add comments to methods/class
class PlayerManager {

    private static PlayerManager instance = null;
    private PreferenceFileManager preferenceFileManager = null;
    //private Context context = null;
    private List<Player> players = new ArrayList<Player>();

    private PlayerManager(){
        preferenceFileManager = PreferenceFileManager.getInstance();
        players = preferenceFileManager.getPlayerList();
    }

    /**
     * Get the instance of the singleton.
     * @return The instance of the singleton.
     */
    static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }
        return instance;
    }

    /**
     * Add player to player list.
     * @param name The name of the player to add.
     * @throws PlayerManagerException
     */
    void addPlayer(String name) throws PlayerManagerException {
        if (name.contains("|")) {
            throw new PlayerManagerException("Invalid character: | (pipe symbol)");
        }

        Player newPlayer = new Player(name);
        if(players.contains(newPlayer)){
            throw new PlayerManagerException(String.format("Player %s already exists", name));
        }
        players.add(newPlayer);
    }

    /**
     * Remove player from player list.
     * @param name The name of the player to remove.
     * @throws PlayerManagerException
     */
    void removePlayer(String name) throws PlayerManagerException {
        for(Player p : players) {
            if(p.getName().equals(name)) {
                players.remove(p);
                return;
            }
        }
        throw new PlayerManagerException("Failed to remove player");
    }

    /**
     * Get the number of currently registered players.
     * @return Number of players.
     */
    int getNumberOfPlayers() {
        return players.size();
    }

    /**
     * Commit results of a game to the player list
     * @param game
     */
    void commitGameResult(Game game){
        // TODO: implement
    }

    // Used for testing only
    public void clearPlayerList() {
        players.clear();
    }
}
