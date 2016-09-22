package de.tum.kickercoding.tournamentviewer.setup.monsterdyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import de.tum.kickercoding.tournamentviewer.R;
import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;


/**
 * First activity to be called during setup, handles setting basic parameters for MonsterDYP
 */
public class MonsterDypBasicSetupActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monster_dyp_basic_setup);
	}

	/**
	 * Called when "Next" button is pressed: starts activity for selecting players <br>
	 * Returns without starting new activity if game settings could not be saved
	 * @param view
	 */
	public void goToPlayerSetup(View view) {
		try {
			saveGameSettings(view.getRootView());
			AppManager.getInstance().startNewTournament();
		} catch (AppManagerException e) {
			AppManager.getInstance().displayError(this, "Failed to save game settings: " + e.getMessage());
			return;
		}
		Intent intent = new Intent(this, MonsterDypPlayerSetupActivity.class);
		startActivity(intent);
	}

	// save max score and number of games
	private void saveGameSettings(View view) throws AppManagerException {
		int maxScore = Integer.parseInt(((EditText) view.findViewById(R.id.editable_max_score)).getText().toString());
		int numberOfGames = Integer.parseInt(((EditText) view.findViewById(R.id.editable_number_games)).getText().toString());
		AppManager.getInstance().saveMaxScore(maxScore);
		AppManager.getInstance().saveNumberOfGames(numberOfGames);
	}
}