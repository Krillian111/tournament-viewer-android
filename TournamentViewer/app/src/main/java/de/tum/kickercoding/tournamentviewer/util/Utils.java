package de.tum.kickercoding.tournamentviewer.util;


import android.app.Dialog;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import de.tum.kickercoding.tournamentviewer.entities.Game;
import de.tum.kickercoding.tournamentviewer.entities.Player;

public class Utils {

	public static String prepareWinRateForView(double winrate) {
		DecimalFormat df = new DecimalFormat("#0%", new DecimalFormatSymbols(Locale.US));
		return df.format(winrate);
	}

	public static void prepareTextView(View view, int id, String text) {
		TextView textView = (TextView) view.findViewById(id);
		textView.setText(text);
	}

	public static void prepareTextView(Dialog dialog, int id, String text) {
		TextView textView = (TextView) dialog.findViewById(id);
		textView.setText(text);
	}

	public static void sortPlayersByWinRate(List<Player> list) {
		Collections.sort(list, new Comparator<Player>() {
			public int compare(Player p1, Player p2) {
				return Double.compare(p2.getWinRateInTournament(), p1.getWinRateInTournament());
			}
		});
	}

	public static void sortPlayersForMatching(List<Player> list) {
		Collections.sort(list, new Comparator<Player>() {
			public int compare(Player p1, Player p2) {
				return Double.compare(p2.getElo(), p1.getElo());
			}
		});
	}

	/**
	 * Update Elo rating of all players based on a games outcome and the current ratings
	 *
	 * @param game
	 */
	public static void updateElo(Game game) {
		List<Player> team1 = game.getTeam1();
		List<Player> team2 = game.getTeam2();
		boolean oneOnOne = team1.size() == 1;
		List<Double> expectedScores = calculateExpectedScore(team1, team2, oneOnOne);
		double expectedScoresTeam1 = expectedScores.get(0);
		double expectedScoresTeam2 = expectedScores.get(1);
		if (game.getScoreTeam1() == game.getScoreTeam2()) {
			updateElo(team1, expectedScoresTeam1, 0.5);
			updateElo(team2, expectedScoresTeam2, 0.5);
		} else if (game.getScoreTeam1() > game.getScoreTeam2()) {
			updateElo(team1, expectedScoresTeam1, 1.0);
			updateElo(team2, expectedScoresTeam2, 0.0);
		} else {
			updateElo(team2, expectedScoresTeam2, 1.0);
			updateElo(team1, expectedScoresTeam1, 0.0);
		}
	}

	// using formula from wikipedia
	private static List<Double> calculateExpectedScore(List<Player> team1, List<Player> team2, boolean oneOnOne) {
		Double avgEloTeam1 = team1.get(0).getElo();
		Double avgEloTeam2 = team2.get(0).getElo();
		if (!oneOnOne) {
			avgEloTeam1 = (team1.get(0).getElo() + team1.get(1).getElo()) / 2;
			avgEloTeam2 = (team2.get(0).getElo() + team2.get(1).getElo()) / 2;
		}
		Double avgExpectedScoreTeam1 = 1 / (1 + Math.pow(10, ((avgEloTeam2 - avgEloTeam1) / Constants
				.FACTOR_TEN_THRESHOLD)));
		Double avgExpectedScoreTeam2 = 1 / (1 + Math.pow(10, ((avgEloTeam1 - avgEloTeam2) / Constants
				.FACTOR_TEN_THRESHOLD)));
		return new ArrayList<>(Arrays.asList(avgExpectedScoreTeam1, avgExpectedScoreTeam2));
	}


	private static void updateElo(List<Player> team, double expectedScore, double actualScore) {
		for (Player player : team) {
			int kFactor = calculateKFactor(player);
			double updatedElo = calculateUpdatedElo(player.getElo(), kFactor, actualScore, expectedScore);
			player.setEloChangeFromLastGame(updatedElo - player.getElo());
			player.setElo(updatedElo);
		}
	}

	private static int calculateKFactor(Player player) {
		int kFactor = Constants.K_FACTOR_NEW;
		if (player.getPlayedGames() > Constants.K_FACTOR_GAME_THRESHOLD) {
			kFactor = Constants.K_FACTOR_ESTABLISHED;
		}
		return kFactor;
	}

	// using formula from wikipedia
	private static double calculateUpdatedElo(double oldRating, int kFactor, double actualScore, double
			expectedScore) {
		return oldRating + kFactor * (actualScore - expectedScore);
	}


}
