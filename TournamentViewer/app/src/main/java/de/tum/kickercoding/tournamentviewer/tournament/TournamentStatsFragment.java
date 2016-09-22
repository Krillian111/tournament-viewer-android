package de.tum.kickercoding.tournamentviewer.tournament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import de.tum.kickercoding.tournamentviewer.R;

public class TournamentStatsFragment extends Fragment {

	public TournamentStatsFragment() {
	}

	public static TournamentStatsFragment newInstance() {
		return new TournamentStatsFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_tournament_stats, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ListView tournamentStats = (ListView) view.findViewById(R.id.list_view_tournament_stats);
		tournamentStats.setAdapter(new TournamentStatsAdapter(getActivity()));
	}
}
