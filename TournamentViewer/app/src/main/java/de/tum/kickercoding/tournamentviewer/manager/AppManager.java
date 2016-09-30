package de.tum.kickercoding.tournamentviewer.manager;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.tum.kickercoding.tournamentviewer.entities.Game;
import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.exceptions.PlayerManagerException;
import de.tum.kickercoding.tournamentviewer.exceptions.PreferenceFileException;
import de.tum.kickercoding.tournamentviewer.exceptions.TournamentManagerException;
import de.tum.kickercoding.tournamentviewer.util.Constants;
import de.tum.kickercoding.tournamentviewer.util.TournamentMode;

/**
 * AppManager solely provides methods callable by Activities.
 * All interactions with the backend happen using this class.
 */
public class AppManager {

	private static AppManager instance = new AppManager();

	private PlayerManager playerManager;

	private PreferenceFileManager preferenceFileManager;

	private TournamentManager tournamentManager;

	public static AppManager getInstance() {
		return instance;
	}

	private AppManager() {
	}

	/**
	 * Initializes all Managers in the correct order to resolve the dependencies.
	 * Called once when app is started
	 *
	 * @param context: context to retrieve sharedPreferences
	 * @throws AppManagerException
	 */
	public void initialize(Context context) throws AppManagerException {
		preferenceFileManager = PreferenceFileManager.getInstance();
		preferenceFileManager.initialize(context);
		try {
			playerManager = PlayerManager.getInstance();
			playerManager.initialize();
		} catch (PreferenceFileException e) {
			throw new AppManagerException("Initialization failed");
		}
	}

	/**
	 * initialize TournamentManager, necessary to start a new Tournament
	 */
	public void initializeTournamentManager(TournamentMode mode) {
		tournamentManager = TournamentManager.getInstance();
		tournamentManager.initialize(mode);
	}

	/**
	 * add new Player;
	 * to permanently save this action call {@link #commitPlayerList()}
	 *
	 * @param name: name of the player
	 * @throws AppManagerException
	 */
	public void addNewPlayer(String name) throws AppManagerException {
		try {
			playerManager.addPlayer(name);
		} catch (PlayerManagerException e) {
			throw new AppManagerException("Player was not saved, wrapped Exception:" + e.toString());
		}
	}


	/**
	 * delete player with specified name;
	 * to permanently save this action call {@link #commitPlayerList()}
	 *
	 * @param name
	 * @throws AppManagerException
	 */
	public void removePlayer(String name) throws AppManagerException {
		playerManager.removePlayer(name);
		// removes player from tournament as well (to prevent inconsistency)
		try {
			tournamentManager.removePlayer(name);
		} catch (TournamentManagerException e) {
			throw new AppManagerException(e.getMessage());
		}
	}

	/**
	 * retrieve the list of all players available
	 *
	 * @return copied list of all players (no reference to the internal list of {@link PlayerManager}
	 */
	public List<Player> getAllPlayers() {
		List<Player> players = playerManager.getPlayers();
		// clone the list and all player objects to avoid changing the original list
		List<Player> playersCopied = new ArrayList<>();
		for (Player p : players) {
			Player copiedPlayer = p.copy();
			playersCopied.add(copiedPlayer);
		}
		return playersCopied;
	}

	/**
	 * get player from specific position of player list
	 *
	 * @param position
	 * @return
	 * @throws AppManagerException
	 */
	public Player getPlayer(int position) throws AppManagerException {
		try {
			return playerManager.getPlayerByPosition(position);
		} catch (PlayerManagerException e) {
			throw new AppManagerException(e.getMessage());
		}
	}

	/**
	 * commit all changes to the list of players to the SharedPreferences
	 *
	 * @throws AppManagerException
	 */
	public void commitPlayerList() throws AppManagerException {
		try {
			playerManager.commitPlayerList();
		} catch (PlayerManagerException e) {
			throw new AppManagerException(e.getMessage());
		}
	}

	/**
	 * load max score from settings
	 *
	 * @return the currently set max score
	 * @throws AppManagerException
	 */
	public int getMaxScore() throws AppManagerException {
		try {
			return preferenceFileManager.loadMaxScore();
		} catch (PreferenceFileException e) {
			throw new AppManagerException(e.getMessage());
		}
	}

