package de.tum.kickercoding.tournamentviewer.util;

public class Constants {

	/**
	 * GENERAL SETTINGS
	 * - contains general settings
	 */

	public static final String FILE_GENERAL_SETTINGS = "de.tum.kickercoding.tournamentviewer.general.settings";

	public static final String VAR_NUMBER_OF_GAMES = "numberOfGames";

	public static final Integer DEFAULT_NUMBER_OF_GAMES = 1;

	public static final String VAR_MAX_SCORE = "maxScore";

	public static final Integer DEFAULT_MAX_SCORE = 7;

	/**
	 * GLOBAL PLAYER LIST
	 * - key: player name
	 * - value: json of player
	 */

	public static final String FILE_GLOBAL_PLAYERS_LIST = "de.tum.kickercoding.tournamentviewer.player.list";

	public static final String DELIMITER = "_";

	public static final String ERROR_DETECTED_SAVE_YOUR_RESULTS = "Error detected, do not proceed, save your results!";

	/**
	 * TOURNAMENT DATA
	 */

	public static final String FILE_TOURNAMENT_DATA = "de.tum.kickercoding.tournamentviewer.tournament.data";

	public static final String VAR_CURRENT_TOURNAMENT = "currentTournament";

}
