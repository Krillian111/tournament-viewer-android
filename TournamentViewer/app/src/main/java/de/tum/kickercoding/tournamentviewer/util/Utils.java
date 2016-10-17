package de.tum.kickercoding.tournamentviewer.util;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import de.tum.kickercoding.tournamentviewer.R;
import de.tum.kickercoding.tournamentviewer.entities.Game;
import de.tum.kickercoding.tournamentviewer.entities.Player;

public class Utils {

	public static String prepareWinRateForView(double winrate) {
		DecimalFormat df = new DecimalFormat("#0%", new DecimalFormatSymbols(Locale.US));
		return df.format(winrate);
	}

	public static String prepareEloForView(double elo) {
		int eloForDisplay = (int) Math.round(elo);
		return "" + eloForDisplay;
	}

	public static void prepareTextView(View view, int id, String text) {
		TextView textView = (TextView) view.findViewById(id);
		textView.setText(text);
	}

	public static void prepareTextView(Dialog dialog, int id, String text) {
		TextView textView = (TextView) dialog.findViewById(id);
		textView.setText(text);
	}

	/**
	 * Sorts the player list by multiple criteria. Additional criteria are only taken into account if previous
	 * criteria yields equality. <br>
	 * 1. by win rate (in tournament) <br>
	 * 2. by goal difference (in tournament) <br>
	 * 3. by elo (reversed, lower elo is higher)
	 */
	public static void sortPlayersForTournamentStats(List<Player> list) {
		Collections.sort(list, new Comparator<Player>() {
			public int compare(Player p1, Player p2) {
				int compared = Double.compare(p2.getWinRateInTournament(), p1.getWinRateInTournament());
				if (compared == 0) {
					// Integer.compare is only  API 19+
					compared = p2.getGoalDifferenceInTournament() - p1.getGoalDifferenceInTournament();
				}
				// lower elo is higher in rating when stats are equal
				if (compared == 0) {
					compared = Double.compare(p1.getElo(), p2.getElo());
				}
				return compared;
			}
		});
	}

	public static void sortPlayersByGamesGeneratedTournament(List<Player> list) {
		Collections.sort(list, new Comparator<Player>() {
			public int compare(Player p1, Player p2) {
				if (p1.getGeneratedGamesInTournament() == p2.getGeneratedGamesInTournament()) {
					return 0;
				}
				return p1.getGeneratedGamesInTournament() < p2.getGeneratedGamesInTournament() ? -1 : 1;
			}
		});
	}

	public static void sortPlayersForMatching(List<Player> list) {
		sortPlayersByElo(list);
	}

	public static void sortPlayersByElo(final List<Player> list) {
		Collections.sort(list, new Comparator<Player>() {
			public int compare(Player p1, Player p2) {
				return Double.compare(p2.getElo(), p1.getElo());
			}
		});
	}

	public static void sortPlayersByName(final List<Player> playerList) {
		Collections.sort(playerList);
	}

	/**
	 * Update Elo rating of all players based on a games outcome and the current ratings
	 *
	 * @param game
	 * @return List of players with updated elo (used as a simple container as the caller needs to commit the actual
	 * changes)
	 */
	public static List<Player> calculateEloAfterGame(Game game) {
		List<Player> team1 = game.getTeam1();
		List<Player> team2 = game.getTeam2();
		boolean oneOnOne = team1.size() == 1;
		List<Double> expectedScores = calculateExpectedScore(team1, team2, oneOnOne);
		double expectedScoresTeam1 = expectedScores.get(0);
		double expectedScoresTeam2 = expectedScores.get(1);
		List<Player> updatedTeam1;
		List<Player> updatedTeam2;
		if (game.getScoreTeam1() == game.getScoreTeam2()) {
			updatedTeam1 = updateElo(team1, expectedScoresTeam1, 0.5);
			updatedTeam2 = updateElo(team2, expectedScoresTeam2, 0.5);
		} else if (game.getScoreTeam1() > game.getScoreTeam2()) {
			updatedTeam1 = updateElo(team1, expectedScoresTeam1, 1.0);
			updatedTeam2 = updateElo(team2, expectedScoresTeam2, 0.0);
		} else {
			updatedTeam2 = updateElo(team2, expectedScoresTeam2, 1.0);
			updatedTeam1 = updateElo(team1, expectedScoresTeam1, 0.0);
		}
		List<Player> players = new ArrayList<>(updatedTeam1);
		players.addAll(updatedTeam2);
		return players;
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


	private static List<Player> updateElo(List<Player> team, double expectedScore, double actualScore) {
		for (Player player : team) {
			int kFactor = calculateKFactor(player);
			double updatedElo = calculateElo(player.getElo(), kFactor, actualScore, expectedScore);
			player.setEloChangeFromLastGame(updatedElo - player.getElo());
			player.setElo(updatedElo);
		}
		return team;
	}

	private static int calculateKFactor(Player player) {
		int kFactor = Constants.K_FACTOR_NEW;
		if (player.getPlayedGames() > Constants.K_FACTOR_GAME_THRESHOLD) {
			kFactor = Constants.K_FACTOR_ESTABLISHED;
		}
		return kFactor;
	}

	// using formula from wikipedia
	private static double calculateElo(double oldRating, int kFactor, double actualScore, double
			expectedScore) {
		return oldRating + kFactor * (actualScore - expectedScore);
	}


	public static Dialog createPlayerDialog(Context context, Player player) {
		final Dialog dialog = new Dialog(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
				.LAYOUT_INFLATER_SERVICE);
		dialog.setContentView(inflater.inflate(R.layout.dialog_player_details, null));
		dialog.setTitle(R.string.title_player_details);

		prepareTextView(dialog, R.id.player_details_name, player.getName());
		prepareTextView(dialog, R.id.player_details_played_games, "" + player.getPlayedGames());
		prepareTextView(dialog, R.id.player_details_won_games, "" + player.getWonGames());
		prepareTextView(dialog, R.id.player_details_lost_games, "" + player.getLostGames());
		prepareTextView(dialog, R.id.player_details_tied_games, "" + player.getTiedGames());
		prepareTextView(dialog, R.id.player_details_win_rate, Utils.prepareWinRateForView(player.getWinRate()));
		prepareTextView(dialog, R.id.player_details_goal_difference, "" + player.getGoalDifference());
		prepareTextView(dialog, R.id.player_details_elo, "" + Utils.prepareEloForView(player.getElo()));

		setupButtonListener(dialog);
		return dialog;
	}

	private static void setupButtonListener(final Dialog dialog) {
		Button backButton = (Button) dialog.findViewById(R.id.button_player_details_back);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
}
