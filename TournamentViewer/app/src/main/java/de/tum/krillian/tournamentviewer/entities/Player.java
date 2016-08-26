package de.tum.krillian.tournamentviewer.entities;

import de.tum.krillian.tournamentviewer.Constants;

public class Player {

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
    public void setRankingScore(double rankingScore){
        double roundedScore = Math.round(rankingScore*10000)/10000d;
        this.rankingScore = roundedScore;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Player) {
            return ((Player) o).getName().equals(this.getName());
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s|%d|%d|%d|%d|%f", name, playedGames, wonGames, lostGames, tiedGames, rankingScore);
    }

    public static Player fromString(String player){
        String[] playerFields = player.split(Constants.DELIMITER);
        if(playerFields.length != 6){
            throw new IllegalArgumentException("String representing player object must contain exactly 5 pipes \"|\"");
        }
        Player playerFromString =  new Player(playerFields[0]);
        playerFromString.setPlayedGames(Integer.parseInt(playerFields[1]));
        playerFromString.setWonGames(Integer.parseInt(playerFields[2]));
        playerFromString.setLostGames(Integer.parseInt(playerFields[3]));
        playerFromString.setTiedGames(Integer.parseInt(playerFields[4]));
        playerFromString.setRankingScore(Double.parseDouble(playerFields[5]));
        return playerFromString;
    }
}
