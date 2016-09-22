package de.tum.kickercoding.tournamentviewer.tournament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import de.tum.kickercoding.tournamentviewer.R;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;

public class TournamentGamesFragment extends Fragment {

	public TournamentGamesFragment() {
	}

	public static TournamentGamesFragment newInstance() {
		return new TournamentGamesFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_tournament_games, container, false);
	}

	//TODO: CREATE OVERLAY: http://stackoverflow.com/questions/12400920/open-a-custom-dialog-when-custom-listview-item-edittext-is-clicked
	@Override
	public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ListView tournamentGames = (ListView) view.findViewById(R.id.list_view_tournament_games);
		tournamentGames.setAdapter(new TournamentGamesAdapter(getActivity()));

		Button addGameButton = (Button) view.findViewById(R.id.button_add_game_to_tournament);

		addGameButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View buttonView) {
				AppManager.getInstance().generateGame();
				ListView tournamentGamesListView = (ListView) view.findViewById(R.id.list_view_tournament_games);
				((TournamentGamesAdapter) tournamentGamesListView.getAdapter()).notifyDataSetChanged();
			}
		});

		Button addRoundButton = (Button) view.findViewById(R.id.button_add_round_to_tournament);

		addRoundButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View buttonView) {
				AppManager.getInstance().generateRound();
				ListView tournamentGamesListView = (ListView) view.findViewById(R.id.list_view_tournament_games);
				((TournamentGamesAdapter) tournamentGamesListView.getAdapter()).notifyDataSetChanged();
			}
		});

		Button deleteLastGameButton = (Button) view.findViewById(R.id.button_delete_last_game_from_tournament);

		deleteLastGameButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View buttonView) {
				AppManager.getInstance().removeLastGame();
				ListView tournamentGamesListView = (ListView) view.findViewById(R.id.list_view_tournament_games);
				((TournamentGamesAdapter) tournamentGamesListView.getAdapter()).notifyDataSetChanged();
			}
		});
	}
}
