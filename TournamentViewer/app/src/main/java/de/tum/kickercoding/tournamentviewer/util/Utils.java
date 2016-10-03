package de.tum.kickercoding.tournamentviewer.util;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Utils {

	public static String prepareWinRateForView(double winrate) {
		DecimalFormat df = new DecimalFormat("#0%", new DecimalFormatSymbols(Locale.US));
		return df.format(winrate);
	}

}
