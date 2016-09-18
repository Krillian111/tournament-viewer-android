package de.tum.kickercoding.tournamentviewer.modes.monsterdyp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.tum.kickercoding.tournamentviewer.R;

public class GameListFragment extends Fragment {

    public GameListFragment() {}

    public static GameListFragment newInstance() {
        return new GameListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_list, container, false);
    }

}
