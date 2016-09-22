package de.tum.kickercoding.tournamentviewer.manager;

import android.util.Log;

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

	private boolean oneOnOne = false;

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

	List<Game> startNewRound() {
		return null;
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
		if (oneOnOne) {
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
		if (oneOnOne) {
			game = matchmaking.generateGame1on1(getPlayers());
		} else {
			game = matchmaking.generateGame2on2(getPlayers());
		}
		addGame(game);
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

	void addGame(Game game) {
		currentTournament.addGame(game);
	}

	boolean removeLastGame() {
		return currentTournament.removeLastGame();
	}

	List<Game> getGames() {
		return currentTournament.getGames();
	}

	boolean isOneOnOne() {
		return oneOnOne;
	}

	void setOneOnOne(boolean oneOnOne) {
		this.oneOnOne = oneOnOne;
	}
}
