package de.tum.kickercoding.tournamentviewer.setup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import de.tum.kickercoding.tournamentviewer.R;
import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;

/**
 * Fragment contains a ListView with a global list of players and a button to add additional players
 * <br>
 * Changes to list are only permanent if they are commited using {@link AppManager#commitPlayerList()} ()}
 */
public class AddPlayersFragment extends Fragment {

	public AddPlayersFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_add_players, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		preparePlayerListView(view);
		attachButtonListener(view);
	}

	private void preparePlayerListView(View view) {
		ListView playerListView = (ListView) view.findViewById(R.id.list_view_add_players);
		playerListView.setAdapter(new PlayerListAdapter(getActivity()));

		playerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				try {
					Player player = AppManager.getInstance().getPlayer(position);
					AppManager.getInstance().displayError(getActivity(), player.toString());
				} catch (AppManagerException e) {
					AppManager.getInstance().displayError(getActivity(), "Error: Player not found");
				}
			}
		});
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
			AppManager.getInstance().displayError(buttonView.getContext(), e.getMessage());
			return;
		}
		ListView listView = (ListView) rootView.findViewById(R.id.list_view_add_players);
		((PlayerListAdapter) listView.getAdapter()).notifyDataSetChanged();
	}
}
