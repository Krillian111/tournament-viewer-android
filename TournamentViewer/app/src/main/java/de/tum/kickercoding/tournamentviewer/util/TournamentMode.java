package de.tum.kickercoding.tournamentviewer.util;

public enum TournamentMode {
	MONSTERDYP("MONSTERDYP"),
	DYP("DYP"),
	FIXED("FIXED");

	String name;

	private TournamentMode(String name){
		this.name = name;
	}

	public String getName(){
		return this.name;
	}
}