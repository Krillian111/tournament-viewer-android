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

	/*
	 * TOURNAMENT DATA
	 */

	public static final String FILE_TOURNAMENT_DATA = "de.tum.kickercoding.tournamentviewer.tournament.data";

	public static final String VAR_CURRENT_TOURNAMENT = "currentTournament";

	/*
	 * Constants for Matchmaking
	 */

	/**
	 * Determines how often the matchmaking algorithm retries to find a team when a team is drawn which already
	 * played in the current tournament
	 */
	public static final int SAME_TEAM_SKIP_THRESHOLD = 10;

	public static final double GAUSSIAN_STD_IN_PERCENTAGE_OF_PLAYERS = 0.3;

	/*
	 * Constants for Elo computation
	 */
	/**
	 * K factor for players with less than K_FACTOR_GAME_THRESHOLD games
	 */
	public static final int K_FACTOR_NEW = 40;

	/**
	 * K factor for players with more than K_FACTOR_GAME_THRESHOLD games
	 */
	public static final int K_FACTOR_ESTABLISHED = 15;

	/**
	 * after this amount of games rating changes slower by using a low k-factor
	 */
	public static final int K_FACTOR_GAME_THRESHOLD = 30;

	/**
	 * every FACTOR_TEN_THRESHOLD amount of rating expected score is ten times higher than opponent's
	 */
	public static final int FACTOR_TEN_THRESHOLD = 400;

	/**
	 * Default value for elo rating
	 */
	public static final double ELO_DEFAULT = 1500;
}
