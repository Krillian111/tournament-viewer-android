package de.tum.kickercoding.tournamentviewer.tournament.monsterdyp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import de.tum.kickercoding.tournamentviewer.R;
import de.tum.kickercoding.tournamentviewer.StartMenuActivity;
import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;

public class MonsterDypTournamentActivity extends AppCompatActivity {

	private final String LOG_TAG = MonsterDypTournamentActivity.class.toString();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monster_dyp_tournament);
	}

	@Override
	public void onDestroy() {
		try {
			AppManager.getInstance().saveTournament();
		} catch (AppManagerException e) {
			Log.e(LOG_TAG, "onDestroy: saving tournament failed: " + e.getMessage());
		}
		super.onDestroy();
	}

	public void generatePlayoffs(View view) {
		//TODO: generate playoff games
	}

	public void finishTournament(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Finish Tournament?");
		builder.setMessage("Finishing the tournament will disable all further changes to the tournament.\nIn " +
				"addition the player ladder will be updated with the results of this tournament.");
		builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				try {
					AppManager.getInstance().finishTournament();
					goToStartMenu();
				} catch (AppManagerException e) {
					AppManager.getInstance().displayError(MonsterDypTournamentActivity.this, e.getMessage());
					dialog.cancel();
				}
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		builder.show();
	}

	public void goToStartMenu() {
		Intent intent = new Intent(this, StartMenuActivity.class);
		startActivity(intent);
	}
}
