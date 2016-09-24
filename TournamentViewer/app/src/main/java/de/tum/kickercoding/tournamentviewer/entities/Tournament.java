package de.tum.kickercoding.tournamentviewer.entities;

import java.util.ArrayList;
import java.util.List;

// TODO: write unit tests
public class Tournament {

	private List<Player> players = new ArrayList<>();

	private List<Game> games = new ArrayList<>();

	private int maxScore;

	private int numberOfGames;

	public Tournament() {
	}

	public void addGame(Game game) {
		games.add(game);
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
}
