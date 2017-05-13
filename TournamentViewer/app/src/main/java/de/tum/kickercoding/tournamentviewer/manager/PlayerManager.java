package de.tum.kickercoding.tournamentviewer.manager;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.exceptions.PlayerManagerException;
import de.tum.kickercoding.tournamentviewer.exceptions.PreferenceFileManagerException;

class PlayerManager {

	private static final String LOG_TAG = PlayerManager.class.toString();
	private static PlayerManager instance = new PlayerManager();
	private List<Player> players;
	private boolean isInitialized = false;

	private PlayerManager() {
	}

	/**
	 * Get the instance of the singleton.
	 *
	 * @return The instance of the singleton.
	 */
	static PlayerManager getInstance() {
		if (!instance.isInitialized) {
			instance.initialize();
		}
		return instance;
	}

	/**
	 * Initialization (should be called after instantiation)
	 *
	 * @throws PreferenceFileManagerException
	 */
	void initialize() {
		loadPlayerList();
		isInitialized = true;
		savePlayerList();
	}

	/**
	 * commits the current player list with all its changes to the preference file
	 */
	void savePlayerList() {
		for (Player p : players) {
			try {
				PreferenceFileManager.getInstance().savePlayer(p);
			} catch (PreferenceFileManagerException e) {
				Log.e(LOG_TAG, "couldn't save players; unstable state; " + e.getMessage());
			}
		}
	}

	void loadPlayerList() {
		try {
			players = PreferenceFileManager.getInstance().getPlayerList();
		} catch (PreferenceFileManagerException e) {
			Log.e(LOG_TAG, "couldn't load players; unstable state; " + e.getMessage());
			players = new ArrayList<>();
		}
	}

	/**
	 * Add a {@link Player} to player list.
	 *
	 * @param name The name of the {@link Player} to add.
	 * @throws PlayerManagerException If there are symbols that are not allowed in the name of the {@link Player}.
	 */
	void addPlayer(String name) throws PlayerManagerException {
		loadPlayerList();
		Player newPlayer = new Player(name);
		if (players.contains(newPlayer)) {
			throw new PlayerManagerException(String.format("Player %s already exists", name));
		}
		players.add(newPlayer);
		Collections.sort(players);
		savePlayerList();
	}


	void updatePlayer(Player playerToUpdate) throws PlayerManagerException {
		loadPlayerList();
		for (int i = 0;i < players.size();i++) {
			if (players.get(i).equals(playerToUpdate)) {
				players.set(i, playerToUpdate);
				savePlayerList();
				return;
			}
		}
		throw new PlayerManagerException(String.format("Player update failed: player %s does not exist in global " +
				"player list", playerToUpdate.getName()));
	}

	/**
	 * Remove {@link Player} from player list.
	 *
	 * @param name The name of the {@link Player} to remove.
	 * @throws PlayerManagerException
	 */
	boolean removePlayer(String name) {
		// players with same name are considered equal, see player.equals()
		Player dummyPlayer = new Player(name);
		boolean playerRemoved = players.remove(dummyPlayer);
		try {
			PreferenceFileManager.getInstance().removePlayer(name);
		} catch (PreferenceFileManagerException e) {
			Log.e(LOG_TAG, String.format("Player removal of %s failed", name));
		}
		return playerRemoved;
	}

	/**
	 * Get the number of currently registered players.
	 *
	 * @return Number of currently registered players.
	 */
	int getNumberOfPlayers() {
		loadPlayerList();
		return players.size();
	}


	/**
	 * Get the currently registered players.
	 *
	 * @return The currently registered players.
	 */
	List<Player> getPlayers() {
		loadPlayerList();
		return players;
	}

	Player getPlayer(int position) throws PlayerManagerException {
		loadPlayerList();
		try {
			return players.get(position);
		} catch (IndexOutOfBoundsException e) {
			throw new PlayerManagerException(String.format("IndexOutOfBounds: No player at position %d", position));
		}
	}

	void manuallyAdjustElo(String name, double elo) {
		loadPlayerList();
		Player playerToFind = new Player(name);
		for (Player playerToUpdate : players) {
			if (playerToFind.equals(playerToUpdate)) {
				playerToUpdate.setElo(elo);
				playerToUpdate.setEloChangeFromLastGame(0);
			}
		}
		savePlayerList();
	}
}
