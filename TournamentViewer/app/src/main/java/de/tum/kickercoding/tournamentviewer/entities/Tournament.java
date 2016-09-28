package de.tum.kickercoding.tournamentviewer.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

// TODO: write unit tests
public class Tournament implements Parcelable {

	private List<Player> players = new ArrayList<>();

	private List<Game> games = new ArrayList<>();

	private int maxScore;

	private int numberOfGames;

	private boolean oneOnOne = false;

	public Tournament() {
	}

	public void addGame(Game game) throws IllegalArgumentException {
		if (game.isOneOnOne() == isOneOnOne()) {
			games.add(game);
		} else {
			throw new IllegalArgumentException(String.format("Game (1on1:%b) and Tournament (1on1:%b) differ in 1on1" +
					" setting", game.isOneOnOne(), isOneOnOne()));
		}
	}

	public boolean removeLastGame() {
		if (games.size() != 0) {
			games.remove(games.size() - 1);
			return true;
		} else {
			return false;
		}
	}

	public List<Game> getGames() {
		return games;
	}

	public Game getGame(int position) {
		return games.get(position);
	}

	public void addPlayer(Player player) {
		players.add(player);
	}

	public boolean removePlayer(Player player) {
		return players.remove(player);
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}

	public int getMaxScore() {
		return maxScore;
	}

	public void setNumberOfGames(int numberOfGames) {
		this.numberOfGames = numberOfGames;
	}

	public int getNumberOfGames() {
		return numberOfGames;
	}

	public void setOneOnOne(boolean oneOnOne) {
		this.oneOnOne = oneOnOne;

	}

	public boolean isOneOnOne() {
		return oneOnOne;
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
		dest.writeTypedList(players);
		dest.writeTypedList(games);
		dest.writeInt(maxScore);
		dest.writeInt(numberOfGames);
		// use int to represent boolean (no writeBoolean method available)
		dest.writeInt(oneOnOne ? 1 : 0);
	}

	// Creator
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Tournament createFromParcel(Parcel in) {
			return new Tournament(in);
		}

		public Tournament[] newArray(int size) {
			return new Tournament[size];
		}
	};

	private Tournament(Parcel in) {
		in.readTypedList(players, Player.CREATOR);
		in.readTypedList(games, Game.CREATOR);
		maxScore = in.readInt();
		numberOfGames = in.readInt();
		oneOnOne = in.readInt() != 0;
	}
}
