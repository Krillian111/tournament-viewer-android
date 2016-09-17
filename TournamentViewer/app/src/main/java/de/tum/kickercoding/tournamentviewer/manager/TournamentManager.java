package de.tum.kickercoding.tournamentviewer.manager;

import java.util.List;

import de.tum.kickercoding.tournamentviewer.entities.Game;
import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.entities.Tournament;
import de.tum.kickercoding.tournamentviewer.exceptions.PreferenceFileException;
import de.tum.kickercoding.tournamentviewer.exceptions.TournamentManagerException;

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

    void removePlayer(Player player){
        currentTournament.removePlayer(player);
    }

    void addGame(Game game){
        currentTournament.addGame(game);
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

    void toggleParticipation(Player player) {
        if (currentTournament.getPlayers().contains(player)){
            removePlayer(player);
        } else {
            addPlayer(player);
        }
    }
}
