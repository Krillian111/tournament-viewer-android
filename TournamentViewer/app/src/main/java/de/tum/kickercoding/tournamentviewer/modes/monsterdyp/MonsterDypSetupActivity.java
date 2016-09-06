package de.tum.kickercoding.tournamentviewer.modes.monsterdyp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import de.tum.kickercoding.tournamentviewer.Constants;
import de.tum.kickercoding.tournamentviewer.R;
import de.tum.kickercoding.tournamentviewer.entities.Player;


// TODO: add comments to methods/class
public class MonsterDypSetupActivity extends AppCompatActivity {

    private final int STAGE_BASIC_SETUP = 1;

    private final int STAGE_ADD_PLAYERS = 2;

    private int currentState = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster_dyp_setup);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        View fragmentContainer = findViewById(R.id.fragment_container);
        if (fragmentContainer != null) {

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
        } else {
            Log.e(MonsterDypSetupActivity.class.toString(), "couldn't find fragment container");
        }
    }

    /** Called when the user clicks the "Next" button in the basic setup fragment */
    public void nextStage(View view) {
        switch(currentState){
            case STAGE_BASIC_SETUP:
                View fragmentContainer = findViewById(R.id.fragment_container);
                saveGameSettings(fragmentContainer);
                switchToAddPlayersFragment(fragmentContainer);
                break;
            case STAGE_ADD_PLAYERS:
                saveSelectedPlayers();
                switchToTournamentActivity();
                break;
        }
    }

    // save max score and number of games
    private void saveGameSettings(View fragmentContainer){
        if(fragmentContainer != null){
            int maxScore = Integer.parseInt(((EditText) fragmentContainer.findViewById(R.id.editable_max_score)).getText().toString());
            int numberOfGames = Integer.parseInt(((EditText) fragmentContainer.findViewById(R.id.editable_number_games)).getText().toString());
            // TODO: refactor with calls to AppManager (write commitMaxScore/commitNrOfGames)
            SharedPreferences preferences = getSharedPreferences(Constants.FILE_GENERAL_SETTINGS,0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(Constants.VAR_MAX_SCORE,maxScore);
            editor.putInt(Constants.VAR_NUMBER_OF_GAMES,numberOfGames);
            editor.commit();
        } else {
            Log.e(MonsterDypSetupActivity.class.toString(),"fragment container was empty; game settings were not saved!");
        }
    }

    private void switchToAddPlayersFragment(View fragmentContainer){
        if (fragmentContainer != null) {
            // Create a new Fragment to be placed in the activity layout
            AddPlayersFragment addPlayersFragment = new AddPlayersFragment();

            // Add the fragment to the 'fragment_container' FrameLayout
            FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, addPlayersFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            Log.e(MonsterDypSetupActivity.class.toString(),"fragment container was empty; couldn't change state");
        }
    }

    private void saveSelectedPlayers() {
        // TODO: implement
    }

    private void switchToTournamentActivity() {
        // TODO: implement
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

    // test output method
    public void testAddingPlayer(View view){
//        try {
//
//            AppManager appManager = AppManager.getInstance();
//            List<Player> playerList = appManager.getAllPlayers();
//            printPlayers(playerList);
//
//            System.out.println(playerList.size());
//
//            appManager.addNewPlayer("testplayer");
//            appManager.commitChanges();
//
//            playerList = appManager.getAllPlayers();
//            printPlayers(playerList);
//
//            System.out.println(playerList.size());
//
//        } catch (AppManagerException e) {
//            Log.e(AddPlayersFragment.class.toString(),e.toString());
//        }
    }

    private void printPlayers(List<Player> playerList){
        for (Player p: playerList) {
            Log.e("TESTOUTPUT:", p.toString());
        }
    }
}
