package de.tum.kickercoding.tournamentviewer.util;


import android.app.Dialog;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

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

	//TODO: implement ranking by mmr
	public static void sortPlayersForMatching(List<Player> list) {
		Collections.sort(list, new Comparator<Player>() {
			public int compare(Player p1, Player p2) {
				return Double.compare(p2.getWinRateInTournament(), p1.getWinRateInTournament());
			}
		});
	}

}
