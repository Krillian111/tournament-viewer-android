package de.tum.kickercoding.tournamentviewer.manager;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.tum.kickercoding.tournamentviewer.entities.Game;
import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.entities.Tournament;
import de.tum.kickercoding.tournamentviewer.exceptions.PreferenceFileManagerException;
import de.tum.kickercoding.tournamentviewer.exceptions.TournamentManagerException;
import de.tum.kickercoding.tournamentviewer.tournament.Matchmaking;
import de.tum.kickercoding.tournamentviewer.tournament.monsterdyp.MonsterDypMatchmaking;
import de.tum.kickercoding.tournamentviewer.util.TournamentMode;

// TODO: write unit tests
class TournamentManager {

	private final String LOG_TAG = TournamentManager.class.toString();

	private static TournamentManager instance = new TournamentManager();

	static TournamentManager getInstance() {
		return instance;
	}

	private TournamentManager() {
	}

	private Matchmaking matchmaking;

	private Tournament currentTournament;

	void initialize() {
		currentTournament = new Tournament();
	}

	void initMatchmaking() throws TournamentManagerException {
		TournamentMode mode = currentTournament.getMode();
		if (mode != null) {
			switch (mode) {
				case MONSTERDYP:
					matchmaking = MonsterDypMatchmaking.getInstance();
					break;
				default:
					Log.e(LOG_TAG,
							String.format("initialize: mode %s not yet implemented", currentTournament.getMode()));
			}
		} else {
			throw new TournamentManagerException("TournamentMode was not set, cannot create games");
		}
	}

	void startNewTournament(TournamentMode mode) {
		currentTournament = new Tournament();
		currentTournament.setMode(mode);
	}

	void setTournamentParameters() throws TournamentManagerException {
		try {
			int maxScore = PreferenceFileManager.getInstance().loadMaxScore();
			int numberOfGames = PreferenceFileManager.getInstance().loadNumberOfGames();
			currentTournament.setMaxScore(maxScore);
			currentTournament.setNumberOfGames(numberOfGames);

		} catch (PreferenceFileManagerException e) {
			throw new TournamentManagerException("Failed to start new tournament, wrapped Exception:" + e.toString());
		}
	}

	void saveTournament() throws PreferenceFileManagerException {
		PreferenceFileManager.getInstance().saveTournament(currentTournament);
	}

	void loadTournament() throws PreferenceFileManagerException, TournamentManagerException {
		Tournament loadedTournament = PreferenceFileManager.getInstance().loadTournament();
		if (loadedTournament != null) {
			currentTournament = loadedTournament;
		} else {
			throw new TournamentManagerException("Loading tournament failed; Tournament must be created from scratch");
		}
	}

	void finishTournament() throws TournamentManagerException {
		if (currentTournament.isFinished()) {
			throw new TournamentManagerException("Can't finish: Tournament was already finished previously");
		}
		currentTournament.setFinished(true);
	}

	boolean toggleParticipation(Player player) throws TournamentManagerException {
		if (currentTournament.isFinished()) {
			throw new TournamentManagerException("Can't toggle player participation: Tournament finished");
		}
		if (currentTournament.getPlayers().contains(player)) {
			removePlayer(player);
			return false;
		} else {
			addPlayer(player);
			return true;
		}
	}

	boolean isSignedUp(String playerName) {
		return currentTournament.getPlayers().contains(new Player(playerName));
	}

	void generateRound() throws TournamentManagerException {
		if (currentTournament.isFinished()) {
			throw new TournamentManagerException("Can't create new round: Tournament finished");
		}
		if (matchmaking == null) {
			initMatchmaking();
		}
		List<Game> newGames = matchmaking.generateRound(getPlayers(), isOneOnOne());
		for (Game game : newGames) {
			addGame(game);
		}
	}

