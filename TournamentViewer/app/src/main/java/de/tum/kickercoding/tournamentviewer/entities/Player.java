package de.tum.kickercoding.tournamentviewer.entities;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import de.tum.kickercoding.tournamentviewer.Constants;


// TODO: add comments to methods/class
public class Player {

    private static final String DECIMAL_PATTERN = "#0.00###";

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
     * @param rankingScore
     */
    public void setRankingScore(double rankingScore) {
        this.rankingScore = roundDouble(rankingScore);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Player) {
            return ((Player) o).getName().equals(this.getName());
        }
        return false;
    }

    /**
     * toString() representation of player: 'name|playedGames|wonGames|lostGames|tiedGames|rankingScore'
     */
    @Override
    public String toString() {
        return String.format("%s~%d~%d~%d~%d~%s", name, playedGames, wonGames, lostGames, tiedGames, parseDouble(rankingScore));
    }

    public static Player fromString(String player){
        String[] playerFields = player.split(Constants.DELIMITER);
        if(playerFields.length != 6){
            for(int i=0;i<playerFields.length;i++){
                System.out.println(playerFields[i]);
            }
            throw new IllegalArgumentException("String representing player object must contain exactly 5 tildes \"~\"");
        }
        Player playerFromString =  new Player(playerFields[0]);
        playerFromString.setPlayedGames(Integer.parseInt(playerFields[1]));
        playerFromString.setWonGames(Integer.parseInt(playerFields[2]));
        playerFromString.setLostGames(Integer.parseInt(playerFields[3]));
        playerFromString.setTiedGames(Integer.parseInt(playerFields[4]));
        playerFromString.setRankingScore(Double.parseDouble(playerFields[5]));
        return playerFromString;
    }

    private double roundDouble(double toRound) {
        return Double.parseDouble(parseDouble(toRound));
    }

    private String parseDouble(double toParse) {
        DecimalFormat df = new DecimalFormat(DECIMAL_PATTERN, new DecimalFormatSymbols(Locale.US));
        return df.format(toParse);
    }

}
