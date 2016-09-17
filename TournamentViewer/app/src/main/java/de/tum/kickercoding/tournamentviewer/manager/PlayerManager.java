package de.tum.kickercoding.tournamentviewer.manager;

import java.util.ArrayList;
import java.util.List;

import de.tum.kickercoding.tournamentviewer.Constants;
import de.tum.kickercoding.tournamentviewer.entities.Game;
import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.exceptions.PlayerManagerException;
import de.tum.kickercoding.tournamentviewer.exceptions.PreferenceFileException;


// TODO: add comments to methods/class
class PlayerManager {

    private static PlayerManager instance = new PlayerManager();
    private PreferenceFileManager preferenceFileManager = null;
    private List<Player> players = new ArrayList<Player>();

    private PlayerManager() {}

    /**
     * Get the instance of the singleton.
     * @return The instance of the singleton.
     */
    static PlayerManager getInstance() {
        return instance;
    }

    /**
     * Initialization (should be called after instantiation)
     * @throws PreferenceFileException
     */
    void initialize() throws PreferenceFileException {
        preferenceFileManager = PreferenceFileManager.getInstance();
        players = preferenceFileManager.getPlayerList();
    }

    /**
     * Add a {@link Player} to player list.
     * @param name The name of the {@link Player} to add.
     * @throws PlayerManagerException If there are symbols that are not allowed in the name of the {@link Player}.
     */
    void addPlayer(String name) throws PlayerManagerException {
        if (name.contains(Constants.DELIMITER)) {
            throw new PlayerManagerException(String.format("Invalid character: %s (pipe symbol)", Constants.DELIMITER));
        }

        Player newPlayer = new Player(name);
        if(players.contains(newPlayer)){
            throw new PlayerManagerException(String.format("Player %s already exists", name));
        }
        players.add(newPlayer);
    }

    /**
     * Remove {@link Player} from player list.
     * @param name The name of the {@link Player} to remove.
     * @throws PlayerManagerException
     */
    void removePlayer(String name) throws PlayerManagerException {
        for(Player p : players) {
            if(p.getName().equals(name)) {
                players.remove(p);
                return;
            }
        }
        throw new PlayerManagerException("Failed to remove player");
    }

    /**
     * Get the number of currently registered players.
     * @return Number of currently registered players.
     */
    int getNumberOfPlayers() {
        return players.size();
    }



    /**
     * Commit results of a {@link Game} to the player list.
     * @param game The {@link Game} that should be committed.
     */
    void commitGameResult(Game game) throws PlayerManagerException {
        if (!game.isFinished()) {
            throw new PlayerManagerException("Cannot commit unfinished game");
        }

        // TODO: calculate ranking score (change) for all players
        int scoreTeam1 = game.getScoreTeam1();
        String[] team1 = game.getTeam1PlayerNames();
        int scoreTeam2 = game.getScoreTeam2();
        String[] team2 = game.getTeam2PlayerNames();
        if (scoreTeam1 == scoreTeam2) {
            // add a tied and a played game to both teams (played = implicitly)
            addTiedGame(team1);
            addTiedGame(team2);
            // TODO: update ranking score of all players
        } else if (scoreTeam1 > scoreTeam2) {
            // add a won game to team 1, a lost game to team 2 and a played game to both teams (played = implicitly)
            addWonGame(team1);
            addLostGame(team2);
            // TODO: update ranking score of all players
        } else {
            // add a won game to team 2, a lost game to team 1 and a played game to both teams (played = implicitly)
            addWonGame(team2);
            addLostGame(team1);
            // TODO: update ranking score of all players
        }
    }

    /**
     * Add a played and a tied game to players.
     * @param playersToUpdate The players that should be updated.
     */
    private void addTiedGame(String[] playersToUpdate) throws PlayerManagerException {
        Player playerToUpdate;
        for (String playerName : playersToUpdate) {
            playerToUpdate = getPlayerByName(playerName);
            playerToUpdate.setPlayedGames(playerToUpdate.getPlayedGames() + 1);
            playerToUpdate.setTiedGames(playerToUpdate.getTiedGames() + 1);
        }
    }

    /**
     * Add a played and a won game to players.
     * @param playersToUpdate The players that should be updated.
     */
    private void addWonGame(String[] playersToUpdate) throws PlayerManagerException {
        Player playerToUpdate;
        for (String playerName : playersToUpdate) {
            playerToUpdate = getPlayerByName(playerName);
            playerToUpdate.setPlayedGames(playerToUpdate.getPlayedGames() + 1);
            playerToUpdate.setWonGames(playerToUpdate.getWonGames() + 1);
        }
    }

    /**
     * Add a played and a lost game to players.
     * @param playersToUpdate The players that should be updated.
     */
    private void addLostGame(String[] playersToUpdate) throws PlayerManagerException {
        Player playerToUpdate;
        for (String playerName : playersToUpdate) {
            playerToUpdate = getPlayerByName(playerName);
            playerToUpdate.setPlayedGames(playerToUpdate.getPlayedGames() + 1);
            playerToUpdate.setLostGames(playerToUpdate.getLostGames() + 1);
        }
    }

    /**
     * Get the currently registered players.
     * @return The currently registered players.
     */
    List<Player> getPlayers(){
        return players;
    }

    /**
     * commits the current player list with all its changes to the preference file
     */
    void commitPlayerList (){
        // TODO: implement
    }

    /**
     * Get the {@link Player} for a specific name.
     * @param name The name of the {@link Player} you want to get.
     * @return The {@link Player} with the given name.
     * @throws PlayerManagerException If there is no {@link Player} with the given name.
     */
    Player getPlayerByName(String name) throws PlayerManagerException {
        for (Player player : players) {
            if (player.getName() == name) {
                return player;
            }
        }
        throw new PlayerManagerException("No player found for the requested name");
    }

    Player getPlayerByPosition(int position) throws PlayerManagerException {
        try {
            return players.get(position);
        } catch (IndexOutOfBoundsException e) {
            throw new PlayerManagerException(String.format("No player at position %d", position));
        }
    }
}
