package de.tum.kickercoding.tournamentviewer.modes.monsterdyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import de.tum.kickercoding.tournamentviewer.R;
import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;


// TODO: add comments to methods/class
public class MonsterDypSetupActivity extends AppCompatActivity {

    public static final String STAGE_BASIC_SETUP = "BASIC_SETUP";

    public static final String STAGE_ADD_PLAYERS = "ADD_PLAYERS";

	private String currentState = STAGE_BASIC_SETUP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster_dyp_setup);
		setFragmentViewToCurrentStage(savedInstanceState);
    }

    /** Called when the user clicks the "Next" button in the basic setup fragment */
    public void nextStage(View view) {
        View fragmentContainer = findViewById(R.id.monster_dyp_setup_fragment_container);
        switch(currentState){
            case STAGE_BASIC_SETUP:
                try {
                    saveGameSettings(fragmentContainer);
                } catch (AppManagerException e) {
                    AppManager.getInstance().displayError(this, "Failed to save game settings: " + e.getMessage());
                    break;
                }
                switchToAddPlayersFragment(fragmentContainer);
                break;
            case STAGE_ADD_PLAYERS:
                try {
                    saveSelectedPlayers();
                } catch (AppManagerException e) {
                    AppManager.getInstance().displayError(this, "Failed to start tournament: " + e.getMessage());
                    break;
                }
                switchToTournamentActivity();
                break;
        }
    }

    private void setFragmentViewToCurrentStage(Bundle savedInstanceState){
		Fragment fragmentForCurrentStage = null;
		switch(currentState) {
            case STAGE_BASIC_SETUP:
				// Create a new Fragment to be placed in the activity layout
				fragmentForCurrentStage= BasicSetupFragment.getInstance();
				break;
            case STAGE_ADD_PLAYERS:
				fragmentForCurrentStage = AddPlayersFragment.getInstance();
                break;
        }
		// Check that the activity is using the layout version with
		// the fragment_container FrameLayout
		View fragmentContainer = findViewById(R.id.monster_dyp_setup_fragment_container);
		if (fragmentContainer != null) {
			// However, if we're being restored from a previous state,
			// then we don't need to do anything and should return or else
			// we could end up with overlapping fragments.
			if (savedInstanceState != null) {
				return;
			}
			// Add the fragment to the 'fragment_container' FrameLayout
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.monster_dyp_setup_fragment_container, fragmentForCurrentStage);
			transaction.addToBackStack(currentState).commit();
		} else {
			Log.e(MonsterDypSetupActivity.class.toString(), "couldn't find fragment container");
		}
    }

    // save max score and number of games
    private void saveGameSettings(View fragmentContainer) throws AppManagerException {
        if(fragmentContainer != null){
            int maxScore = Integer.parseInt(((EditText) fragmentContainer.findViewById(R.id.editable_max_score)).getText().toString());
            int numberOfGames = Integer.parseInt(((EditText) fragmentContainer.findViewById(R.id.editable_number_games)).getText().toString());
            AppManager.getInstance().saveMaxScore(maxScore);
            AppManager.getInstance().saveNumberOfGames(numberOfGames);
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
            transaction.replace(R.id.monster_dyp_setup_fragment_container, addPlayersFragment);
            transaction.addToBackStack(STAGE_ADD_PLAYERS);
            transaction.commit();
        } else {
            Log.e(MonsterDypSetupActivity.class.toString(),"fragment container was empty; couldn't change state");
        }
    }

    private void saveSelectedPlayers() throws AppManagerException {
        AppManager.getInstance().commitChanges();
    }

    private void switchToTournamentActivity() {
        Intent intent = new Intent(this, MonsterDypTournamentActivity.class);
        startActivity(intent);
    }

    /*
    * *************************************
    *  ON CLICK METHODS BasicSetupFragment
    * **************************************
    */

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

    /*
    * *************************************
    *  ON CLICK METHODS AddPlayersFragment
    * **************************************
    */

    public void addPlayerToList(View view) {
        View rootView = view.getRootView();
        EditText editableNewPlayer = (EditText) rootView.findViewById(R.id.editable_new_player);
        String newPlayer = editableNewPlayer.getText().toString();
        ListView listView = (ListView) rootView.findViewById(R.id.list_view_add_players);
        try {
            AppManager.getInstance().addNewPlayer(newPlayer);
        } catch (AppManagerException e) {
            AppManager.getInstance().displayError(this, e.getMessage());
        }
        PlayerListAdapter playerListAdapter = (PlayerListAdapter) listView.getAdapter();
        playerListAdapter.notifyDataSetChanged();
    }

	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}
}