	/**
	 * save max score to settings
	 *
	 * @param maxScore
	 * @throws AppManagerException
	 */
	public void setMaxScore(int maxScore) throws AppManagerException {
		try {
			preferenceFileManager.saveMaxScore(maxScore);
		} catch (PreferenceFileException e) {
			throw new AppManagerException(e.getMessage());
		}
	}

	/**
	 * load number of games from settings
	 *
	 * @return the currently set number of game
	 * @throws AppManagerException
	 */
	public int getNumberOfGames() throws AppManagerException {
		try {
			return preferenceFileManager.loadNumberOfGames();
		} catch (PreferenceFileException e) {
			throw new AppManagerException(e.getMessage());
		}
	}

	/**
	 * save number of games to settings
	 *
	 * @param maxScore
	 * @throws AppManagerException
	 */
	public void setNumberOfGames(int maxScore) throws AppManagerException {
		try {
			preferenceFileManager.saveNumberOfGames(maxScore);
		} catch (PreferenceFileException e) {
			throw new AppManagerException(e.getMessage());
		}
	}

	/**
	 * initialize tournament
	 */
	public void startNewTournament() throws AppManagerException {
		try {
			tournamentManager.startNewTournament();
		} catch (TournamentManagerException e) {
			throw new AppManagerException(e.getMessage());
		}
	}

	public void finishTournament() throws AppManagerException {
		try {
			tournamentManager.finishTournament();
			for (Player player : tournamentManager.getPlayers()) {
				playerManager.updatePlayer(player);
			}
			playerManager.commitPlayerList();
		} catch (TournamentManagerException | PlayerManagerException e) {
			throw new AppManagerException(e.getMessage());
		}
	}

	/**
	 * adds player to tournament or removes player if already signed up
	 *
	 * @param player
	 * @return true if player signed up as result of pressing button, false otherwise
	 */
	public boolean toggleParticipation(Player player) throws AppManagerException {
		try {
			return tournamentManager.toggleParticipation(player);
		} catch (TournamentManagerException e) {
			throw new AppManagerException(e.getMessage());
		}
	}

	/**
	 * Commit results of all finished but not yet committed games.
	 */
	public void commitGameResults() throws AppManagerException {
		try {
			tournamentManager.commitGames();
		} catch (TournamentManagerException e) {
			throw new AppManagerException(e.getMessage());
		}
	}


	/**
	 * checks if player is already signed up (i.e. in the list of players for the current tournament)
	 *
	 * @param player
	 * @return true if player is signed up, false else
	 */
	public boolean isSignedUp(String player) {
		return TournamentManager.getInstance().isSignedUp(player);
	}

	public List<Player> getPlayersForTournament() {
		return tournamentManager.getPlayers();
	}

	public List<Game> getGamesForTournament() {
		return tournamentManager.getGames();
	}

	public void generateGame() throws AppManagerException {
		try {
			tournamentManager.generateGame();
		} catch (TournamentManagerException e) {
			throw new AppManagerException(e.getMessage());
		}
	}

	/**
	 * generate as many games as possible such that each player participates in at most one game
	 */
	public void generateRound() throws AppManagerException {
		try {
			tournamentManager.generateRound();
		} catch (TournamentManagerException e) {
			throw new AppManagerException(e.getMessage());
		}
	}

	public void removeLastGame() throws AppManagerException {
		try {
			tournamentManager.removeLastGame();
		} catch (TournamentManagerException e) {
			throw new AppManagerException(e.getMessage());
		}
	}

	public void finalizeGame(int position, int scoreTeam1, int scoreTeam2) throws AppManagerException {
		try {
			tournamentManager.finalizeGame(position, scoreTeam1, scoreTeam2);
		} catch (TournamentManagerException e) {
			throw new AppManagerException(e.getMessage());
		}
	}

	/**
	 * generic method to display an error message as a Toast
	 *
	 * @param context
	 * @param message
	 */
	public void displayError(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * method to display an error message as a Toast which tells the user to save the results because the app
	 * encountered an error which is most likely going to crash the app if ignored
	 *
	 * @param context
	 */
	public void displayFatalError(Context context) {
		Toast.makeText(context, Constants.ERROR_DETECTED_SAVE_YOUR_RESULTS, Toast.LENGTH_LONG).show();
	}

	private void logDebug(String message) {
		Log.d(AppManager.class.toString(), message);
	}
}
