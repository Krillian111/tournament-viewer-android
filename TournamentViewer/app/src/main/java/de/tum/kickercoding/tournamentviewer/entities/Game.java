package de.tum.kickercoding.tournamentviewer.entities;

import java.util.List;

// TODO: add comments to methods/class
public class Game {

    private boolean finished;
    private boolean oneOnOne;
    private int scoreTeam1;
    private int scoreTeam2;
    private List<Player> participants;

    public Game(List<Player> participants) {
        this.participants = participants;
        this.scoreTeam1 = 0;
        this.scoreTeam2 = 0;
        this.finished = false;
        int numberOfParticipants = participants.size();

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

    public int getScoreTeam1() {
        return scoreTeam1;
    }

    // TODO: validate score in GameManager (0 <= score <= max_score)
    public void setScoreTeam1(int scoreTeam1) {
        this.scoreTeam1 = scoreTeam1;
    }

    // TODO: validate score in GameManager (0 <= score <= max_score)
    public int getScoreTeam2() {
        return scoreTeam2;
    }

    public void setScoreTeam2(int scoreTeam2) {
        this.scoreTeam2 = scoreTeam2;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isOneOnOne() {
        return oneOnOne;
    }
}
