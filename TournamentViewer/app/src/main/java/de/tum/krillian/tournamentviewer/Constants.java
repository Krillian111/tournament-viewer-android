package de.tum.krillian.tournamentviewer;

public class Constants {

    /**
     *
     * GENERAL SETTINGS
     * - contains general settings
     * - changes are permanently saved
     */

    public static final String FILE_GAME_SETTINGS = "de.tum.krillian.tournamentviewer.settings";

    public static final String VAR_NUMBER_OF_GAMES = "numberOfGames";

    public static final String VAR_MAX_SCORE = "maxScore";

    public static final Integer DEFAULT_MAX_SCORE = 7;

    public static final Integer DEFAULT_NUMBER_OF_GAMES = 1;

    /**
     *
     * GLOBAL PLAYER LIST
     *
     * - MAY ONLY consist of entries in the form of player(key@String)/elo(value@Integer)
     * - elo values are updated after each tournament
     */

    public static final String FILE_GLOBAL_PLAYER_LIST = "de.tum.krillian.tournamentviewer.players";

    public static final String VAR_PLAYER_SET = "playerSet";

    /**
     *
     * CURRENT TOURNAMENT
     * - contains a copy of the currently used players in the same form as the global list
     * - is copied from the global list when players are selected
     * - gets synced with the global list after the tournament (new elo values)
     */

    public static final String FILE_TOURNAMENT = "de.tum.krillian.tournamentviewer.tournament";
}
