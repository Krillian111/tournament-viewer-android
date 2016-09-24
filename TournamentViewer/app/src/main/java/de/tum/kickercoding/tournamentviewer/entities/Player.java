package de.tum.kickercoding.tournamentviewer.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import de.tum.kickercoding.tournamentviewer.util.Constants;


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

    // Players with identical name need to return true; if change needed all utility methods based
    // on name need to be changed as well!
    //TODO: IMPLEMENT METHOD COMPARETO FOR CONSISTENCY
    @Override
    public boolean equals(Object o) {
        if (o instanceof Player) {
            return ((Player) o).getName().equals(this.getName());
        }
        return false;
    }

    /**
     * toString() representation of player: 'name~playedGames~wonGames~lostGames~tiedGames~rankingScore'
     */
    @Override
    public String toString() {
        return String.format("%s_%d_%d_%d_%d_%s", name, playedGames, wonGames, lostGames, tiedGames, parseDouble(rankingScore));
    }

    public static Player fromString(String player) {
        String[] playerFields = player.split(Constants.DELIMITER);
        if (playerFields.length != 6) {
            for (int i = 0; i < playerFields.length; i++) {
                System.out.println(playerFields[i]);
            }
            throw new IllegalArgumentException("String representing player object must contain exactly 5 tildes \"~\"");
        }
        Player playerFromString = new Player(playerFields[0],
                Integer.parseInt(playerFields[1]),
                Integer.parseInt(playerFields[2]),
                Integer.parseInt(playerFields[3]),
                Integer.parseInt(playerFields[4]),
                Double.parseDouble(playerFields[5]));
        return playerFromString;
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
}
