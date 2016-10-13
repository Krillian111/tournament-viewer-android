package de.tum.kickercoding.tournamentviewer.tournament.monsterdyp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import de.tum.kickercoding.tournamentviewer.entities.Game;
import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.tournament.Matchmaking;
import de.tum.kickercoding.tournamentviewer.util.Utils;

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
	public Game generateGame(List<Player> players, boolean oneOnOne, List<Game> pastGames) {
		return generateGames(players, oneOnOne, pastGames, true).get(0);
	}

	@Override
	public List<Game> generateRound(List<Player> players, boolean oneOnOne, List<Game> pastGames) {
		return generateGames(players, oneOnOne, pastGames, false);
	}

	private List<Game> generateGames(List<Player> players, boolean oneOnOne, List<Game> pastGames, boolean
			singleGame) {
		List<Player> playersToMatch = selectPlayers(players, oneOnOne, singleGame);
		int[][] partnerFrequencies = calculatePartnerFrequencies(players, oneOnOne, pastGames);
		int gamesToGenerate = 1;
		if (!singleGame) {
			gamesToGenerate = oneOnOne ? players.size() / 2 : players.size() / 4;
		}

		List<Game> generatedGames = new ArrayList<>();
		for (int i = 0;i < gamesToGenerate;i++) {
			generatedGames.add(generateRandomGame(playersToMatch, oneOnOne, partnerFrequencies));
		}
		return generatedGames;
	}

	private int[][] calculatePartnerFrequencies(List<Player> players, boolean oneOnOne, List<Game> pastGames) {
		if (oneOnOne) {
			return new int[0][0];
		}
		int playerSize = players.size();
		int[][] frequencies = new int[playerSize][playerSize];
		for (Game game : pastGames) {
			Player p1 = game.getTeam1().get(0);
			Player p2 = game.getTeam1().get(1);
			Player p3 = game.getTeam2().get(0);
			Player p4 = game.getTeam2().get(1);
			int positionP1 = players.indexOf(p1);
			int positionP2 = players.indexOf(p2);
			int positionP3 = players.indexOf(p3);
			int positionP4 = players.indexOf(p4);
			frequencies[positionP1][positionP2]++;
			frequencies[positionP2][positionP1]++;
			frequencies[positionP3][positionP4]++;
			frequencies[positionP4][positionP3]++;
		}
		// make sure every row has a 0 entry by shifting it down by its minimum
		for (int i = 0;i < playerSize;i++) {
			int min = Integer.MAX_VALUE;
			// get row minimum
			for (int j = 0;j < playerSize;j++) {
				if (i != j && frequencies[i][j] < min) {
					min = frequencies[i][j];
				}
			}
			// adjust row s.th. at least one value == 0
			if (min > 0) {
				for (int j = 0;j < playerSize;j++) {
					if (i != j) {
						frequencies[i][j] -= min;
					}
				}
			}
		}
		return frequencies;
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
		Utils.sortPlayersForMatching(selectedPlayers);
		return selectedPlayers;
	}

	private Game generateRandomGame(List<Player> players, boolean oneOnOne, int[][] pastGameFrequencies) {
		List<Player> playersForGame = new ArrayList<>();
		if (oneOnOne) {
			// TODO: build more sophisticated 1v1 matching
			Random random = new Random();
			for (int i = 0;i < 2;i++) {
				int playerPosition = random.nextInt(players.size());
				playersForGame.add(players.remove(playerPosition));
			}
		} else {
			for (int i = 0;i < 2;i++) {
				List<Player> team = generateTeam(players, pastGameFrequencies);
				players.removeAll(team);
				playersForGame.addAll(team);
			}
		}
		return new Game(playersForGame);
	}

	private List<Player> generateTeam(List<Player> players, int[][] pastGameFrequencies) {
		int playersSize = players.size();
		// select random player
		Random random = new Random();
		int playerPosition = random.nextInt(playersSize);
		Player playerToMatch = players.get(playerPosition);
		// generate parameters for gaussian distribution to draw partner
		// std was determined by some basic sampling test; not set in stone
		double std = playersSize * 0.3;
		double avg = playersSize - playerPosition;
		Player partner = null;
		int[] partnerDrawCount = new int[playersSize];
		while (partner == null) {
			int partnerPosition = (int) (random.nextGaussian() * std + avg);
			if (partnerPosition < 0 || partnerPosition >= playersSize || partnerPosition == playerPosition) {
				// resample until valid value occurs
				continue;
			}
			// if already matched before and not yet drawn this round, skip SAME_TEAM_THRESHOLD times (adjustable if
			// results not satisfactory)
			int SAME_TEAM_THRESHOLD = 3;
			if (pastGameFrequencies[playerPosition][partnerPosition] > 0
					&& partnerDrawCount[partnerPosition] < SAME_TEAM_THRESHOLD) {
				partnerDrawCount[partnerPosition]++;
				continue;
			}
			partner = players.get(partnerPosition);
		}
		return new ArrayList<>(Arrays.asList(playerToMatch, partner));
	}


}
