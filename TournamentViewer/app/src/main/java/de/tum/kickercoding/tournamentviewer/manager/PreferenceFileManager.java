package de.tum.kickercoding.tournamentviewer.manager;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import de.tum.kickercoding.tournamentviewer.Constants;
import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.exceptions.PreferenceFileException;


/**
 * Handles all communication with SharedPreferences
 * Has multiple methods for adding, removing, updating a player
 */
class PreferenceFileManager {

    private static PreferenceFileManager INSTANCE = null;

    // context necessary for retrieving sharedPreferences
    private Context context;

    static PreferenceFileManager getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        return new PreferenceFileManager();
    }

    private PreferenceFileManager() {
    }

    /**
     * called by {@link AppManager} to initialize the context
     * @param context
     */
    void initialize(Context context) {
        this.context = context;
    }

    private boolean isInitialized() {
        return (context != null);
    }

    /**
     * add a player to the preference lsit
     * @param player
     * @throws PreferenceFileException
     */
    void addNewPlayer(Player player) throws PreferenceFileException {
        if(isInitialized()) {
            try {
                SharedPreferences playersListPref = context.getSharedPreferences(Constants.FILE_GLOBAL_PLAYERS_LIST, 0);
                playersListPref.edit().putString(player.getName(), player.toString()).apply();
            } catch (NullPointerException e){
                throw new PreferenceFileException("PreferenceEditor not available");
            }
        } else {
            throw new PreferenceFileException("PreferenceFileManager not initialized");
        }
    }

    /**
     * remove a player from the preference list
     * @param name
     * @throws PreferenceFileException
     */
    void removePlayer(String name) throws PreferenceFileException {
        if (isInitialized()){
            try {
                SharedPreferences playersListPref = context.getSharedPreferences(Constants.FILE_GLOBAL_PLAYERS_LIST, 0);
                playersListPref.edit().remove(name).apply();
            } catch (NullPointerException e) {
                throw new PreferenceFileException("PreferenceEditor not available");
            }
        } else {
            throw new PreferenceFileException("PreferenceFileManager not initialized");
        }
    }


    /**
     * update the key 'updatestring.getName()' in preference list with the string representation of updatePlayer
     * @param updatePlayer
     * @throws PreferenceFileException
     */
    void updatePlayer(Player updatePlayer) throws PreferenceFileException {
        SharedPreferences playersListPref = context.getSharedPreferences(Constants.FILE_GLOBAL_PLAYERS_LIST, 0);

        playersListPref.edit().putString(updatePlayer.getName(),updatePlayer.toString()).apply();
    }

    List<Player> getPlayerList() throws PreferenceFileException {
        ArrayList<Player> playerList = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.FILE_GLOBAL_PLAYERS_LIST, 0);
        for (Object playerAsObject: sharedPreferences.getAll().values()){
            Player player = Player.fromString((String) playerAsObject);
            playerList.add(player);
        }
        return playerList;
    }
}