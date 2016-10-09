package de.tum.kickercoding.tournamentviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;
import de.tum.kickercoding.tournamentviewer.setup.monsterdyp.MonsterDypBasicSetupActivity;
import de.tum.kickercoding.tournamentviewer.tournament.monsterdyp.MonsterDypTournamentActivity;
import de.tum.kickercoding.tournamentviewer.util.TournamentMode;

/**
 * Activity is called when app is opened, responsible for initializing basic infrastructure
 * Presents the user with a screen of basic options
 */
// TODO: add possibility to load pending tournament (click on monsterdyp calls initialization of TournamentManager
// which resets the Tournament
public class StartMenuActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_menu);
	}

	/**
	 * Called when the user clicks the MonsterDYP button
	 */
	public void monsterDypSetup(View view) {
		AppManager.getInstance().startNewTournament(TournamentMode.MONSTERDYP);
		Intent intent = new Intent(this, MonsterDypBasicSetupActivity.class);
		startActivity(intent);
	}

	/**
	 * Called when the user clicks the load tournament button
	 */
	public void loadLastTournament(View view) {
		try {
			AppManager.getInstance().loadTournament();
			Intent intent = new Intent(this, MonsterDypTournamentActivity.class);
			startActivity(intent);
		} catch (AppManagerException e) {
			AppManager.getInstance().displayMessage(this, e.getMessage());
		}
	}
}
