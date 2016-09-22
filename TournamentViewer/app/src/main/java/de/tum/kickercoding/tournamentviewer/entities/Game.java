package de.tum.kickercoding.tournamentviewer.entities;

import java.util.ArrayList;
import java.util.List;

// TODO: add comments to methods/class
public class Game {

    private boolean finished;
    private boolean oneOnOne;
    private int scoreTeam1;
    private int scoreTeam2;
    // 1on1: 1st vs 2nd participant, 2on2: 1st/2nd vs 3rd/4th participant
    private List<Player> participants; // TODO: "make sure" that only the PlayerManager can make changes to the player objects

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

    public List<Player> getTeam1() {
        if (isOneOnOne()) {
            return participants.subList(0, 1);
        } else {
            return participants.subList(0, 2);
        }
    }

    public List<String> getTeam1PlayerNames() {
        List<String> playerNames = new ArrayList<>();
        if (isOneOnOne()) {
            playerNames.add(participants.get(0).getName());
        } else {
            playerNames.add(participants.get(0).getName());
            playerNames.add(participants.get(1).getName());
        }
        return playerNames;
    }

    public int getScoreTeam1() {
        return scoreTeam1;
    }

    public List<Player> getTeam2() {
        if (isOneOnOne()) {
            return participants.subList(1, 2);
        } else {
            return participants.subList(2, 4);
        }
    }

    public List<String> getTeam2PlayerNames() {
        List<String> playerNames = new ArrayList<>();
        if (isOneOnOne()) {
            playerNames.add(participants.get(1).getName());
        } else {
            playerNames.add(participants.get(2).getName());
            playerNames.add(participants.get(3).getName());
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


	// TODO: implement toString
	@Override
	public String toString() {
		if(isOneOnOne()){
			return String.format("1_%s_%s_%d_%d_%b",getTeam1PlayerNames()[0],getTeam2PlayerNames()[0],getScoreTeam1(),getScoreTeam2(),isFinished());
		} else {
			return String.format("2_%s_%s_%s_%s_%d_%d_%b",getTeam1PlayerNames()[0],getTeam1PlayerNames()[1],getTeam2PlayerNames()[0],getTeam2PlayerNames()[1],getScoreTeam1(),getScoreTeam2(),isFinished());
		}
	}
}
