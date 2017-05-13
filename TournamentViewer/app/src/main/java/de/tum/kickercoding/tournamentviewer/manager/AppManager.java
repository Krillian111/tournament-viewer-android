package de.tum.kickercoding.tournamentviewer.manager;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.tum.kickercoding.tournamentviewer.entities.Game;
import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.exceptions.PlayerManagerException;
import de.tum.kickercoding.tournamentviewer.exceptions.PreferenceFileManagerException;
import de.tum.kickercoding.tournamentviewer.exceptions.TournamentManagerException;
import de.tum.kickercoding.tournamentviewer.util.Constants;
import de.tum.kickercoding.tournamentviewer.util.TournamentMode;

/**
 * AppManager solely provides methods callable by Activities.
 * All interactions with the backend happen using this class.
 * Proper exception handling is also taken care of in this class, exceptions should only be thrown to transfer messages
 */
public class AppManager {

	private static final String LOG_TAG = AppManager.class.toString();

	private static AppManager instance = new AppManager();

	//References to managers to keep them from being garbage collected
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
	 * @param applicationContext: applicationContext to retrieve sharedPreferences
	 * @throws AppManagerException
	 */
	public void initialize(Context applicationContext) throws AppManagerException {
		preferenceFileManager = PreferenceFileManager.getInstance();
		preferenceFileManager.initialize(applicationContext);
		playerManager = PlayerManager.getInstance();
		tournamentManager = TournamentManager.getInstance();
	}

	/**
	 * initialize new tournament
	 */
	public void startNewTournament(TournamentMode mode) {
		tournamentManager.startNewTournament(mode);
		tournamentManager.saveTournament();
	}

	/**
	 * add new Player;
	 *
	 * @param name: name of the player
	 * @throws AppManagerException
	 */
	public void addNewPlayer(String name) throws AppManagerException {
		try {
			playerManager.loadPlayerList();
			playerManager.addPlayer(name);
			playerManager.savePlayerList();
		} catch (PlayerManagerException e) {
			throw new AppManagerException(String.format("Try again: name '%s' is already taken", name));
		}
	}

	/**
	 * delete player with specified name;
	 *
	 * @param name
	 * @throws AppManagerException
	 */
	public void removePlayer(String name) throws AppManagerException {
		playerManager.loadPlayerList();
		// propagating to DB is done in method (TODO: extract this)
		playerManager.removePlayer(name);
		// removes player from tournament as well (to prevent inconsistency)
		tournamentManager.loadTournament();
		try {
			tournamentManager.removePlayer(name);
		} catch (TournamentManagerException e) {
			throw new AppManagerException(e.getMessage());
		}
		tournamentManager.saveTournament();
	}

	public void manuallyAdjustElo(String name, double elo) {
		playerManager.loadPlayerList();
		playerManager.manuallyAdjustElo(name, elo);
		playerManager.savePlayerList();
	}

	/**
	 * retrieve the list of all players available
	 *
	 * @return copied list of all players (no reference to the internal list of {@link PlayerManager}
	 */
	public List<Player> getAllPlayers() {
		playerManager.loadPlayerList();
		List<Player> players = playerManager.getPlayers();
		// clone the list and all player objects to avoid changing the original list
		List<Player> playersCopied = new ArrayList<>();
		for (Player p : players) {
			Player copiedPlayer = p.copy();
			playersCopied.add(copiedPlayer);
		}
		return playersCopied;
	}

//	/**
//	 * get player from specific position of player list
//	 *
//	 * @param position
//	 * @return
//	 * @throws AppManagerException
//	 */
//	public Player getPlayer(int position) throws AppManagerException {
//		try {
//			return playerManager.getPlayer(position).copy();
//		} catch (PlayerManagerException e) {
//			throw new AppManagerException(e.getMessage());
//		}
//	}

	public void setOneOnOne(boolean oneOnOne) {
		tournamentManager.loadTournament();
		tournamentManager.setOneOnOne(oneOnOne);
		tournamentManager.saveTournament();
	}

	public boolean isOneOnOne() {
		tournamentManager.loadTournament();
		return tournamentManager.isOneOnOne();
	}

	/**
	 * load max score from settings
	 *
	 * @return the currently set max score
	 * @throws AppManagerException
	 */
	public int getMaxScoreFromSettings() throws AppManagerException {
		try {
			return preferenceFileManager.loadMaxScore();
		} catch (PreferenceFileManagerException e) {
			throw new AppManagerException(e.getMessage());
		}
	}

