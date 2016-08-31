package de.tum.kickercoding.tournamentviewer.manager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.tum.kickercoding.tournamentviewer.Constants;
import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.exceptions.PreferenceFileException;


// TODO: add comments to methods/class
class PreferenceFileManager {

    private static PreferenceFileManager INSTANCE = null;

    static PreferenceFileManager getInstance(){
        if (INSTANCE != null) {
            return INSTANCE;
        }
        return new PreferenceFileManager();
    }

    private PreferenceFileManager(){
        // TODO: implement initialization
    }

    void initialize(){

    }

//    public static Set<String> loadPlayerNamesFromGeneralSettings(Activity activity){
//        SharedPreferences generalSettingsPref = activity.getSharedPreferences(Constants.FILE_GENERAL_SETTINGS, 0);
//        return generalSettingsPref.getStringSet(Constants.VAR_GLOBAL_PLAYER_NAME_SET, new HashSet<String>());
//    }

//    public static List<Player> getPlayersByNames(Set<String> playerNames, Activity activity) throws IllegalArgumentException{
//        SharedPreferences playersFile = activity.getSharedPreferences(Constants.FILE_GLOBAL_PLAYERS_LIST, 0);
//
//        List<Player> playerList = new ArrayList<>();
//
//        for (String playerName : playerNames) {
//            String playerAsString = playersFile.getString(playerName, null);
//            if (playerAsString == null) {
//                throw new IllegalArgumentException("Player with name " + playerAsString + " does not exist in preference file");
//            }
//            Player player = Player.fromString(playerAsString);
//            playerList.add(player);
//        }
//
//        return playerList;
//    }

    static Player addNewPlayer(String playerName, Activity activity){
        Player newPlayer = new Player(playerName);
        System.err.println(newPlayer);
        // add player to player list
        SharedPreferences playersListPref = activity.getSharedPreferences(Constants.FILE_GLOBAL_PLAYERS_LIST, 0);
        SharedPreferences.Editor editorPlayersFile = playersListPref.edit();
        editorPlayersFile.putString(newPlayer.getName(), newPlayer.toString());
        editorPlayersFile.apply();
        // TODO: update method (Bow)
        return newPlayer;
    }

    void removePlayer(String name) throws PreferenceFileException{
        // TODO: implement
    }

    void updatePlayer(Player updatePlayer) throws PreferenceFileException{
        // TODO: implement
    }

    static List<Player> getPlayerList(Context context) throws PreferenceFileException{
        // TODO: implement
        return new ArrayList<>();
    }
}
