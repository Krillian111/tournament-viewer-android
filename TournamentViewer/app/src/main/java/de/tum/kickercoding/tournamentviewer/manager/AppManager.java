package de.tum.kickercoding.tournamentviewer.manager;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.exceptions.PlayerManagerException;
import de.tum.kickercoding.tournamentviewer.exceptions.PreferenceFileException;

/**
 * AppManager solely provides methods callable by Activities.
 * All interactions with the backend happen using this class.
 */
public class AppManager {

    private static AppManager instance = new AppManager();

    private PlayerManager playerManager;

    public static AppManager getInstance() {
        return instance;
    }

    private AppManager() {
    }

    /**
     * Initializes all Managers in the correct order to resolve the dependencies.
     * Called once when app is started
     * @param context: context to retrieve sharedPreferences
     * @throws AppManagerException
     */
    public void initialize(Context context) throws AppManagerException{
        PreferenceFileManager.getInstance().initialize(context);
        try {
            playerManager = PlayerManager.getInstance();
            playerManager.initialize();
        } catch (PreferenceFileException e) {
            throw new AppManagerException("Initialization failed");
        }
    }

    /**
     * add new Player;
     * to permanently save this action call {@link #commitChanges()}
     * @param name: name of the player
     * @throws AppManagerException
     */
    public void addNewPlayer(String name) throws AppManagerException {
        try{
            playerManager.addPlayer(name);
        } catch(PlayerManagerException e){
            throw new AppManagerException("Player was not saved, wrapped Exception:" + e.toString());
        }
    }


    /**
     * delete player with specified name;
     * to permanently save this action call {@link #commitChanges()}
     * @param name
     * @throws AppManagerException
     */
    public void removePlayer(String name) throws AppManagerException {
        try{
            playerManager.removePlayer(name);
        } catch(PlayerManagerException e){
            throw new AppManagerException("Player was not deleted, wrapped Exception:" + e.toString());
        }
    }


    /**
     * retrieve the list of all players available
     * @return copied list of all players (no reference to the internal list of {@link PlayerManager}
     */
    public List<Player> getAllPlayers(){
        List<Player> players = playerManager.getPlayers();
        // clone the list and all player objects to avoid changing the original list
        List<Player> playersCopied = new ArrayList<>();
        for (Player p: players) {
            Player copiedPlayer = Player.fromString(p.toString());
            playersCopied.add(copiedPlayer);
        }
        return playersCopied;
    }

    /**
     * commit all changes to the list of players to the SharedPreferences
     * @throws AppManagerException
     */
    public void commitChanges() throws AppManagerException {
        playerManager.commitPlayerList();
    }

}
