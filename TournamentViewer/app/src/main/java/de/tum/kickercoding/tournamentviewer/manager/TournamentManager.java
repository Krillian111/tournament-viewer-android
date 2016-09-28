package de.tum.kickercoding.tournamentviewer.manager;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.tum.kickercoding.tournamentviewer.entities.Game;
import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.entities.Tournament;
import de.tum.kickercoding.tournamentviewer.exceptions.PreferenceFileException;
import de.tum.kickercoding.tournamentviewer.exceptions.TournamentManagerException;
import de.tum.kickercoding.tournamentviewer.tournament.Matchmaking;
import de.tum.kickercoding.tournamentviewer.tournament.monsterdyp.MonsterDypMatchmaking;
import de.tum.kickercoding.tournamentviewer.util.TournamentMode;

// TODO: write unit tests
class TournamentManager {

	static TournamentManager instance = new TournamentManager();

	static TournamentManager getInstance() {
		return instance;
	}

	private TournamentManager() {
	}

	private Matchmaking matchmaking;

	private Tournament currentTournament;


	void initialize(TournamentMode mode) {
		this.currentTournament = new Tournament();
		switch (mode) {
			case MONSTERDYP:
				matchmaking = MonsterDypMatchmaking.getInstance();
				break;
			default:
				Log.e(TournamentManager.class.toString(),
						String.format("initialize: used mode %s not yet implemented", mode.getName()));
		}
	}

	void startNewTournament() throws TournamentManagerException {
		try {
			int maxScore = PreferenceFileManager.getInstance().loadMaxScore();
			int numberOfGames = PreferenceFileManager.getInstance().loadNumberOfGames();
			currentTournament.setMaxScore(maxScore);
			currentTournament.setNumberOfGames(numberOfGames);
		} catch (PreferenceFileException e) {
			throw new TournamentManagerException("Failed to start new tournament, wrapped Exception:" + e.toString());
		}
	}

	boolean toggleParticipation(Player player) {
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

	void generateRound() {
		List<Game> newGames;
		if (isOneOnOne()) {
			newGames = matchmaking.generateRound1on1(getPlayers());
		} else {
			newGames = matchmaking.generateRound2on2(getPlayers());
		}
		for (Game game : newGames) {
			addGame(game);
		}

	}

	void generateGame() {
		Game game;
		if (isOneOnOne()) {
			game = matchmaking.generateGame1on1(getPlayers());
		} else {
			game = matchmaking.generateGame2on2(getPlayers());
		}
		addGame(game);
	}

	/**
	 * Commit results of all finished but not yet committed games.
	 */
	void commitGameResults() throws TournamentManagerException {
		List<Game> games = getGamesToCommit();
		for (Game game : games) {
			commitGameResult(game);
		}
	}

	private void commitGameResult(Game game) throws TournamentManagerException {
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
			// add a tied and a played game to both teams (played = implicitly)
			addTiedGame(team1);
			addTiedGame(team2);
			// TODO: update ranking score of all players
		} else if (scoreTeam1 > scoreTeam2) {
			// add a won game to team 1, a lost game to team 2 and a played game to both teams (played = implicitly)
			addWonGame(team1);
			addLostGame(team2);
			// TODO: update ranking score of all players
		} else {
			// add a won game to team 2, a lost game to team 1 and a played game to both teams (played = implicitly)
			addWonGame(team2);
			addLostGame(team1);
			// TODO: update ranking score of all players
		}
		game.setResultCommitted(true);
		if (isOneOnOne()) {
			Log.d(TournamentManager.class.toString(), String.format("commitGameResult: game %s vs %s was commited " +
					"with result (%d:%d)", team1.get(0), team2.get(0), scoreTeam1, scoreTeam2));
		} else {
			Log.d(TournamentManager.class.toString(), String.format("commitGameResult: game with team1(%s,%s) vs " +
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
			playerToUpdate.setPlayedGames(playerToUpdate.getPlayedGames() + 1);
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
			playerToUpdate.setPlayedGames(playerToUpdate.getPlayedGames() + 1);
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
			playerToUpdate.setPlayedGames(playerToUpdate.getPlayedGames() + 1);
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

	void addGame(Game game) {
		currentTournament.addGame(game);
	}

	boolean removeLastGame() {
		return currentTournament.removeLastGame();
	}

	List<Game> getGames() {
		return currentTournament.getGames();
	}

	//TODO: handle editing finished game (how do we allow "admin edit" or just extra confirm?)
	void finalizeGame(int position, int scoreTeam1, int scoreTeam2) throws TournamentManagerException {
		int maxScore = currentTournament.getMaxScore();
		if (position >= getGames().size()) {
			throw new IndexOutOfBoundsException("game with position " + position + " does not exist");
		} else if (scoreTeam1 > maxScore || scoreTeam2 > maxScore || scoreTeam1 < 0 || scoreTeam2 < 0) {
			throw new TournamentManagerException(String.format("one of the entered scores is invalid: team1:%d, " +
					"team2:%d", scoreTeam1, scoreTeam2));
		} else {
			Game gameToBeFinalized = currentTournament.getGame(position);
			gameToBeFinalized.setScoreTeam1(scoreTeam1);
			gameToBeFinalized.setScoreTeam2(scoreTeam2);
			gameToBeFinalized.setFinished(true);
		}
	}

	void addPlayer(Player player) {
		currentTournament.addPlayer(player);
	}

	boolean removePlayer(Player player) {
		return currentTournament.removePlayer(player);
	}

	boolean removePlayer(String name) {
		// use fake player to force removal; players with identical name are considered equal
		return removePlayer(new Player(name));
	}

	List<Player> getPlayers() {
		return currentTournament.getPlayers();
	}

	public boolean isOneOnOne() {
		return currentTournament.isOneOnOne();
	}
}
