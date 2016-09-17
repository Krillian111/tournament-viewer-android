package de.tum.kickercoding.tournamentviewer.modes.monsterdyp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import de.tum.kickercoding.tournamentviewer.R;
import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;


// TODO: add comments to methods/class
public class BasicSetupFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basic_setup, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        MonsterDypSetupActivity activity = (MonsterDypSetupActivity) getActivity();
        activity.setCurrentState(MonsterDypSetupActivity.STAGE_BASIC_SETUP);
        try {
            String maxScore = "" + AppManager.getInstance().loadMaxScore();
            String numberOfGames = "" + AppManager.getInstance().loadNumberOfGames();
            ((EditText) view.findViewById(R.id.editable_max_score)).setText(maxScore);
            ((EditText) view.findViewById(R.id.editable_number_games)).setText(numberOfGames);
        } catch (AppManagerException e) {
            AppManager.getInstance().displayError(getActivity(), e.getMessage());
        }

    }
}
