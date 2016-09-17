package de.tum.kickercoding.tournamentviewer.modes.monsterdyp;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
public class AddPlayersFragment extends Fragment {

    private ListView playerListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_players, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // force stage to reflect current stage (important for back button behaviour)
        ((MonsterDypSetupActivity) getActivity()).setCurrentState(MonsterDypSetupActivity.STAGE_ADD_PLAYERS);

        playerListView = (ListView) getActivity().findViewById(R.id.listViewPlayers);

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