	void generateGame() throws TournamentManagerException {
		if (currentTournament.isFinished()) {
			throw new TournamentManagerException("Can't create new game: Tournament finished");
		}
		if (matchmaking == null) {
			initMatchmaking();
		}
		Game game = matchmaking.generateGame(getPlayers(), isOneOnOne());
		addGame(game);
	}

	/**
	 * Commit results of all finished but not yet committed games.
	 */
	void commitGames() throws TournamentManagerException {
		if (currentTournament.isFinished()) {
			throw new TournamentManagerException("Can't commit games: Tournament finished");
		}
		List<Game> games = getGamesToCommit();
		for (Game game : games) {
			commitGame(game);
		}
	}

	private void commitGame(Game game) throws TournamentManagerException {
		if (game.isResultCommitted()) {
			throw new TournamentManagerException("Error: game was already committed");
		}
		if (!game.isFinished()) {
			throw new TournamentManagerException("Cannot commit unfinished game");
		}

		// TODO: calculate ranking score (change) for all players
		int scoreTeam1 = game.getScoreTeam1();
		List<String> team1 = game.getTeam1PlayerNames();
		int scoreTeam2 = game.getScoreTeam2();
		List<String> team2 = game.getTeam2PlayerNames();
		if (scoreTeam1 == scoreTeam2) {
			// add a tied game to both teams
			addTiedGame(team1);
			addTiedGame(team2);
			// TODO: update ranking score of all players
		} else if (scoreTeam1 > scoreTeam2) {
			// add a won game to team 1, a lost game to team 2
			addWonGame(team1);
			addLostGame(team2);
			// TODO: update ranking score of all players
		} else {
			// add a won game to team 2, a lost game to team 1
			addWonGame(team2);
			addLostGame(team1);
			// TODO: update ranking score of all players
		}
		game.setResultCommitted(true);
		if (isOneOnOne()) {
			Log.d(LOG_TAG, String.format("commitGame: game %s vs %s was commited " +
					"with result (%d:%d)", team1.get(0), team2.get(0), scoreTeam1, scoreTeam2));
		} else {
			Log.d(LOG_TAG, String.format("commitGame: game with team1(%s,%s) vs " +
							"team2(%s,%s) was commited with result (%d:%d)",
					team1.get(0), team1.get(1), team2.get(0), team2.get(1), scoreTeam1, scoreTeam2));
		}

	}

	private List<Game> getGamesToCommit() {
		List<Game> gamesToCommit = new ArrayList<>();
		for (Game game : currentTournament.getGames()) {
			if (game.isFinished() && !game.isResultCommitted()) {
				gamesToCommit.add(game);
			}
		}
		return gamesToCommit;
	}

	/**
	 * Add a played and a tied game to players.
	 *
	 * @param playersToUpdate The players that should be updated.
	 */
	private void addTiedGame(List<String> playersToUpdate) throws TournamentManagerException {
		Player playerToUpdate;
		for (String playerName : playersToUpdate) {
			playerToUpdate = getPlayerByName(playerName);
			playerToUpdate.setTiedGamesInTournament(playerToUpdate.getTiedGamesInTournament() + 1);
			playerToUpdate.setTiedGames(playerToUpdate.getTiedGames() + 1);
		}
	}

	/**
	 * Add a played and a won game to players.
	 *
	 * @param playersToUpdate The players that should be updated.
	 */
	private void addWonGame(List<String> playersToUpdate) throws TournamentManagerException {
		Player playerToUpdate;
		for (String playerName : playersToUpdate) {
			playerToUpdate = getPlayerByName(playerName);
			playerToUpdate.setWonGamesInTournament(playerToUpdate.getWonGamesInTournament() + 1);
			playerToUpdate.setWonGames(playerToUpdate.getWonGames() + 1);
		}
	}

