package de.tum.kickercoding.tournamentviewer.tournament.monsterdyp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import de.tum.kickercoding.tournamentviewer.entities.Game;
import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.tournament.Matchmaking;
import de.tum.kickercoding.tournamentviewer.util.Constants;
import de.tum.kickercoding.tournamentviewer.util.Utils;

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
		List<Player> playersToMatch = selectPlayers(players, oneOnOne, pastGames, singleGame);
		Utils.sortPlayersForMatching(playersToMatch);
		int gamesToGenerate = 1;
		if (!singleGame) {
			gamesToGenerate = oneOnOne ? players.size() / 2 : players.size() / 4;
		}

		List<Game> generatedGames = new ArrayList<>();
		for (int i = 0;i < gamesToGenerate;i++) {
			generatedGames.add(generateRandomGame(playersToMatch, oneOnOne, pastGames));
		}
		return generatedGames;
	}

	private List<Player> selectPlayers(List<Player> players, boolean oneOnOne, List<Game> pastGames, boolean
			singleGame) {
		// copy list to not change original list
		List<Player> playersUpForSelection = new ArrayList<>(players);
		List<Player> playersSelected = new ArrayList<>();
		int playersToSelect;
		int size = playersUpForSelection.size();
		if (singleGame) {
			playersToSelect = (oneOnOne) ? 2 : 4;
		} else {
			playersToSelect = (oneOnOne) ?
					size - (size % 2) : size - (size % 4);
		}

		setGeneratedGames(playersUpForSelection, pastGames);

		Utils.sortPlayersByGamesGeneratedTournament(playersUpForSelection);
		while (playersToSelect > 0) {
			// get players with least games
			int playedGamesMin = playersUpForSelection.get(0).getGeneratedGamesInTournament();
			List<Player> playersWithMinGames = new ArrayList<>();
			for (Player player : playersUpForSelection) {
				if (player.getGeneratedGamesInTournament() == playedGamesMin) {
					playersWithMinGames.add(player);
				}
			}
			// add players (randomize if more available than needed)
			if (playersWithMinGames.size() <= playersToSelect) {
				playersSelected.addAll(playersWithMinGames);
				playersUpForSelection.removeAll(playersWithMinGames);
				playersToSelect -= playersWithMinGames.size();
			} else {
				Random random = new Random();
				for (int i = 0;i < playersToSelect;i++) {
					int playerPosition = random.nextInt(playersWithMinGames.size());
					playersSelected.add(playersWithMinGames.remove(playerPosition));
				}
				break;
			}
		}
		return playersSelected;
	}

	private void setGeneratedGames(final List<Player> players, List<Game> pastGames) {
		// efficiency of loop was sacrificed for readability (list sizes low -> shouldn't cause performance issues)
		for (Player player : players) {
			player.setGeneratedGamesInTournament(0);
			for (Game game : pastGames) {
				if (game.getTeam1().contains(player) || game.getTeam2().contains(player)) {
					player.setGeneratedGamesInTournament(player.getGeneratedGamesInTournament() + 1);
				}
			}
		}
	}

	private Game generateRandomGame(List<Player> players, boolean oneOnOne, List<Game> pastGames) {
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
				List<Player> team = generateTeam(players, pastGames);
				players.removeAll(team);
				playersForGame.addAll(team);
			}
		}
		return new Game(playersForGame);
	}

	List<Player> generateTeam(List<Player> players, List<Game> pastGames) {
		int[][] pastGameFrequencies = calculatePartnerFrequencies(players, pastGames);
		int playersSize = players.size();
		// select random player
		Random random = new Random();
		int playerPosition = random.nextInt(playersSize);
		Player playerToMatch = players.get(playerPosition);
		// generate parameters for gaussian distribution to draw partner
		// std was determined by some basic sampling test; not set in stone
		double std = playersSize * Constants.GAUSSIAN_STD_IN_PERCENTAGE_OF_PLAYERS;
		double avg = playersSize - playerPosition;
		Player partner = null;
		int[] partnerDrawCount = new int[playersSize];
		while (partner == null) {
			int partnerPosition = (int) (random.nextGaussian() * std + avg);
			if (partnerPosition < 0 || partnerPosition >= playersSize || partnerPosition == playerPosition) {
				// resample until valid value occurs
				continue;
			}
			// if already matched before and not yet drawn this round, skip SAME_TEAM_SKIP_THRESHOLD times
			// (adjustable if
			// results not satisfactory)
			if (pastGameFrequencies[playerPosition][partnerPosition] > 0
					&& partnerDrawCount[partnerPosition] < Constants.SAME_TEAM_SKIP_THRESHOLD) {
				partnerDrawCount[partnerPosition]++;
				continue;
			}
			partner = players.get(partnerPosition);
		}
		return new ArrayList<>(Arrays.asList(playerToMatch, partner));
	}

	private int[][] calculatePartnerFrequencies(List<Player> players, List<Game> pastGames) {
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
			if (positionP1 != -1 && positionP2 != -1) {
				frequencies[positionP1][positionP2]++;
				frequencies[positionP2][positionP1]++;
			}
			if (positionP3 != -1 && positionP4 != -1) {
				frequencies[positionP3][positionP4]++;
				frequencies[positionP4][positionP3]++;
			}
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


}
