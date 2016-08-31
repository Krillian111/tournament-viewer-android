package de.tum.kickercoding.tournamentviewer.monsterdyp;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import de.tum.kickercoding.tournamentviewer.Constants;
import de.tum.kickercoding.tournamentviewer.R;


// TODO: add comments to methods/class
public class MonsterDypSetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster_dyp_setup);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            BasicSetupFragment basicSetupFragment = new BasicSetupFragment();

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, basicSetupFragment).commit();
        }

    }

    /** Called when the user clicks the "Next" button in the basic setup fragment */
    public void selectPlayers(View view) {

        saveGameSettings(view);

        if (findViewById(R.id.fragment_container) != null) {

            // Create a new Fragment to be placed in the activity layout
            AddPlayersFragment addPlayersFragment = new AddPlayersFragment();

            // Add the fragment to the 'fragment_container' FrameLayout
            FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, addPlayersFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }


    private void saveGameSettings(View view){

        // save max score and number of games
        View fragmentContainer = findViewById(R.id.fragment_container);
        if(fragmentContainer != null){
            int maxScore = Integer.parseInt(((EditText) fragmentContainer.findViewById(R.id.editable_max_score)).getText().toString());
            int numberOfGames = Integer.parseInt(((EditText) fragmentContainer.findViewById(R.id.editable_number_games)).getText().toString());
            SharedPreferences preferences = getSharedPreferences(Constants.FILE_GENERAL_SETTINGS,0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(Constants.VAR_MAX_SCORE,maxScore);
            editor.putInt(Constants.VAR_NUMBER_OF_GAMES,numberOfGames);
            editor.commit();
        } else {
            Log.e("TESTTESTETSTTE","Game settings were not saved!");
        }
    }

    /** Called when plus-Button next to "max Score" is pressed */
    public void incrementMaxScore(View view){
        View rootView = view.getRootView();
        EditText editableMaxScore = (EditText) rootView.findViewById(R.id.editable_max_score);
        Integer currentScore = Integer.parseInt(editableMaxScore.getText().toString());
        currentScore++;
        editableMaxScore.setText(currentScore.toString());
    }

    /** Called when minus-Button next to "max Score" is pressed */
    public void decrementMaxScore(View view){
        View rootView = view.getRootView();
        EditText editableMaxScore = (EditText) rootView.findViewById(R.id.editable_max_score);
        Integer currentScore = Integer.parseInt(editableMaxScore.getText().toString());
        currentScore--;
        editableMaxScore.setText(currentScore.toString());
    }

    /** Called when plus-Button next to "number of games" is pressed */
    public void incrementNumberOfGames(View view){
        View rootView = view.getRootView();
        EditText editableMaxScore = (EditText) rootView.findViewById(R.id.editable_number_games);
        Integer currentScore = Integer.parseInt(editableMaxScore.getText().toString());
        currentScore++;
        editableMaxScore.setText(currentScore.toString());
    }

    /** Called when minus-Button next to "number of games" is pressed */
    public void decrementNumberOfGames(View view){
        View rootView = view.getRootView();
        EditText editableMaxScore = (EditText) rootView.findViewById(R.id.editable_number_games);
        Integer currentScore = Integer.parseInt(editableMaxScore.getText().toString());
        currentScore--;
        editableMaxScore.setText(currentScore.toString());
    }
}
