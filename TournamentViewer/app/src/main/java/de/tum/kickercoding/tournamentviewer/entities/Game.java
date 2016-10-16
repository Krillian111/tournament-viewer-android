package de.tum.kickercoding.tournamentviewer.entities;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Game {

	// 1on1: 1st vs 2nd participant, 2on2: 1st/2nd vs 3rd/4th participant
	private List<Player> participants;
	private int scoreTeam1;
	private int scoreTeam2;
	private boolean finished;
	private boolean oneOnOne;
	private boolean resultCommitted;


	/**
	 * Creates a new game, mode (1v1 or 2v2) is inferred from the amount of players
	 *
	 * @param participants
	 */
	public Game(List<Player> participants) {
		this.participants = participants;
		this.scoreTeam1 = 0;
		this.scoreTeam2 = 0;
		this.finished = false;
		this.resultCommitted = false;
		int numberOfParticipants = participants.size();

		if (numberOfParticipants == 2) {
			// game type: one on one
			// team 1: participants[0]
			// team 2: participants[1]
			oneOnOne = true;
		} else if (numberOfParticipants == 4) {
			// game type: two on two
			// team 1: participants[0] and participants[1]
			// team 2: participants[2] and participants[3]
			oneOnOne = false;
		} else {
			throw new IllegalArgumentException(String.format("Invalid number of players for a game: %s",
					numberOfParticipants));
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

	public int getScoreTeam2() {
		return scoreTeam2;
	}

	public boolean isFinished() {
		return finished;
	}

	public boolean isOneOnOne() {
		return oneOnOne;
	}

	public void setScoreTeam1(int scoreTeam1) {
		this.scoreTeam1 = scoreTeam1;
	}

	public void setScoreTeam2(int scoreTeam2) {
		this.scoreTeam2 = scoreTeam2;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public boolean isResultCommitted() {
		return resultCommitted;
	}

	public void setResultCommitted(boolean resultCommitted) {
		this.resultCommitted = resultCommitted;
	}

	/****************************
	 * hand written (de)serialization using json
	 * reason: as "interface methods" of serializable would need to be wrapped into stream
	 * parsing methods to generate the actual serialization
	 *****************************/
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public static Game fromJson(String gameAsJson) {
		Gson gson = new Gson();
		return gson.fromJson(gameAsJson, Game.class);
	}

}
