package de.tum.kickercoding.tournamentviewer.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;


public class Player implements Parcelable {

	private static final String DECIMAL_PATTERN = "#0.00###";

	// IMPORTANT: adjust writeToParcel when adding new fields
	private String name;
	private int playedGames;
	private int wonGames;
	private int lostGames;
	private int tiedGames;
	private double rankingScore;

	public Player(String name) {
		this.name = name;
		playedGames = 0;
		wonGames = 0;
		lostGames = 0;
		tiedGames = 0;
		rankingScore = 0.0;
	}

	/**
	 * utitliy constructor, used for unittest and copying objects (to prevent references to lists
	 * of singletons)
	 *
	 * @param name
	 * @param playedGames
	 * @param wonGames
	 * @param lostGames
	 * @param tiedGames
	 * @param rankingScore
	 */
	public Player(String name, int playedGames, int wonGames, int lostGames, int tiedGames, double rankingScore) {
		this.name = name;
		this.playedGames = playedGames;
		this.wonGames = wonGames;
		this.lostGames = lostGames;
		this.tiedGames = tiedGames;
		this.rankingScore = rankingScore;
	}

	public String getName() {
		return name;
	}

	public int getPlayedGames() {
		return playedGames;
	}

	public int getWonGames() {
		return wonGames;
	}

	public int getLostGames() {
		return lostGames;
	}

	public int getTiedGames() {
		return tiedGames;
	}

	public double getRankingScore() {
		return rankingScore;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPlayedGames(int playedGames) {
		this.playedGames = playedGames;
	}

	public void setWonGames(int wonGames) {
		this.wonGames = wonGames;
	}

	public void setLostGames(int lostGames) {
		this.lostGames = lostGames;
	}

	public void setTiedGames(int tiedGames) {
		this.tiedGames = tiedGames;
	}

	/**
	 * rounds to 4 digits after comma before calling setter
	 *
	 * @param rankingScore
	 */
	public void setRankingScore(double rankingScore) {
		this.rankingScore = roundDouble(rankingScore);
	}


	@Override
	public boolean equals(Object o) {
		if (o instanceof Player) {
			return ((Player) o).getName().equals(this.getName());
		}
		return false;
	}

	private double roundDouble(double toRound) {
		return Double.parseDouble(parseDouble(toRound));
	}

	private String parseDouble(double toParse) {
		DecimalFormat df = new DecimalFormat(DECIMAL_PATTERN, new DecimalFormatSymbols(Locale.US));
		return df.format(toParse);
	}

	public Player copy() {
		return new Player(name, playedGames, wonGames, lostGames, tiedGames, rankingScore);
	}

	/**********************************
	 * Parcelable interface methods
	 *********************************/

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(playedGames);
		dest.writeInt(wonGames);
		dest.writeInt(lostGames);
		dest.writeInt(tiedGames);
		dest.writeDouble(rankingScore);
	}

	// Creator
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Player createFromParcel(Parcel in) {
			return new Player(in);
		}

		public Player[] newArray(int size) {
			return new Player[size];
		}
	};

	private Player(Parcel in) {
		name = in.readString();
		playedGames = in.readInt();
		wonGames = in.readInt();
		lostGames = in.readInt();
		tiedGames = in.readInt();
		rankingScore = in.readDouble();
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
