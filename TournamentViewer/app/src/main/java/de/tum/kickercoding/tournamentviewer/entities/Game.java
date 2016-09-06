package de.tum.kickercoding.tournamentviewer.entities;

import java.util.ArrayList;
import java.util.List;

// TODO: add comments to methods/class
public class Game {

    private boolean finished;
    private boolean oneOnOne;
    private int scoreTeam1;
    private int scoreTeam2;
    // 1on1: 0 vs 1; 2on2: 0/1 vs 2/3
    private Player[] participants; // TODO: "make sure" that only the PlayerManager can make changes to the player objects

    public Game(Player[] participants) {
        this.participants = participants;
        this.scoreTeam1 = 0;
        this.scoreTeam2 = 0;
        this.finished = false;
        int numberOfParticipants = participants.length;

        if(numberOfParticipants == 2) {
            // game type: one on one
            // team 1: participants[0]
            // team 2: participants[1]
            oneOnOne = true;
        } else if(numberOfParticipants == 4) {
            // game type: two on two
            // team 1: participants[0] and participants[1]
            // team 2: participants[2] and participants[3]
            oneOnOne = false;
        } else {
            throw new IllegalArgumentException(String.format("Invalid number of players for a game: %s", numberOfParticipants));
        }
    }

    public Player[] getTeam1() {
        if (isOneOnOne()) {
            return new Player[]{participants[0]};
        } else {
            return new Player[]{participants[0],participants[1]};
        }
    }

    public String[] getTeam1PlayerNames() {
        String[] playerNames;
        if (isOneOnOne()) {
            playerNames = new String[]{getTeam1()[0].getName()};
        } else {
            Player[] team1 = getTeam1();
            playerNames = new String[]{team1[0].getName(),team1[1].getName()};
        }
        return playerNames;
    }

    public int getScoreTeam1() {
        return scoreTeam1;
    }

    public Player[] getTeam2() {
        if (isOneOnOne()) {
            return new Player[]{participants[1]};
        } else {
            return new Player[]{participants[2],participants[3]};
        }
    }

    public String[] getTeam2PlayerNames() {
        String[] playerNames;
        if (isOneOnOne()) {
            playerNames = new String[]{getTeam2()[0].getName()};
        } else {
            Player[] team2 = getTeam2();
            playerNames = new String[]{team2[0].getName(),team2[1].getName()};
        }
        return playerNames;
    }

    // TODO: validate score in GameManager (0 <= score <= max_score)
    public int getScoreTeam2() {
        return scoreTeam2;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isOneOnOne() {
        return oneOnOne;
    }

    // TODO: validate score in GameManager (0 <= score <= max_score)
    public void setScoreTeam1(int scoreTeam1) {
        this.scoreTeam1 = scoreTeam1;
    }

    public void setScoreTeam2(int scoreTeam2) {
        this.scoreTeam2 = scoreTeam2;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
