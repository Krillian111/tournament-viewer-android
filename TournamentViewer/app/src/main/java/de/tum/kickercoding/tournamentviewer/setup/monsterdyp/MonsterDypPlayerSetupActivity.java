package de.tum.kickercoding.tournamentviewer.setup.monsterdyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import de.tum.kickercoding.tournamentviewer.R;
import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;
import de.tum.kickercoding.tournamentviewer.tournament.monsterdyp.MonsterDypTournamentActivity;

public class MonsterDypPlayerSetupActivity extends AppCompatActivity {

	private final String LOG_TAG = MonsterDypPlayerSetupActivity.class.toString();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monster_dyp_player_setup);
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

	public void goToTournament(View view) {
		try {
			boolean savingSuccessful = saveSelectedPlayers();
			if (!savingSuccessful) {
				AppManager.getInstance().displayError(this, "Insufficient players to start tournament!");
			} else {
				Intent intent = new Intent(this, MonsterDypTournamentActivity.class);
				startActivity(intent);
			}
		} catch (AppManagerException e) {
			AppManager.getInstance().displayError(this, "Could not save players: " + e.getMessage());
			return;
		}
	}

	private boolean saveSelectedPlayers() throws AppManagerException {
		// check if enough players are present
		// (needs to be done here as commitPlayerList() might also be used elsewhere)
		int playersInTournament = AppManager.getInstance().getPlayersForTournament().size();
		if (playersInTournament < 4) {
			if (playersInTournament < 2 || !AppManager.getInstance().isOneOnOne()) {
				return false;
			}
		}
		AppManager.getInstance().commitPlayerList();
		return true;
	}
}
