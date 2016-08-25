package de.tum.krillian.tournamentviewer.monsterdyp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import de.tum.krillian.tournamentviewer.Constants;
import de.tum.krillian.tournamentviewer.R;

public class AddPlayersFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_players, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences globalPlayerList = getActivity().getSharedPreferences(Constants.FILE_GLOBAL_PLAYER_LIST,0);
        Map<String,?> playerMapWithElo = globalPlayerList.getAll();

        SharedPreferences tournamentPreferences = getActivity().getSharedPreferences(Constants.FILE_TOURNAMENT,0);
        SharedPreferences.Editor editor = tournamentPreferences.edit();
        editor.clear();

        ArrayList<String> playerListForView = new ArrayList<>();

        // copy global player map to tournament preference file
        // add player names to array for view
        for(Map.Entry<String,?> entry: playerMapWithElo.entrySet()){
            String playerName = entry.getKey();
            Integer playerElo = (Integer) entry.getValue();
            editor.putInt(playerName,playerElo);
            playerListForView.add(playerName);
        }
        editor.commit();


    }
}
