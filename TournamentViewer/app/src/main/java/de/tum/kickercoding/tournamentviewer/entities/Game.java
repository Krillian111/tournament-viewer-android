package de.tum.kickercoding.tournamentviewer.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

// TODO: add comments to methods/class
public class Game implements Parcelable {

	/**
	 * IF FIELDS ARE ADDED/REMOVED: UPDATE PARCELABLE INTERFACE METHODS
	 */

	// TODO: "make sure" that only the PlayerManager can make changes to the player objects
	// 1on1: 1st vs 2nd participant, 2on2: 1st/2nd vs 3rd/4th participant
	private List<Player> participants;
	private int scoreTeam1;
	private int scoreTeam2;
	private boolean finished;
	private boolean oneOnOne;
	private boolean resultCommitted;


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

	public boolean isResultCommitted() {
		return resultCommitted;
	}

	public void setResultCommitted(boolean resultCommitted) {
		this.resultCommitted = resultCommitted;
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
		dest.writeTypedList(participants);
		dest.writeInt(scoreTeam1);
		dest.writeInt(scoreTeam2);
		// use int to represent boolean (no writeBoolean method available)
		dest.writeInt(finished ? 1 : 0);
		dest.writeInt(oneOnOne ? 1 : 0);
		dest.writeInt(resultCommitted ? 1 : 0);
	}

	// Creator
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Game createFromParcel(Parcel in) {
			return new Game(in);
		}

		public Game[] newArray(int size) {
			return new Game[size];
		}
	};

	private Game(Parcel in) {
		in.readTypedList(participants, Player.CREATOR);
		scoreTeam1 = in.readInt();
		scoreTeam2 = in.readInt();
		finished = in.readInt() != 0;
		oneOnOne = in.readInt() != 0;
		resultCommitted = in.readInt() != 0;
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