	/**
	 * Add a played and a lost game to players.
	 *
	 * @param playersToUpdate The players that should be updated.
	 */
	private void addLostGame(List<String> playersToUpdate) throws TournamentManagerException {
		Player playerToUpdate;
		for (String playerName : playersToUpdate) {
			playerToUpdate = getPlayerByName(playerName);
			playerToUpdate.setLostGamesInTournament(playerToUpdate.getLostGamesInTournament() + 1);
			playerToUpdate.setLostGames(playerToUpdate.getLostGames() + 1);
		}
	}

	/**
	 * Get the {@link Player} for a specific name.
	 *
	 * @param name The name of the {@link Player} you want to get.
	 * @return The {@link Player} with the given name.
	 * @throws TournamentManagerException If there is no {@link Player} with the given name.
	 */
	Player getPlayerByName(String name) throws TournamentManagerException {
		for (Player player : getPlayers()) {
			if (player.getName() == name) {
				return player;
			}
		}
		throw new TournamentManagerException("No player found for the requested name");
	}

	private void addGame(Game game) {
		currentTournament.addGame(game);
	}

	boolean removeLastGame() throws TournamentManagerException {
		if (currentTournament.isFinished()) {
			throw new TournamentManagerException("Can't remove game: Tournament finished");
		}
		return currentTournament.removeLastGame();
	}

	List<Game> getGames() {
		return currentTournament.getGames();
	}

	//TODO: handle editing finished game (how do we allow "admin edit" or just extra confirm?)
	void finalizeGame(int position, int scoreTeam1, int scoreTeam2) throws TournamentManagerException {
		if (currentTournament.isFinished()) {
			throw new TournamentManagerException("Can't finalize game: Tournament finished");
		}
		int maxScore = currentTournament.getMaxScore();
		if (position >= getGames().size()) {
			throw new IndexOutOfBoundsException("game with position " + position + " does not exist");
		} else if (scoreTeam1 > maxScore || scoreTeam2 > maxScore || scoreTeam1 < 0 || scoreTeam2 < 0) {
			throw new TournamentManagerException(String.format("one of the entered scores is invalid: team1:%d, " +
					"team2:%d", scoreTeam1, scoreTeam2));
		} else {
			Game gameToBeFinalized = currentTournament.getGame(position);
			if (gameToBeFinalized.isResultCommitted()) {
				throw new TournamentManagerException("Game was already committed, can't alter results");
			}
			gameToBeFinalized.setScoreTeam1(scoreTeam1);
			gameToBeFinalized.setScoreTeam2(scoreTeam2);
			gameToBeFinalized.setFinished(true);
		}
	}

	void addPlayer(Player player) {
		// reset stats for tournament
		player.setWonGamesInTournament(0);
		player.setLostGamesInTournament(0);
		player.setTiedGamesInTournament(0);
		currentTournament.addPlayer(player);
		// sort list after adding player
		List<Player> players = currentTournament.getPlayers();
		sortPlayersByWinRate(players);
	}

	List<Player> getPlayers() {
		List<Player> players = currentTournament.getPlayers();
		// sort list as win rates change
		sortPlayersByWinRate(players);
		return players;
	}

	private void sortPlayersByWinRate(List<Player> list) {
		Collections.sort(list, new Comparator<Player>() {
			public int compare(Player p1, Player p2) {
				return Double.compare(p2.getWinRateInTournament(), p1.getWinRateInTournament());
			}
		});
	}

	private boolean removePlayer(Player player) {
		return currentTournament.removePlayer(player);
	}

	boolean removePlayer(String name) throws TournamentManagerException {
		if (currentTournament.isFinished()) {
			throw new TournamentManagerException("Can't remove player: Tournament finished");
		}
		// use fake player to force removal; players with identical name are considered equal
		return removePlayer(new Player(name));
	}

	boolean isOneOnOne() {
		return currentTournament.isOneOnOne();
	}

	void setOneOnOne(boolean oneOnOne) {
		currentTournament.setOneOnOne(oneOnOne);
	}


	int getMaxScore() {
		return currentTournament.getMaxScore();
	}
}
