package de.tum.kickercoding.tournamentviewer.setup.monsterdyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

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
	 *
	 * @param view
	 */
	public void goToPlayerSetup(View view) {
		try {
			saveGameSettings(view.getRootView());
			AppManager.getInstance().setTournamentParameters();
		} catch (AppManagerException e) {
			AppManager.getInstance().displayError(this, "Failed to save game settings: " + e.getMessage());
			return;
		}
		Intent intent = new Intent(this, MonsterDypPlayerSetupActivity.class);
		startActivity(intent);
	}

	// save max score and number of games
	private void saveGameSettings(View view) throws AppManagerException {
		int maxScore = Integer.parseInt(((TextView) view.findViewById(R.id.var_max_score)).getText().toString());
		int numberOfGames = Integer.parseInt(((TextView) view.findViewById(R.id.var_number_games)).getText().toString
				());
		boolean oneOnOne = ((CheckBox) view.findViewById(R.id.checkbox_one_on_one)).isChecked();
		AppManager.getInstance().setMaxScore(maxScore);
		AppManager.getInstance().setNumberOfGames(numberOfGames);
		AppManager.getInstance().setOneOnOne(oneOnOne);
	}
}