	/**
	 * load max score from settings
	 *
	 * @return the currently set max score
	 * @throws AppManagerException
	 */
	public int getMaxScoreFromTournament() {
		tournamentManager.loadTournament();
		return tournamentManager.getMaxScore();
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
		} catch (PreferenceFileManagerException e) {
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
		} catch (PreferenceFileManagerException e) {
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
		} catch (PreferenceFileManagerException e) {
			throw new AppManagerException(e.getMessage());
		}
	}

	/**
	 * initialize tournament
	 *
	 * @throws AppManagerException
	 */
	public void setTournamentParameters() throws AppManagerException {
		tournamentManager.loadTournament();
		try {
			tournamentManager.setTournamentParameters();
		} catch (TournamentManagerException e) {
			throw new AppManagerException(e.getMessage());
		}
		tournamentManager.saveTournament();
	}

	/**
	 * Finishes up the tournament, write changes to players to permanent storage
	 *
	 * @throws AppManagerException
	 */
	public void finishTournament() throws AppManagerException {
		tournamentManager.loadTournament();
		playerManager.loadPlayerList();
		try {
			tournamentManager.finishTournament();
			for (Player player : tournamentManager.getPlayers()) {
				playerManager.updatePlayer(player);
			}
			playerManager.savePlayerList();
		} catch (TournamentManagerException | PlayerManagerException e) {
			throw new AppManagerException(e.getMessage());
		}
		playerManager.savePlayerList();
		tournamentManager.saveTournament();
	}

	/**
	 * Checks wether tournament is in progress. This is determined by checking whether the list of games in a
	 * tournament is greater than zero.
	 */
	public boolean isTournamentInProgress() {
		tournamentManager.loadTournament();
		return tournamentManager.isTournamentInProgress();
	}

	/**
	 * adds player to tournament or removes player if already signed up
	 *
	 * @param player
	 * @return true if player signed up as result of pressing button, false otherwise
	 * @throws AppManagerException
	 */
	public boolean toggleParticipation(Player player) throws AppManagerException {
		tournamentManager.loadTournament();
		boolean playerInTournament;
		try {
			playerInTournament = tournamentManager.toggleParticipation(player);
		} catch (TournamentManagerException e) {
			throw new AppManagerException(e.getMessage());
		}
		tournamentManager.saveTournament();
		return playerInTournament;
	}

	/**
	 * Commit results of all finished but not yet committed games.
	 *
	 * @throws AppManagerException
	 */
	public void commitGameResults() throws AppManagerException {
		tournamentManager.loadTournament();
		try {
			tournamentManager.commitGames();
		} catch (TournamentManagerException e) {
			throw new AppManagerException(e.getMessage());
		}
		tournamentManager.saveTournament();
	}

	public void revertGame(int position) throws AppManagerException {
		tournamentManager.loadTournament();
		try {
			tournamentManager.revertGame(position);
		} catch (TournamentManagerException e) {
			throw new AppManagerException(e.getMessage());
		}
		tournamentManager.saveTournament();
	}

	public void removeGame(int position) throws AppManagerException {
		tournamentManager.loadTournament();
		try {
			tournamentManager.removeGame(position);
		} catch (TournamentManagerException e) {
			throw new AppManagerException("Failed to remove game:" + e.getMessage());
		}
		tournamentManager.saveTournament();
	}

	/**
	 * checks if player is already signed up (i.e. in the list of players for the current tournament)
	 *
	 * @param player
	 * @return true if player is signed up, false else
	 */
	public boolean isSignedUp(String player) {
		tournamentManager.loadTournament();
		return tournamentManager.isSignedUp(player);
	}

	public List<Player> getPlayersForTournament() {
		tournamentManager.loadTournament();
		return tournamentManager.getPlayers();
	}

	public List<Game> getGamesForTournament() {
		tournamentManager.loadTournament();
		return tournamentManager.getGames();
	}

	/**
	 * generate a single game for the Tournament
	 *
	 * @throws AppManagerException
	 */
	public void generateGame() throws AppManagerException {
		tournamentManager.loadTournament();
		try {
			tournamentManager.generateGame();
		} catch (TournamentManagerException e) {
			throw new AppManagerException(e.getMessage());
		}
		tournamentManager.saveTournament();
	}

	/**
	 * generate as many games as possible such that each player participates in at most one game
	 *
	 * @throws AppManagerException
	 */
	public void generateRound() throws AppManagerException {
		tournamentManager.loadTournament();
		try {
			tournamentManager.generateRound();
		} catch (TournamentManagerException e) {
			throw new AppManagerException(e.getMessage());
		}
		tournamentManager.saveTournament();
	}

	public void generatePlayoffs() throws AppManagerException {
		tournamentManager.loadTournament();
		try {
			tournamentManager.generatePlayoffs();
		} catch (TournamentManagerException e) {
			throw new AppManagerException(e.getMessage());
		}
		tournamentManager.saveTournament();
	}

	/**
	 * finish up a game, making it eligible for commitment of results
	 *
	 * @param position
	 * @param scoreTeam1
	 * @param scoreTeam2
	 * @throws AppManagerException
	 */
	public void finalizeGame(int position, int scoreTeam1, int scoreTeam2) throws AppManagerException {
		tournamentManager.loadTournament();
		try {
			tournamentManager.finalizeGame(position, scoreTeam1, scoreTeam2);
		} catch (TournamentManagerException e) {
			throw new AppManagerException(e.getMessage());
		}
		tournamentManager.saveTournament();
	}

	/**
	 * generic method to display an error message as a Toast
	 *
	 * @param context
	 * @param message
	 */
	public void displayMessage(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public void displayMessageShort(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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

//	public void saveTournamentManagerInBundle(Bundle savedInstanceState) {
//		savedInstanceState.putString(Constants.KEY_TOURNAMENT_MANAGER, tournamentManager.toJson());
//	}

//	public void reinitializeTournamentManager(Context context, Bundle savedInstanceState) {
//		String tmAsString = savedInstanceState.getString(Constants.KEY_TOURNAMENT_MANAGER);
//		try {
//			TournamentManager.initFromJson(tmAsString);
//		} catch (TournamentManagerException e) {
//			displayMessage(context, "Fatal error; error during reinitialization of TournamentManager: " + e.getMessage
//					());
//		}
//	}
}
