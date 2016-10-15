package de.tum.kickercoding.tournamentviewer.entities;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import de.tum.kickercoding.tournamentviewer.util.TournamentMode;

public class Tournament {

	private List<Player> players = new ArrayList<>();

	private List<Game> games = new ArrayList<>();

	private int maxScore;

	private int numberOfGames;

	private boolean oneOnOne = false;

	private boolean finished = false;

	private TournamentMode mode;

	public Tournament() {
	}

	public void addGame(Game game) throws IllegalArgumentException {
		// all players need to be registered for the tournament
		List<Player> playersForGame = new ArrayList<>();
		playersForGame.addAll(game.getTeam1());
		playersForGame.addAll(game.getTeam2());

		for (Player p : playersForGame) {
			if (!players.contains(p)) {
				throw new IllegalArgumentException(String.format("Game contains player %s who is not registered for " +
						"the tournament", p.getName()));
			}
		}
		// check correct game type
		if (game.isOneOnOne() == isOneOnOne()) {
			games.add(game);
		} else {
			throw new IllegalArgumentException(String.format("Game (1on1:%b) and Tournament (1on1:%b) differ in 1on1" +
					" setting", game.isOneOnOne(), isOneOnOne()));
		}
	}

	public boolean removeLastGame() {
		if (games.size() != 0) {
			games.remove(games.size() - 1);
			return true;
		} else {
			return false;
		}
	}

	public List<Game> getGames() {
		return games;
	}

	public Game getGame(int position) {
		return games.get(position);
	}

	public void addPlayer(Player player) {
		players.add(player);
	}

	public boolean removePlayer(Player player) {
		return players.remove(player);
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}

	public int getMaxScore() {
		return maxScore;
	}

	public void setNumberOfGames(int numberOfGames) {
		this.numberOfGames = numberOfGames;
	}

	public int getNumberOfGames() {
		return numberOfGames;
	}

	public void setOneOnOne(boolean oneOnOne) {
		this.oneOnOne = oneOnOne;
	}

	public boolean isOneOnOne() {
		return oneOnOne;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public TournamentMode getMode() {
		return mode;
	}

	public void setMode(TournamentMode mode) {
		this.mode = mode;
	}


	/****************************
	 * hand written (de)serialization using json
	 * reason: as "interface methods" of serializable would need to be wrapped into stream
	 * parsing methods to generate the actual serialization
	 *****************************/
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public static Tournament fromJson(String gameAsJson) {
		Gson gson = new Gson();
		return gson.fromJson(gameAsJson, Tournament.class);
	}

}
