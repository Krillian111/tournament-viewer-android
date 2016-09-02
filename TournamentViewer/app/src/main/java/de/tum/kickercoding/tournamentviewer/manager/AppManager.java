package de.tum.kickercoding.tournamentviewer.manager;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.exceptions.PlayerManagerException;
import de.tum.kickercoding.tournamentviewer.exceptions.PreferenceFileException;

// TODO: add comments to methods/class
public class AppManager {

    private static AppManager INSTANCE = null;

    private PlayerManager playerManager;

    public static AppManager getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        return new AppManager();
    }

    private AppManager() {
    }

    /**
     * Initializes all Managers in the correct order to resolve the dependencies.
     * Called once when app is started
     * @param context
     * @throws AppManagerException
     */
    public void initialize(Context context) throws AppManagerException{
        PreferenceFileManager.getInstance().initialize(context);
        try {
            PlayerManager playerManager = PlayerManager.getInstance();
            playerManager.initialize();
        } catch (PreferenceFileException e) {
            throw new AppManagerException("Initialization failed");
        }
    }

    /**
     * add new Player; changes are only written to DB
     * @param name
     * @throws AppManagerException
     */
    public void addNewPlayer(String name) throws AppManagerException {
        try{
            playerManager.addPlayer(name);
        } catch(PlayerManagerException e){
            throw new AppManagerException("Player was not saved, wrapped Exception:" + e.toString());
        }
    }

    public void removePlayer(String name) throws AppManagerException {
        try{
            playerManager.removePlayer(name);
        } catch(PlayerManagerException e){
            throw new AppManagerException("Player was not deleted, wrapped Exception:" + e.toString());
        }
    }

    public List<Player> getAllPlayers(){
        // TODO; make a copy to avoid passing the reference to the list
        return new ArrayList<Player>();
    }

    public void commitChanges() throws AppManagerException {
        playerManager.commitPlayerList();
    }

}
