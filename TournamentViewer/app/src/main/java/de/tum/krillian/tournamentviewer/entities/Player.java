package de.tum.krillian.tournamentviewer.entities;

public class Player {

    private int id;
    private String name;
    private int playedGames;
    private int wonGames;
    private int lostGames;
    private int tiedGames;
    private double rankingScore;

    public Player(int id, String name){
        this.id = id;
        this.name = name;
        playedGames = 0;
        wonGames = 0;
        lostGames = 0;
        tiedGames = 0;
        rankingScore = 0.0;
    }

    public int getId(){
        return id;
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

    @Override
    public boolean equals(Object o) {
        if(o instanceof Player) {
            return ((Player) o).getName().equals(this.getName());
        }
        return false;
    }
}
