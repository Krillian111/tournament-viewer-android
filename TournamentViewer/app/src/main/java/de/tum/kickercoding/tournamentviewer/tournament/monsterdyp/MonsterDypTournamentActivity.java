package de.tum.kickercoding.tournamentviewer.tournament.monsterdyp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import de.tum.kickercoding.tournamentviewer.R;
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
}
