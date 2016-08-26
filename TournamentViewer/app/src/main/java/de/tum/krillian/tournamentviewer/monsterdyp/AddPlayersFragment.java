package de.tum.krillian.tournamentviewer.monsterdyp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.tum.krillian.tournamentviewer.Constants;
import de.tum.krillian.tournamentviewer.R;
import de.tum.krillian.tournamentviewer.entities.Player;
import de.tum.krillian.tournamentviewer.manager.PreferenceFileManager;

public class AddPlayersFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_players, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO: check logging; was syse/syso but should be log

        super.onViewCreated(view, savedInstanceState);

        Player testPlayer1 = PreferenceFileManager.createNewPlayer("testplayer1", getActivity());
        Player testPlayer2 = PreferenceFileManager.createNewPlayer("testplayer2", getActivity());

        Set<String> playerNames = PreferenceFileManager.loadPlayerNamesFromGeneralSettings(getActivity());
        try{
            List<Player> players = PreferenceFileManager.getPlayersByNames(playerNames, getActivity());
            for(Player p: players){
                Log.d("DEBUG:", p.toString());
            }
        } catch (IllegalArgumentException e){
            createError(e.getMessage());
        }

        Player testPlayer3 = PreferenceFileManager.createNewPlayer("testplayer3", getActivity());

        System.out.println("====================");

        Set<String> playerNames2 = PreferenceFileManager.loadPlayerNamesFromGeneralSettings(getActivity());
        try{
            List<Player> players = PreferenceFileManager.getPlayersByNames(playerNames, getActivity());
            for(Player p: players){
                Log.d("DEBUG:", p.toString());
            }
        } catch (IllegalArgumentException e){
            createError(e.getMessage());
        }

    }



    public void createError(String message){
        // TODO: create error popup
        System.err.println("NEEDS TO BE IMPLEMENTED");
    }
}
