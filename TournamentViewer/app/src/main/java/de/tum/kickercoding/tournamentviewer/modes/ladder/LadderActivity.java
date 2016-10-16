package de.tum.kickercoding.tournamentviewer.modes.ladder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.List;

import de.tum.kickercoding.tournamentviewer.R;
import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;

public class LadderActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ladder);
		ListView playerListView = (ListView) findViewById(R.id.list_view_ladder);
		List<Player> playerList = AppManager.getInstance().getAllPlayers();
		playerListView.setAdapter(new PlayerLadderAdapter(this, playerList));
	}
}
