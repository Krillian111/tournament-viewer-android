package de.tum.kickercoding.tournamentviewer.manager;

import java.util.List;

import de.tum.kickercoding.tournamentviewer.entities.Game;
import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.entities.Tournament;
import de.tum.kickercoding.tournamentviewer.exceptions.PreferenceFileException;
import de.tum.kickercoding.tournamentviewer.exceptions.TournamentManagerException;

// TODO: write unit tests
class TournamentManager {

    static TournamentManager instance = new TournamentManager();

    static TournamentManager getInstance() {
        return instance;
    }

    private TournamentManager() {
    }

    private Tournament currentTournament;

    void initialize(){
        this.currentTournament = new Tournament();
    }

    void addPlayer(Player player){
        currentTournament.addPlayer(player);
    }

    boolean removePlayer(Player player){
        return currentTournament.removePlayer(player);
    }

    boolean removePlayer(String name){
        // use fake player to force removal; players with identical name are considered equal
        return removePlayer(new Player(name));
    }

    List<Player> getPlayers() {
        return currentTournament.getPlayers();
    }

    void addGame(Game game){
        currentTournament.addGame(game);
    }

    boolean removeLastGame(){
        return currentTournament.removeLastGame();
    }

    List<Game> getGames() {
        return currentTournament.getGames();
    }


    void startNewTournament() throws TournamentManagerException {
        try {
            int maxScore = PreferenceFileManager.getInstance().loadMaxScore();
            int numberOfGames = PreferenceFileManager.getInstance().loadNumberOfGames();
            currentTournament.setMaxScore(maxScore);
            currentTournament.setNumberOfGames(numberOfGames);
        } catch (PreferenceFileException e){
            throw new TournamentManagerException("Failed to start new tournament, wrapped Exception:"+e.toString());
        }
    }

    List<Game> startNewRound(){
        return null;
    }

    boolean toggleParticipation(Player player) {
        if (currentTournament.getPlayers().contains(player)){
            removePlayer(player);
            return false;
        } else {
            addPlayer(player);
            return true;
        }
    }

    boolean isSignedUp(String playerName) {
        return currentTournament.getPlayers().contains(new Player(playerName));
    }
}
