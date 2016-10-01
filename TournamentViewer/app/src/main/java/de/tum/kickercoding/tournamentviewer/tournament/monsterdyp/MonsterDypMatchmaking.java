package de.tum.kickercoding.tournamentviewer.tournament.monsterdyp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import de.tum.kickercoding.tournamentviewer.entities.Game;
import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.tournament.Matchmaking;

// TODO: incorporate ranking
public class MonsterDypMatchmaking implements Matchmaking {


	private static MonsterDypMatchmaking instance = new MonsterDypMatchmaking();

	private MonsterDypMatchmaking() {
	}

	/**
	 * Get the instance of the singleton.
	 *
	 * @return The instance of the singleton.
	 */
	public static MonsterDypMatchmaking getInstance() {
		return instance;
	}


	@Override
	public Game generateGame2on2(List<Player> players) {
		List<Player> playersToMatch = selectPlayers(players, false, true);
		return generateRandomGame(playersToMatch, false);
	}

	@Override
	public List<Game> generateRound2on2(List<Player> players) {
		List<Player> playersToMatch = selectPlayers(players, false, false);
		int gamesToGenerate = players.size() / 4;

		List<Game> generatedGames = new ArrayList<>();
		for (int i = 0;i < gamesToGenerate;i++) {
			generatedGames.add(generateRandomGame(playersToMatch, false));
		}
		return generatedGames;
	}

	private List<Player> selectPlayers(List<Player> players, boolean oneOnOne, boolean singleGame) {
		List<Player> selectedPlayers = new ArrayList<>();
		int playersToDelete;
		if (singleGame) {
			playersToDelete = (oneOnOne) ? players.size() - 2 : players.size() - 4;
		} else {
			playersToDelete = (oneOnOne) ? players.size() % 2 : players.size() % 4;
		}
		// copy list to not change original list
		for (Player player : players) {
			selectedPlayers.add(player.copy());
		}
		if (playersToDelete != 0) {
			// sort by played games
			Collections.sort(selectedPlayers, new Comparator<Player>() {
				public int compare(Player p1, Player p2) {
					if (p1.getPlayedGames() == p2.getPlayedGames()) {
						return 0;
					}
					return p1.getPlayedGames() < p2.getPlayedGames() ? -1 : 1;
				}
			});
			// TODO: incorporate randomization regarding which players do get deleted (players
			// TODO: with equal amount of games are treated differently depending on where they are
			// TODO: in the list once sorting is done)
			// temporarily: simply remove excessive players
			for (int i = 0;i < playersToDelete;i++) {
				selectedPlayers.remove(selectedPlayers.size() - 1);
			}
		}
		return selectedPlayers;
	}

	private Game generateRandomGame(List<Player> players, boolean oneOnOne) {
		int playerToSelect = oneOnOne ? 2 : 4;
		Random random = new Random();
		List<Player> playersForGame = new ArrayList<>();
		for (int i = 0;i < playerToSelect;i++) {
			int playerPosition = random.nextInt(players.size());
			playersForGame.add(players.remove(playerPosition));
		}
		return new Game(playersForGame);
	}


	/**********************************
	 * 1 on 1 Matchmaking
	 *********************************/

	// TODO: implement
	@Override
	public Game generateGame1on1(List<Player> players) {
		return null;
	}

	// TODO: implement
	@Override
	public List<Game> generateRound1on1(List<Player> players) {
		return null;
	}
}
