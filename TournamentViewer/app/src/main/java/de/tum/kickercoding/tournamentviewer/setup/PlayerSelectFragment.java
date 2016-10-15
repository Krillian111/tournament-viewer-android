package de.tum.kickercoding.tournamentviewer.setup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import de.tum.kickercoding.tournamentviewer.R;
import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;

/**
 * Fragment contains a ListView with a global list of players and a button to add additional players
 * <br>
 * Changes to list are only permanent if they are commited using {@link AppManager#commitPlayerList()} ()}
 */
public class PlayerSelectFragment extends Fragment {

	public PlayerSelectFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_select_players, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		preparePlayerListView(view);
		attachButtonListener(view);
	}

	private void preparePlayerListView(View view) {
		ListView playerListView = (ListView) view.findViewById(R.id.list_view_add_players);
		playerListView.setAdapter(new PlayerListAdapter(getActivity()));
	}

	private void attachButtonListener(View view) {
		Button addPlayerButton = (Button) view.findViewById(R.id.button_add_player);
		addPlayerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View buttonView) {
				addPlayerToList(buttonView);
			}
		});
	}

	private void addPlayerToList(View buttonView) {
		View rootView = buttonView.getRootView();
		EditText editableNewPlayer = (EditText) rootView.findViewById(R.id.editable_new_player);
		String newPlayer = editableNewPlayer.getText().toString();
		try {
			AppManager.getInstance().addNewPlayer(newPlayer);
		} catch (AppManagerException e) {
			AppManager.getInstance().displayMessage(buttonView.getContext(), e.getMessage());
			return;
		}
		ListView listView = (ListView) rootView.findViewById(R.id.list_view_add_players);
		((PlayerListAdapter) listView.getAdapter()).notifyDataSetChanged();
		editableNewPlayer.setText("");
	}
}
