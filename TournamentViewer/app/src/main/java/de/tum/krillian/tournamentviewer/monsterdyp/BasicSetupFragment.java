package de.tum.krillian.tournamentviewer.monsterdyp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import de.tum.krillian.tournamentviewer.Constants;
import de.tum.krillian.tournamentviewer.R;

public class BasicSetupFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basic_setup, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        // read saved values from settings
        SharedPreferences settings = getActivity().getSharedPreferences(Constants.FILE_GENERAL_SETTINGS,0);
        Integer currentMaxScore = settings.getInt(Constants.VAR_MAX_SCORE, Constants.DEFAULT_MAX_SCORE);
        ((EditText) view.findViewById(R.id.editable_max_score)).setText(currentMaxScore.toString());
        Integer numberOfGames = settings.getInt(Constants.VAR_NUMBER_OF_GAMES, Constants.DEFAULT_NUMBER_OF_GAMES);
        ((EditText) view.findViewById(R.id.editable_number_games)).setText(numberOfGames.toString());
    }
}
