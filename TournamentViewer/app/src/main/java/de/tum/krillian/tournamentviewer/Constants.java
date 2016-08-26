package de.tum.krillian.tournamentviewer;

public class Constants {

    /**
     *
     * GENERAL SETTINGS
     * - contains general settings
     * - changes are permanently saved
     */

    public static final String FILE_GENERAL_SETTINGS = "de.tum.krillian.tournamentviewer.general.settings";

    public static final String VAR_NUMBER_OF_GAMES = "numberOfGames";

    public static final Integer DEFAULT_NUMBER_OF_GAMES = 1;

    public static final String VAR_MAX_SCORE = "maxScore";

    public static final Integer DEFAULT_MAX_SCORE = 7;

    public static final String VAR_GLOBAL_PLAYER_NAME_SET = "globalPlayerIdSet";

    /**
     *
     * GLOBAL 0PLAYER LIST
     * - key: player name
     * - value: toString() of player with key as name
     *
     */

    public static final String FILE_GLOBAL_PLAYERS_LIST = "de.tum.krillian.tournamentviewer.player.list";

    public static final String DELIMITER = "|";

}
