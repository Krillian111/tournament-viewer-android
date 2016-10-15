package de.tum.kickercoding.tournamentviewer.tournament;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import de.tum.kickercoding.tournamentviewer.R;
import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;
import de.tum.kickercoding.tournamentviewer.util.Utils;

import static de.tum.kickercoding.tournamentviewer.util.Utils.createPlayerDialog;
import static de.tum.kickercoding.tournamentviewer.util.Utils.prepareTextView;

public class TournamentStatsAdapter extends BaseAdapter implements ListAdapter {

	public TournamentStatsAdapter() {
	}

	@Override
	public int getCount() {
		return AppManager.getInstance().getPlayersForTournament().size();
	}

	@Override
	public Object getItem(int pos) {
		return AppManager.getInstance().getPlayersForTournament().get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final Context context = parent.getContext();
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
					.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.item_tournament_stats, null);
		}

		Player player = (Player) getItem(position);
		prepareTextView(view, R.id.tournament_stats_item_rank, "" + (position + 1));
		prepareTextView(view, R.id.tournament_stats_item_name, player.getName());
		prepareTextView(view, R.id.tournament_stats_item_games_played, "" + player.getPlayedGamesInTournament());
		prepareTextView(view, R.id.tournament_stats_item_games_won, "" + player.getWonGamesInTournament());
		prepareTextView(view, R.id.tournament_stats_item_win_rate, Utils.prepareWinRateForView(player
				.getWinRateInTournament()));
		prepareTextView(view, R.id.tournament_stats_item_goal_difference, "" + player.getGoalDifferenceInTournament());

		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View viewItem) {
				Dialog dialog = createPlayerDialog(context, (Player) getItem(position));
				dialog.show();
			}
		});
		return view;
	}
}