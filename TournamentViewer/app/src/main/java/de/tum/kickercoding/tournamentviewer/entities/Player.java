package de.tum.kickercoding.tournamentviewer.entities;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import de.tum.kickercoding.tournamentviewer.util.Constants;


public class Player implements Comparable<Player> {

	private static final String DECIMAL_PATTERN = "#0.00###";

	private String name;
	private int wonGames;
	private int lostGames;
	private int tiedGames;
	private int wonGamesInTournament;
	private int lostGamesInTournament;
	private int tiedGamesInTournament;
	private int goalsShot;
	private int goalsReceived;
	private int goalsShotInTournament;
	private int goalsReceivedInTournament;
	private double elo;
	// allows to adjust elo when reverting a game (only one game reset in a row)
	private double eloChangeFromLastGame;

	public Player(String name) {
		this.name = name;
		wonGames = 0;
		lostGames = 0;
		tiedGames = 0;
		wonGamesInTournament = 0;
		lostGamesInTournament = 0;
		tiedGamesInTournament = 0;
		goalsShot = 0;
		goalsShotInTournament = 0;
		goalsReceived = 0;
		goalsReceivedInTournament = 0;
		elo = Constants.ELO_DEFAULT;
		eloChangeFromLastGame = 0.0;

	}

	/**
	 * utitliy constructor, used for unit test and copying objects
	 * (to prevent object references to player list of PlayerManager)
	 */
	public Player(String name, int wonGames, int lostGames, int tiedGames, int wonGamesInTournament, int
			lostGamesInTournament, int tiedGamesInTournament, int goalsShot, int goalsShotInTournament,
				  int goalsReceived, int goalsReceivedInTournament, double elo, double eloChangeFromLastGame) {
		this.name = name;
		this.wonGames = wonGames;
		this.lostGames = lostGames;
		this.tiedGames = tiedGames;
		this.wonGamesInTournament = wonGamesInTournament;
		this.lostGamesInTournament = lostGamesInTournament;
		this.tiedGamesInTournament = tiedGamesInTournament;
		this.goalsShot = goalsShot;
		this.goalsShotInTournament = goalsShotInTournament;
		this.goalsReceived = goalsReceived;
		this.goalsReceivedInTournament = goalsReceivedInTournament;
		this.elo = elo;
		this.eloChangeFromLastGame = eloChangeFromLastGame;

	}

	public String getName() {
		return name;
	}

	public void setWonGames(int wonGames) {
		this.wonGames = wonGames;
	}

	public int getWonGames() {
		return wonGames;
	}

	public void setLostGames(int lostGames) {
		this.lostGames = lostGames;
	}

	public int getLostGames() {
		return lostGames;
	}

	public void setTiedGames(int tiedGames) {
		this.tiedGames = tiedGames;
	}

	public int getTiedGames() {
		return tiedGames;
	}

	public int getWonGamesInTournament() {
		return wonGamesInTournament;
	}

	public void setWonGamesInTournament(int wonGamesInTournament) {
		this.wonGamesInTournament = wonGamesInTournament;
	}

	public int getLostGamesInTournament() {
		return lostGamesInTournament;
	}

	public void setLostGamesInTournament(int lostGamesInTournament) {
		this.lostGamesInTournament = lostGamesInTournament;
	}

	public int getTiedGamesInTournament() {
		return tiedGamesInTournament;
	}

	public void setTiedGamesInTournament(int tiedGamesInTournament) {
		this.tiedGamesInTournament = tiedGamesInTournament;
	}

	public int getGoalsShot() {
		return goalsShot;
	}

	public void setGoalsShot(int goalsShot) {
		this.goalsShot = goalsShot;
	}

	public int getGoalsReceived() {
		return goalsReceived;
	}

	public void setGoalsReceived(int goalsReceived) {
		this.goalsReceived = goalsReceived;
	}

	public int getGoalsShotInTournament() {
		return goalsShotInTournament;
	}

	public void setGoalsShotInTournament(int goalsShotInTournament) {
		this.goalsShotInTournament = goalsShotInTournament;
	}

	public int getGoalsReceivedInTournament() {
		return goalsReceivedInTournament;
	}

	public void setGoalsReceivedInTournament(int goalsReceivedInTournament) {
		this.goalsReceivedInTournament = goalsReceivedInTournament;
	}

	public int getGoalDifference() {
		return getGoalsShot() - getGoalsReceived();
	}

	public int getGoalDifferenceInTournament() {
		return getGoalsShotInTournament() - getGoalsReceivedInTournament();
	}

	/**
	 * rounds to 4 digits after comma before calling setter
	 *
	 * @param elo
	 */
	public void setElo(double elo) {
		this.elo = roundDouble(elo);
	}

	public double getElo() {
		return elo;
	}

	public double getEloChangeFromLastGame() {
		return eloChangeFromLastGame;
	}

	public void setEloChangeFromLastGame(double eloChangeFromLastGame) {
		this.eloChangeFromLastGame = eloChangeFromLastGame;
	}

	public int getPlayedGames() {
		return wonGames + lostGames + tiedGames;
	}

	public int getPlayedGamesInTournament() {
		return wonGamesInTournament + lostGamesInTournament + tiedGamesInTournament;
	}

	public double getWinRate() {
		if (getPlayedGames() != 0) {
			return roundDouble(wonGames / (double) getPlayedGames());
		} else {
			return 0;
		}
	}

	public double getWinRateInTournament() {
		if (getPlayedGamesInTournament() != 0) {
			return roundDouble(wonGamesInTournament / (double) getPlayedGamesInTournament());
		} else {
			return 0;
		}
	}

	private double roundDouble(double toRound) {
		return Double.parseDouble(parseDouble(toRound));
	}

	private String parseDouble(double toParse) {
		DecimalFormat df = new DecimalFormat(DECIMAL_PATTERN, new DecimalFormatSymbols(Locale.US));
		return df.format(toParse);
	}

	public Player copy() {
		return new Player(name, wonGames, lostGames, tiedGames,
				wonGamesInTournament, lostGamesInTournament, tiedGamesInTournament, goalsShot, goalsShotInTournament,
				goalsReceived, goalsReceivedInTournament, elo, eloChangeFromLastGame);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Player) {
			return ((Player) o).getName().equals(this.getName());
		}
		return false;
	}

	@Override
	public int compareTo(Player otherPlayer) {
		return getName().compareTo(otherPlayer.getName());
	}

	/****************************
	 * hand written (de)serialization using json
	 * reason: "interface methods" of serializable would need to be wrapped into stream
	 * parsing methods to generate the actual serialization
	 *****************************/
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public static Player fromJson(String playerAsJson) {
		Gson gson = new Gson();
		return gson.fromJson(playerAsJson, Player.class);
	}
}
