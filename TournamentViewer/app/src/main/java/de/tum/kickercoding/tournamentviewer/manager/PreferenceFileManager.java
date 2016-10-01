package de.tum.kickercoding.tournamentviewer.manager;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.entities.Tournament;
import de.tum.kickercoding.tournamentviewer.exceptions.PreferenceFileManagerException;
import de.tum.kickercoding.tournamentviewer.util.Constants;


/**
 * Handles all communication with permanent storage (SharedPreferences)
 * Has multiple methods for adding, removing, updating a player
 */
// TODO: write logging for succesful commits
class PreferenceFileManager {

	private static PreferenceFileManager instance = new PreferenceFileManager();

	private final String NOT_INIT = "FATAL ERROR: PreferenceFileManager not initialized";

	// applicationContext necessary for retrieving sharedPreferences
	private Context applicationContext;

	static PreferenceFileManager getInstance() {
		return instance;
	}

	private PreferenceFileManager() {
	}

	/**
	 * called by {@link AppManager} to initialize the context
	 *
	 * @param applicationContext
	 */
	void initialize(Context applicationContext) {
		this.applicationContext = applicationContext;
	}

	private boolean isInitialized() {
		return (applicationContext != null);
	}

	void savePlayer(Player player) throws PreferenceFileManagerException {
		if (isInitialized()) {
			SharedPreferences pref = applicationContext.getSharedPreferences(Constants.FILE_GLOBAL_PLAYERS_LIST, 0);
			pref.edit().putString(player.getName(), player.toJson()).apply();
		} else {
			throw new PreferenceFileManagerException(NOT_INIT);
		}
	}

	/**
	 * remove a player from the preference list
	 *
	 * @param name
	 * @throws PreferenceFileManagerException
	 */
	void removePlayer(String name) throws PreferenceFileManagerException {
		if (isInitialized()) {
			SharedPreferences playersListPref = applicationContext.getSharedPreferences(Constants
					.FILE_GLOBAL_PLAYERS_LIST, 0);
			playersListPref.edit().remove(name).apply();
		} else {
			throw new PreferenceFileManagerException(NOT_INIT);
		}
	}

	List<Player> getPlayerList() throws PreferenceFileManagerException {
		if (isInitialized()) {
			ArrayList<Player> playerList = new ArrayList<>();
			SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(Constants
					.FILE_GLOBAL_PLAYERS_LIST, 0);
			for (Object playerAsObject : sharedPreferences.getAll().values()) {
				Player player = Player.fromJson((String) playerAsObject);
				playerList.add(player);
			}
			return playerList;
		} else {
			throw new PreferenceFileManagerException(NOT_INIT);
		}
	}

	public int loadMaxScore() throws PreferenceFileManagerException {
		if (isInitialized()) {
			SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(Constants
					.FILE_GENERAL_SETTINGS, 0);
			return sharedPreferences.getInt(Constants.VAR_MAX_SCORE, Constants.DEFAULT_MAX_SCORE);
		} else {
			throw new PreferenceFileManagerException(NOT_INIT);
		}
	}

	public void saveMaxScore(int maxScore) throws PreferenceFileManagerException {
		if (isInitialized()) {
			SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(Constants
					.FILE_GENERAL_SETTINGS, 0);
			sharedPreferences.edit().putInt(Constants.VAR_MAX_SCORE, maxScore).apply();
		} else {
			throw new PreferenceFileManagerException(NOT_INIT);
		}
	}

	public int loadNumberOfGames() throws PreferenceFileManagerException {
		if (isInitialized()) {
			SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(Constants
					.FILE_GENERAL_SETTINGS, 0);
			return sharedPreferences.getInt(Constants.VAR_NUMBER_OF_GAMES, Constants.DEFAULT_NUMBER_OF_GAMES);
		} else {
			throw new PreferenceFileManagerException(NOT_INIT);
		}
	}

	public void saveNumberOfGames(int numberOfGames) throws PreferenceFileManagerException {
		if (isInitialized()) {
			SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(Constants
					.FILE_GENERAL_SETTINGS, 0);
			sharedPreferences.edit().putInt(Constants.VAR_NUMBER_OF_GAMES, numberOfGames).apply();
		} else {
			throw new PreferenceFileManagerException(NOT_INIT);
		}
	}

	void saveTournament(Tournament tournament) throws PreferenceFileManagerException {
		if (isInitialized()) {
			SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(Constants
					.FILE_TOURNAMENT_DATA, 0);
			sharedPreferences.edit().putString(Constants.VAR_CURRENT_TOURNAMENT, tournament.toJson()).apply();
		} else {
			throw new PreferenceFileManagerException(NOT_INIT);
		}
	}

	Tournament loadTournament() throws PreferenceFileManagerException {
		if (isInitialized()) {
			SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(Constants
					.FILE_TOURNAMENT_DATA, 0);
			String tournamentAsJson = sharedPreferences.getString(Constants.VAR_CURRENT_TOURNAMENT, null);
			if (tournamentAsJson != null) {
				return Tournament.fromJson(tournamentAsJson);
			} else {
				return null;
			}
		} else {
			throw new PreferenceFileManagerException(NOT_INIT);
		}
	}
}