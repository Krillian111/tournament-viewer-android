package de.tum.kickercoding.tournamentviewer.manager;

import java.util.ArrayList;
import java.util.List;

import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.exceptions.PlayerManagerException;
import de.tum.kickercoding.tournamentviewer.exceptions.PreferenceFileException;
import de.tum.kickercoding.tournamentviewer.util.Constants;


// TODO: add comments to methods/class
class PlayerManager {

	private static PlayerManager instance = new PlayerManager();
	private PreferenceFileManager preferenceFileManager = null;
	private List<Player> players = new ArrayList<Player>();

	private PlayerManager() {
	}

	/**
	 * Get the instance of the singleton.
	 *
	 * @return The instance of the singleton.
	 */
	static PlayerManager getInstance() {
		return instance;
	}

	/**
	 * Initialization (should be called after instantiation)
	 *
	 * @throws PreferenceFileException
	 */
	void initialize() throws PreferenceFileException {
		preferenceFileManager = PreferenceFileManager.getInstance();
		players = preferenceFileManager.getPlayerList();
	}

	/**
	 * Add a {@link Player} to player list.
	 *
	 * @param name The name of the {@link Player} to add.
	 * @throws PlayerManagerException If there are symbols that are not allowed in the name of the {@link Player}.
	 */
	void addPlayer(String name) throws PlayerManagerException {
		if (name.contains(Constants.DELIMITER)) {
			throw new PlayerManagerException(String.format("Invalid character: %s (underscore)", Constants
					.DELIMITER));
		}

		Player newPlayer = new Player(name);
		if (players.contains(newPlayer)) {
			throw new PlayerManagerException(String.format("Player %s already exists", name));
		}
		players.add(newPlayer);
	}


	void updatePlayer(Player playerToUpdate) throws PlayerManagerException {
		for (int i = 0;i < players.size();i++) {
			if (players.get(i).equals(playerToUpdate)) {
				players.set(i, playerToUpdate);
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
		return players.remove(dummyPlayer);
	}

	/**
	 * Get the number of currently registered players.
	 *
	 * @return Number of currently registered players.
	 */
	int getNumberOfPlayers() {
		return players.size();
	}


	/**
	 * Get the currently registered players.
	 *
	 * @return The currently registered players.
	 */
	List<Player> getPlayers() {
		return players;
	}

	/**
	 * commits the current player list with all its changes to the preference file
	 */
	void commitPlayerList() throws PlayerManagerException {
		for (Player p : players) {
			try {
				preferenceFileManager.savePlayer(p);
			} catch (PreferenceFileException e) {
				throw new PlayerManagerException(e.getMessage());
			}
		}
	}

	Player getPlayerByPosition(int position) throws PlayerManagerException {
		try {
			return players.get(position);
		} catch (IndexOutOfBoundsException e) {
			throw new PlayerManagerException(String.format("No player at position %d", position));
		}
	}
}
