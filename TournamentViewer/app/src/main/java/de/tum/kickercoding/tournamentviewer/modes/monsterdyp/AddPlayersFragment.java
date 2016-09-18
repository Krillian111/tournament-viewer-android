package de.tum.kickercoding.tournamentviewer.modes.monsterdyp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import de.tum.kickercoding.tournamentviewer.R;
import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;

// TODO: add comments to methods/class
// TODO: refactor as activity
public class AddPlayersFragment extends Fragment {

    public AddPlayersFragment(){}

    public static AddPlayersFragment getInstance() {
        return new AddPlayersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_players, container, false);
    }

    // TODO: consider moving functionality to onActivityCreated due to Views not being available?
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // force stage to reflect current stage (important for back button behaviour)
        ((MonsterDypSetupActivity) getActivity()).setCurrentState(MonsterDypSetupActivity.STAGE_ADD_PLAYERS);

        ListView playerListView = (ListView) getActivity().findViewById(R.id.list_view_add_players);

        playerListView.setAdapter(new PlayerListAdapter(getActivity()));

        playerListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View view,int position, long arg3) {
                try {
                    Player player = AppManager.getInstance().getPlayer(position);
                    AppManager.getInstance().displayError(getActivity(), player.toString());
                } catch (AppManagerException e) {
                    AppManager.getInstance().displayError(getActivity(), "Error: Player not found");
                }
            }
        });
    }
}
