package de.tum.kickercoding.tournamentviewer.tournament.monsterdyp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import de.tum.kickercoding.tournamentviewer.R;
import de.tum.kickercoding.tournamentviewer.StartMenuActivity;
import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;
import de.tum.kickercoding.tournamentviewer.setup.monsterdyp.MonsterDypPlayerSetupActivity;
import de.tum.kickercoding.tournamentviewer.setup.monsterdyp.TournamentPagerAdapter;
import de.tum.kickercoding.tournamentviewer.tournament.TournamentGamesFragment.OnGameChangeListener;

public class MonsterDypTournamentActivity extends AppCompatActivity implements OnGameChangeListener {

	private TournamentPagerAdapter pagerAdapter;

	private final String LOG_TAG = MonsterDypTournamentActivity.class.toString();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monster_dyp_tournament);

		ViewPager pager = (ViewPager) findViewById(R.id.tournament_pager);
		TabLayout tabs = (TabLayout) findViewById(R.id.tournament_pager_tabs);
		pagerAdapter = new TournamentPagerAdapter(getSupportFragmentManager());

		pager.setAdapter(pagerAdapter);
		tabs.setupWithViewPager(pager);

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
		AppManager.getInstance().displayMessage(this, "Sorry, this feature has not been implemented yet");
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
					AppManager.getInstance().displayMessage(MonsterDypTournamentActivity.this, e.getMessage());
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

	public void goToPlayerSelection() {
		Intent intent = new Intent(this, MonsterDypPlayerSetupActivity.class);
		startActivity(intent);
	}

	public void goToPlayerSetup(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Delete or add player?");
		builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				goToPlayerSelection();
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

	@Override
	public void onGameChanged() {
		((OnGameChangeListener) pagerAdapter.getItem(TournamentPagerAdapter.PAGE_STATS)).onGameChanged();
	}
}
