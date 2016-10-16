package de.tum.kickercoding.tournamentviewer.tournament;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import de.tum.kickercoding.tournamentviewer.R;
import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;

import static de.tum.kickercoding.tournamentviewer.util.Listeners.OnGameChangeListener;
import static de.tum.kickercoding.tournamentviewer.util.Listeners.OnPlayoffGeneratedListener;

public class TournamentGamesFragment extends Fragment implements OnPlayoffGeneratedListener {

	OnGameChangeListener onGameChangeListener;

	private TournamentGamesAdapter adapter;

	public TournamentGamesFragment() {
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			onGameChangeListener = (OnGameChangeListener) getActivity();
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString()
					+ " must implement OnGameChangeListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_tournament_games, container, false);
	}

	@Override
	public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ListView tournamentGames = (ListView) view.findViewById(R.id.list_view_tournament_games);
		adapter = new TournamentGamesAdapter(getActivity(), onGameChangeListener);
		tournamentGames.setAdapter(adapter);

		Button addGameButton = (Button) view.findViewById(R.id.button_add_game_to_tournament);
		addGameButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View buttonView) {
				try {
					AppManager.getInstance().generateGame();
					notifyAdapter();
				} catch (AppManagerException e) {
					AppManager.getInstance().displayMessage(getActivity(), e.getMessage());
				}
			}
		});

		Button addRoundButton = (Button) view.findViewById(R.id.button_add_round_to_tournament);
		addRoundButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View buttonView) {
				try {
					AppManager.getInstance().generateRound();
					notifyAdapter();
				} catch (AppManagerException e) {
					AppManager.getInstance().displayMessage(getActivity(), e.getMessage());
				}
			}
		});
	}

	private void notifyAdapter() {
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onPlayoffGenerated() {
		notifyAdapter();
	}
}
