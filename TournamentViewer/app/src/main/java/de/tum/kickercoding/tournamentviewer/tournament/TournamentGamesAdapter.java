package de.tum.kickercoding.tournamentviewer.tournament;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import de.tum.kickercoding.tournamentviewer.R;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;

public class TournamentGamesAdapter extends BaseAdapter implements ListAdapter {

	Context context;

	public TournamentGamesAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return AppManager.getInstance().getGamesForTournament().size();
	}

	@Override
	public Object getItem(int pos) {
		return AppManager.getInstance().getGamesForTournament().get(pos);
	}

	// TODO: handle game id
	@Override
	public long getItemId(int pos) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.item_tournament_stats, null);
		}

		//Handle TextView and display player name
		TextView listItemText = (TextView) view.findViewById(R.id.tournament_stats_item_text_view);
		listItemText.setText(getItem(position).toString());

		return view;
	}
}