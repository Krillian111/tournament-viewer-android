package de.tum.kickercoding.tournamentviewer.manager;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.tum.kickercoding.tournamentviewer.entities.Game;
import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.entities.Tournament;
import de.tum.kickercoding.tournamentviewer.exceptions.PreferenceFileManagerException;
import de.tum.kickercoding.tournamentviewer.exceptions.TournamentManagerException;
import de.tum.kickercoding.tournamentviewer.tournament.Matchmaking;
import de.tum.kickercoding.tournamentviewer.tournament.monsterdyp.MonsterDypMatchmaking;
import de.tum.kickercoding.tournamentviewer.util.TournamentMode;
import de.tum.kickercoding.tournamentviewer.util.Utils;

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

	boolean isTournamentInProgress() {
		return currentTournament.getGames().size() > 0;
	}

	boolean toggleParticipation(Player player) throws TournamentManagerException {
		if (currentTournament.isFinished()) {
			throw new TournamentManagerException("Can't toggle player participation: Tournament finished");
		}
		if (currentTournament.getPlayers().contains(player)) {
			// delete unfinished games of player
			List<Game> games = getGames();
			List<Game> gamesToDelete = new ArrayList<>();
			for (Game game : games) {
				if (!game.isResultCommitted()) {
					gamesToDelete.add(game);
				}
			}
			games.removeAll(gamesToDelete);
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
		List<Game> newGames = matchmaking.generateRound(getPlayers(), isOneOnOne(), getGames());
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
		Game game = matchmaking.generateGame(getPlayers(), isOneOnOne(), getGames());
		addGame(game);
	}

	// TODO: implement toggling boolean semifinalsplayed
	// TODO: prevent other games from being generated
	// TODO: prevent draw for semi finals
	// TODO: implement multiple game generation for semi finals (additional activity?)
	void generatePlayoffs() throws TournamentManagerException {
		List<Player> players = getPlayers();
		if (currentTournament.isSemiFinalsPlayed() ||
				(isOneOnOne() && players.size() < 4) ||
				(!isOneOnOne() && players.size() < 8)) {
			generateFinal(players);
		} else {
			generateSemiFinals(players);
		}
	}

	void generateSemiFinals(List<Player> players) {
		List<Player> game1team1;
		List<Player> game1team2;
		List<Player> game2team1;
		List<Player> game2team2;
		if (isOneOnOne()) {
			game1team1 = Arrays.asList(players.get(0));
			game1team2 = Arrays.asList(players.get(3));
			game2team1 = Arrays.asList(players.get(1));
			game2team2 = Arrays.asList(players.get(2));
		} else {
			game1team1 = Arrays.asList(players.get(0), players.get(4));
			game1team2 = Arrays.asList(players.get(3), players.get(7));
			game2team1 = Arrays.asList(players.get(1), players.get(5));
			game2team2 = Arrays.asList(players.get(2), players.get(6));
		}
		List<Player> participantsGame1 = new ArrayList<>(game1team1);
		participantsGame1.addAll(game1team2);
		List<Player> participantsGame2 = new ArrayList<>(game2team1);
		participantsGame2.addAll(game2team2);
		addGame(new Game(participantsGame1));
		addGame(new Game(participantsGame2));
	}

	void generateFinal(List<Player> players) throws TournamentManagerException {
		List<Game> games = getGames();
		Game semifinal1 = games.get(games.size() - 1);
		Game semifinal2 = games.get(games.size() - 2);
		List<Player> winnerTeam1 = getWinnerTeam(semifinal1);
		List<Player> winnerTeam2 = getWinnerTeam(semifinal2);
		List<Player> participantsFinal = new ArrayList<>(winnerTeam1);
		participantsFinal.addAll(winnerTeam2);
		addGame(new Game(participantsFinal));
	}

	private List<Player> getWinnerTeam(final Game game) throws TournamentManagerException {
		if (game.getScoreTeam1() > game.getScoreTeam2()) {
			return game.getTeam1();
		} else if (game.getScoreTeam1() < game.getScoreTeam2()) {
			return game.getTeam2();
		} else {
			throw new TournamentManagerException("Draw in a semi final not possible!");
		}
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

	private List<Game> getGamesToCommit() {
		List<Game> gamesToCommit = new ArrayList<>();
		for (Game game : currentTournament.getGames()) {
			if (game.isFinished() && !game.isResultCommitted()) {
				gamesToCommit.add(game);
			}
		}
		return gamesToCommit;
	}

	private void commitGame(Game game) throws TournamentManagerException {
		if (game.isResultCommitted()) {
			throw new TournamentManagerException("Error: game was already committed");
		}
		if (!game.isFinished()) {
			throw new TournamentManagerException("Cannot commit unfinished game");
		}

		int scoreTeam1 = game.getScoreTeam1();
		List<String> team1 = game.getTeam1PlayerNames();
		int scoreTeam2 = game.getScoreTeam2();
		List<String> team2 = game.getTeam2PlayerNames();
		if (scoreTeam1 == scoreTeam2) {
			addTiedGame(team1, scoreTeam1);
			addTiedGame(team2, scoreTeam1);
		} else if (scoreTeam1 > scoreTeam2) {
			addWonGame(team1, scoreTeam1, scoreTeam2);
			addLostGame(team2, scoreTeam1, scoreTeam2);
		} else {
			addWonGame(team2, scoreTeam2, scoreTeam1);
			addLostGame(team1, scoreTeam2, scoreTeam1);
		}
		List<Player> eloUpdatedPlayers = Utils.calculateEloAfterGame(game);
		commitEloUpdates(eloUpdatedPlayers);

		game.setResultCommitted(true);
		if (isOneOnOne()) {
			Log.d(LOG_TAG, String.format("commitGame: game %s vs %s was committed " +
					"with result (%d:%d)", team1.get(0), team2.get(0), scoreTeam1, scoreTeam2));
		} else {
			Log.d(LOG_TAG, String.format("commitGame: game with team1(%s,%s) vs " +
							"team2(%s,%s) was commited with result (%d:%d)",
					team1.get(0), team1.get(1), team2.get(0), team2.get(1), scoreTeam1, scoreTeam2));
		}
	}

	/**
	 * revert a game after it being permanently committed
	 *
	 * @param position: position of the game in the list
	 * @throws TournamentManagerException
	 */
	void revertGame(int position) throws TournamentManagerException {
		if (currentTournament.isFinished()) {
			throw new TournamentManagerException("Can't revert game: Tournament finished");
		}
		if (position >= getGames().size()) {
			throw new IndexOutOfBoundsException("game with position " + position + " does not exist");
		}
		Game game = currentTournament.getGame(position);
		if (!game.isResultCommitted()) {
			throw new TournamentManagerException("Error reverting: game was not committed");
		}
		if (!game.isFinished()) {
			throw new TournamentManagerException("Cannot revert unfinished game");
		}

		int scoreTeam1 = game.getScoreTeam1();
		List<String> team1 = game.getTeam1PlayerNames();
		int scoreTeam2 = game.getScoreTeam2();
		List<String> team2 = game.getTeam2PlayerNames();
		if (scoreTeam1 == scoreTeam2) {
			removeTiedGame(team1, scoreTeam1);
			removeTiedGame(team2, scoreTeam2);
		} else if (scoreTeam1 > scoreTeam2) {
			removeWonGame(team1, scoreTeam1, scoreTeam2);
			removeLostGame(team2, scoreTeam1, scoreTeam2);
		} else {
			removeWonGame(team2, scoreTeam2, scoreTeam1);
			removeLostGame(team1, scoreTeam2, scoreTeam1);
		}
		revertEloUpdates(team1);
		revertEloUpdates(team2);
		game.setScoreTeam1(0);
		game.setScoreTeam2(0);
		game.setResultCommitted(false);
		if (isOneOnOne()) {
			Log.d(LOG_TAG, String.format("revertGame: game %s vs %s was reverted" +
					", result was (%d:%d)", team1.get(0), team2.get(0), scoreTeam1, scoreTeam2));
		} else {
			Log.d(LOG_TAG, String.format("revertGame: game with team1(%s,%s) vs " +
							"team2(%s,%s) was reverted, result was (%d:%d)",
					team1.get(0), team1.get(1), team2.get(0), team2.get(1), scoreTeam1, scoreTeam2));
		}
	}

	/**
	 * Add a played and a tied game to players.
	 *
	 * @param playersToUpdate The players that should be updated.
	 */
	private void addTiedGame(List<String> playersToUpdate, int score) throws TournamentManagerException {
		Player playerToUpdate;
		for (String playerName : playersToUpdate) {
			playerToUpdate = getPlayerByName(playerName);
			playerToUpdate.setTiedGamesInTournament(playerToUpdate.getTiedGamesInTournament() + 1);
			playerToUpdate.setTiedGames(playerToUpdate.getTiedGames() + 1);
			playerToUpdate.setGoalsShot(playerToUpdate.getGoalsShot() + score);
			playerToUpdate.setGoalsShotInTournament(playerToUpdate.getGoalsShotInTournament() + score);
			playerToUpdate.setGoalsReceived(playerToUpdate.getGoalsReceived() + score);
			playerToUpdate.setGoalsReceivedInTournament(playerToUpdate.getGoalsReceivedInTournament() + score);
		}
	}

	private void removeTiedGame(List<String> playersToRevert, int score) throws
			TournamentManagerException {
		Player playerToUpdate;
		for (String playerName : playersToRevert) {
			playerToUpdate = getPlayerByName(playerName);
			playerToUpdate.setTiedGamesInTournament(playerToUpdate.getTiedGamesInTournament() - 1);
			playerToUpdate.setTiedGames(playerToUpdate.getTiedGames() - 1);
			playerToUpdate.setGoalsShot(playerToUpdate.getGoalsShot() - score);
			playerToUpdate.setGoalsShotInTournament(playerToUpdate.getGoalsShotInTournament() - score);
			playerToUpdate.setGoalsReceived(playerToUpdate.getGoalsReceived() - score);
			playerToUpdate.setGoalsReceivedInTournament(playerToUpdate.getGoalsReceivedInTournament() - score);
		}
	}

	/**
	 * Add a played and a won game to players.
	 *
	 * @param playersToUpdate The players that should be updated.
	 */
	private void addWonGame(List<String> playersToUpdate, int scoreWinner, int scoreLoser) throws
			TournamentManagerException {
		Player playerToUpdate;
		for (String playerName : playersToUpdate) {
			playerToUpdate = getPlayerByName(playerName);
			playerToUpdate.setWonGamesInTournament(playerToUpdate.getWonGamesInTournament() + 1);
			playerToUpdate.setWonGames(playerToUpdate.getWonGames() + 1);
			playerToUpdate.setGoalsShot(playerToUpdate.getGoalsShot() + scoreWinner);
			playerToUpdate.setGoalsShotInTournament(playerToUpdate.getGoalsShotInTournament() + scoreWinner);
			playerToUpdate.setGoalsReceived(playerToUpdate.getGoalsReceived() + scoreLoser);
			playerToUpdate.setGoalsReceivedInTournament(playerToUpdate.getGoalsReceivedInTournament() + scoreLoser);
		}
	}

	private void removeWonGame(List<String> playersToRevert, int scoreWinner, int scoreLoser) throws
			TournamentManagerException {
		Player playerToUpdate;
		for (String playerName : playersToRevert) {
			playerToUpdate = getPlayerByName(playerName);
			playerToUpdate.setWonGamesInTournament(playerToUpdate.getWonGamesInTournament() - 1);
			playerToUpdate.setWonGames(playerToUpdate.getWonGames() - 1);
			playerToUpdate.setGoalsShot(playerToUpdate.getGoalsShot() - scoreWinner);
			playerToUpdate.setGoalsShotInTournament(playerToUpdate.getGoalsShotInTournament() - scoreWinner);
			playerToUpdate.setGoalsReceived(playerToUpdate.getGoalsReceived() - scoreLoser);
			playerToUpdate.setGoalsReceivedInTournament(playerToUpdate.getGoalsReceivedInTournament() - scoreLoser);
		}
	}

	/**
	 * Add a played and a lost game to players.
	 *
	 * @param playersToUpdate The players that should be updated.
	 */
	private void addLostGame(List<String> playersToUpdate, int scoreWinner, int scoreLoser) throws
			TournamentManagerException {
		Player playerToUpdate;
		for (String playerName : playersToUpdate) {
			playerToUpdate = getPlayerByName(playerName);
			playerToUpdate.setLostGamesInTournament(playerToUpdate.getLostGamesInTournament() + 1);
			playerToUpdate.setLostGames(playerToUpdate.getLostGames() + 1);
			playerToUpdate.setGoalsShot(playerToUpdate.getGoalsShot() + scoreLoser);
			playerToUpdate.setGoalsShotInTournament(playerToUpdate.getGoalsShotInTournament() + scoreLoser);
			playerToUpdate.setGoalsReceived(playerToUpdate.getGoalsReceived() + scoreWinner);
			playerToUpdate.setGoalsReceivedInTournament(playerToUpdate.getGoalsReceivedInTournament() + scoreWinner);
		}
	}

	private void removeLostGame(List<String> playersToRevert, int scoreWinner, int scoreLoser) throws
			TournamentManagerException {
		Player playerToUpdate;
		for (String playerName : playersToRevert) {
			playerToUpdate = getPlayerByName(playerName);
			playerToUpdate.setLostGamesInTournament(playerToUpdate.getLostGamesInTournament() - 1);
			playerToUpdate.setLostGames(playerToUpdate.getLostGames() - 1);
			playerToUpdate.setGoalsShot(playerToUpdate.getGoalsShot() - scoreLoser);
			playerToUpdate.setGoalsShotInTournament(playerToUpdate.getGoalsShotInTournament() - scoreLoser);
			playerToUpdate.setGoalsReceived(playerToUpdate.getGoalsReceived() - scoreWinner);
			playerToUpdate.setGoalsReceivedInTournament(playerToUpdate.getGoalsReceivedInTournament() - scoreWinner);
		}
	}

	private void commitEloUpdates(final List<Player> eloUpdatedPlayers) throws TournamentManagerException {
		for (Player eloUpdatedPlayer : eloUpdatedPlayers) {
			Player playerToUpdate = getPlayerByName(eloUpdatedPlayer.getName());
			playerToUpdate.setElo(eloUpdatedPlayer.getElo());
			playerToUpdate.setEloChangeFromLastGame(eloUpdatedPlayer.getEloChangeFromLastGame());
		}
	}

	private void revertEloUpdates(final List<String> playerNames) throws TournamentManagerException {
		for (String playerName : playerNames) {
			Player playerToUpdate = getPlayerByName(playerName);
			playerToUpdate.setElo(playerToUpdate.getElo() - playerToUpdate.getEloChangeFromLastGame());
			// prevent unwanted effects from reverting multiple games in a row
			playerToUpdate.setEloChangeFromLastGame(0.0);
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

	void removeGame(int position) throws TournamentManagerException {
		if (currentTournament.isFinished()) {
			throw new TournamentManagerException("Can't delete game: Tournament finished");
		}
		try {
			revertGame(position);
		} catch (TournamentManagerException e) {
			// swallow exception as it only indicates error when game is supposed to be reset;
			// method is just reused to reset a potentially committed game
		}
		currentTournament.getGames().remove(position);
	}

	List<Game> getGames() {
		return currentTournament.getGames();
	}

	void finalizeGame(int position, int scoreTeam1, int scoreTeam2) throws TournamentManagerException {
		if (currentTournament.isFinished()) {
			throw new TournamentManagerException("Can't finalize game: Tournament finished");
		}
		int maxScore = currentTournament.getMaxScore();
		if (position >= getGames().size()) {
			throw new IndexOutOfBoundsException("game with position " + position + " does not exist");
		}
		if (scoreTeam1 > maxScore || scoreTeam2 > maxScore || scoreTeam1 < 0 || scoreTeam2 < 0) {
			throw new TournamentManagerException(String.format("one of the entered scores is invalid: team1:%d, " +
					"team2:%d", scoreTeam1, scoreTeam2));
		}
		Game gameToBeFinalized = currentTournament.getGame(position);
		if (gameToBeFinalized.isResultCommitted()) {
			throw new TournamentManagerException("Game was already committed, can't alter results");
		}
		gameToBeFinalized.setScoreTeam1(scoreTeam1);
		gameToBeFinalized.setScoreTeam2(scoreTeam2);
		gameToBeFinalized.setFinished(true);
	}

	private void addPlayer(Player player) {
		// reset stats for tournament
		player.setWonGamesInTournament(0);
		player.setLostGamesInTournament(0);
		player.setTiedGamesInTournament(0);
		player.setGoalsShotInTournament(0);
		player.setGoalsReceivedInTournament(0);
		currentTournament.addPlayer(player);
		// sort list after adding player
		List<Player> players = currentTournament.getPlayers();
		Utils.sortPlayersByWinRate(players);
	}

	List<Player> getPlayers() {
		List<Player> players = currentTournament.getPlayers();
		// sort list as win rates change
		Utils.sortPlayersByWinRate(players);
		return players;
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
