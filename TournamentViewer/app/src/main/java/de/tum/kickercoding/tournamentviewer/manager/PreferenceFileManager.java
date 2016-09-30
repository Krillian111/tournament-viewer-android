package de.tum.kickercoding.tournamentviewer.manager;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.exceptions.PreferenceFileException;
import de.tum.kickercoding.tournamentviewer.util.Constants;


/**
 * Handles all communication with permanent storage (SharedPreferences)
 * Has multiple methods for adding, removing, updating a player
 */
// TODO: write logging for succesful commits
class PreferenceFileManager {

	private static PreferenceFileManager instance = new PreferenceFileManager();

	private final String NOT_INIT = "PreferenceFileManager not initialized";

	// context necessary for retrieving sharedPreferences
	private Context context;

	static PreferenceFileManager getInstance() {
		return instance;
	}

	private PreferenceFileManager() {
	}

	/**
	 * called by {@link AppManager} to initialize the context
	 *
	 * @param context
	 */
	void initialize(Context context) {
		this.context = context;
	}

	private boolean isInitialized() {
		return (context != null);
	}

	void savePlayer(Player player) throws PreferenceFileException {
		if (isInitialized()) {
			SharedPreferences pref = context.getSharedPreferences(Constants.FILE_GLOBAL_PLAYERS_LIST, 0);
			pref.edit().putString(player.getName(), player.toJson()).apply();
		} else throw new PreferenceFileException(NOT_INIT);
	}

	/**
	 * remove a player from the preference list
	 *
	 * @param name
	 * @throws PreferenceFileException
	 */
	void removePlayer(String name) throws PreferenceFileException {
		if (isInitialized()) {
			SharedPreferences playersListPref = context.getSharedPreferences(Constants.FILE_GLOBAL_PLAYERS_LIST, 0);
			playersListPref.edit().remove(name).apply();
		} else {
			throw new PreferenceFileException(NOT_INIT);
		}
	}

	List<Player> getPlayerList() throws PreferenceFileException {
		if (isInitialized()) {
			ArrayList<Player> playerList = new ArrayList<>();
			SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.FILE_GLOBAL_PLAYERS_LIST, 0);
			for (Object playerAsObject : sharedPreferences.getAll().values()) {
				Player player = Player.fromJson((String) playerAsObject);
				playerList.add(player);
			}
			return playerList;
		} else {
			throw new PreferenceFileException(NOT_INIT);
		}
	}

	public int loadMaxScore() throws PreferenceFileException {
		if (isInitialized()) {
			SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.FILE_GENERAL_SETTINGS, 0);
			return sharedPreferences.getInt(Constants.VAR_MAX_SCORE, Constants.DEFAULT_MAX_SCORE);
		} else {
			throw new PreferenceFileException(NOT_INIT);
		}
	}


	public void saveMaxScore(int maxScore) throws PreferenceFileException {
		if (isInitialized()) {
			SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.FILE_GENERAL_SETTINGS, 0);
			sharedPreferences.edit().putInt(Constants.VAR_MAX_SCORE, maxScore).apply();
		} else {
			throw new PreferenceFileException(NOT_INIT);
		}
	}

	public int loadNumberOfGames() throws PreferenceFileException {
		if (isInitialized()) {
			SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.FILE_GENERAL_SETTINGS, 0);
			return sharedPreferences.getInt(Constants.VAR_NUMBER_OF_GAMES, Constants.DEFAULT_NUMBER_OF_GAMES);
		} else {
			throw new PreferenceFileException(NOT_INIT);
		}
	}

	public void saveNumberOfGames(int numberOfGames) throws PreferenceFileException {
		if (isInitialized()) {
			SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.FILE_GENERAL_SETTINGS, 0);
			sharedPreferences.edit().putInt(Constants.VAR_NUMBER_OF_GAMES, numberOfGames).apply();
		} else {
			throw new PreferenceFileException(NOT_INIT);
		}
	}

}