package de.tum.kickercoding.tournamentviewer.exceptions;

public class TournamentManagerException extends Exception {

	public TournamentManagerException() {
	}

	public TournamentManagerException(String message) {
		super(message);
	}

	public TournamentManagerException(String message, Throwable cause) {
		super(message, cause);
	}
}
