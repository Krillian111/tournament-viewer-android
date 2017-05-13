package de.tum.kickercoding.tournamentviewer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.tum.kickercoding.tournamentviewer.manager.AppManager;
import de.tum.kickercoding.tournamentviewer.modes.ladder.LadderActivity;
import de.tum.kickercoding.tournamentviewer.setup.monsterdyp.MonsterDypBasicSetupActivity;
import de.tum.kickercoding.tournamentviewer.tournament.monsterdyp.MonsterDypTournamentActivity;
import de.tum.kickercoding.tournamentviewer.util.TournamentMode;

/**
 * Activity is called when app is opened, responsible for initializing basic infrastructure
 * Presents the user with a screen of basic options
 */
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
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Start a new tournament?");
		builder.setMessage("This action will delete all data of an unfinished tournament!");
		builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				AppManager.getInstance().startNewTournament(TournamentMode.MONSTERDYP);
				Intent intent = new Intent(StartMenuActivity.this, MonsterDypBasicSetupActivity.class);
				startActivity(intent);
			}
		});
		builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}

	/**
	 * Called when the user clicks the load tournament button
	 */
	public void loadLastTournament(View view) {
		Intent intent = new Intent(this, MonsterDypTournamentActivity.class);
		startActivity(intent);
	}

	/**
	 * called when user clicks the ladder button
	 *
	 * @param view
	 */
	public void loadLadder(View view) {
		Intent intent = new Intent(this, LadderActivity.class);
		startActivity(intent);
	}
}
