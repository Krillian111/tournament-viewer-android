package de.tum.kickercoding.tournamentviewer.util;

/**
 * Utility class which holds interfaces necessary for communication. Class was introduced due to circular dependency
 * warnings.
 */
public class Listeners {

	public interface OnGameChangeListener {
		void onGameChanged();
	}

	public interface OnPlayoffGeneratedListener {
		void onPlayoffGenerated();
	}
}
