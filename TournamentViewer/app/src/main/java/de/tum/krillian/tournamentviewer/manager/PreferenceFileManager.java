package de.tum.krillian.tournamentviewer.manager;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.tum.krillian.tournamentviewer.Constants;
import de.tum.krillian.tournamentviewer.entities.Player;

public class PreferenceFileManager {

    public static Set<String> loadPlayerNamesFromGeneralSettings(Activity activity){
        SharedPreferences generalSettingsPref = activity.getSharedPreferences(Constants.FILE_GENERAL_SETTINGS, 0);
        return generalSettingsPref.getStringSet(Constants.VAR_GLOBAL_PLAYER_NAME_SET, new HashSet<String>());
    }

    public static List<Player> getPlayersByNames(Set<String> playerNames, Activity activity) throws IllegalArgumentException{
        SharedPreferences playersFile = activity.getSharedPreferences(Constants.FILE_GLOBAL_PLAYERS_LIST, 0);

        List<Player> playerList = new ArrayList<>();

        for (String playerName : playerNames) {
            String playerAsString = playersFile.getString(playerName, null);
            if (playerAsString == null) {
                throw new IllegalArgumentException("Player with name " + playerAsString + " does not exist in preference file");
            }
            Player player = Player.fromString(playerAsString);
            playerList.add(player);
        }

        return playerList;
    }

    public static Player createNewPlayer(String playerName, Activity activity){
        Player newPlayer = new Player(playerName);
        System.err.println(newPlayer);
        // add player to player list
        SharedPreferences playersListPref = activity.getSharedPreferences(Constants.FILE_GLOBAL_PLAYERS_LIST, 0);
        SharedPreferences.Editor editorPlayersFile = playersListPref.edit();
        editorPlayersFile.putString(newPlayer.getName(), newPlayer.toString());
        editorPlayersFile.apply();

        // add player name to set in general setting
        SharedPreferences generalSettingsPref = activity.getSharedPreferences(Constants.FILE_GENERAL_SETTINGS, 0);
        Set<String> playerSet = generalSettingsPref.getStringSet(Constants.VAR_GLOBAL_PLAYER_NAME_SET, new HashSet<String>());
        Set<String> playerSetCopy = playerSet;
        playerSetCopy.add(newPlayer.getName());
        SharedPreferences.Editor editorGeneralSettingsFile = generalSettingsPref.edit();
        editorGeneralSettingsFile.putStringSet(Constants.VAR_GLOBAL_PLAYER_NAME_SET, playerSetCopy);
        editorGeneralSettingsFile.apply();

        return newPlayer;
    }
}
