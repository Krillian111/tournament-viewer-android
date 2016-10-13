package de.tum.kickercoding.tournamentviewer.tournament;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.tum.kickercoding.tournamentviewer.R;

public class GameDetailFragment extends Fragment {

	public GameDetailFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_game_detail, container, false);
	}
}
