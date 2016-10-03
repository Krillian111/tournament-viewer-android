package de.tum.kickercoding.tournamentviewer.entities;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;


public class Player implements Comparable<Player> {

	private static final String DECIMAL_PATTERN = "#0.00###";

	private String name;
	private int wonGames;
	private int lostGames;
	private int tiedGames;
	private int wonGamesInTournament;
	private int lostGamesInTournament;
	private int tiedGamesInTournament;
	private double mmr;

	public Player(String name) {
		this.name = name;
		wonGames = 0;
		lostGames = 0;
		tiedGames = 0;
		wonGamesInTournament = 0;
		lostGamesInTournament = 0;
		tiedGamesInTournament = 0;
		mmr = 0.0;

	}

	/**
	 * utitliy constructor, used for unittest and copying objects (to prevent references to lists
	 * of singletons)
	 *
	 * @param name
	 * @param wonGames
	 * @param lostGames
	 * @param tiedGames
	 * @param wonGamesInTournament
	 * @param lostGamesInTournament
	 * @param tiedGamesInTournament
	 * @param mmr
	 */
	public Player(String name, int wonGames, int lostGames, int tiedGames, int wonGamesInTournament, int
			lostGamesInTournament, int tiedGamesInTournament,
				  double mmr) {
		this.name = name;
		this.wonGames = wonGames;
		this.lostGames = lostGames;
		this.tiedGames = tiedGames;
		this.wonGamesInTournament = wonGamesInTournament;
		this.lostGamesInTournament = lostGamesInTournament;
		this.tiedGamesInTournament = tiedGamesInTournament;
		this.mmr = mmr;

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

	/**
	 * rounds to 4 digits after comma before calling setter
	 *
	 * @param mmr
	 */
	public void setMmr(double mmr) {
		this.mmr = roundDouble(mmr);
	}

	public double getMmr() {

		return mmr;
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
				wonGamesInTournament, lostGamesInTournament, tiedGamesInTournament, mmr);
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
