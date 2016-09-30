package de.tum.kickercoding.tournamentviewer.setup.monsterdyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.tum.kickercoding.tournamentviewer.R;
import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;
import de.tum.kickercoding.tournamentviewer.tournament.monsterdyp.MonsterDypTournamentActivity;

public class MonsterDypPlayerSetupActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monster_dyp_player_setup);
	}

	public void goToTournament(View view) {
		try {
			saveSelectedPlayers();
		} catch (AppManagerException e) {
			AppManager.getInstance().displayError(this, "Could not save players: " + e.getMessage());
			return;
		}
		Intent intent = new Intent(this, MonsterDypTournamentActivity.class);
		startActivity(intent);
	}

	private void saveSelectedPlayers() throws AppManagerException {
		AppManager.getInstance().commitPlayerList();
	}
}
