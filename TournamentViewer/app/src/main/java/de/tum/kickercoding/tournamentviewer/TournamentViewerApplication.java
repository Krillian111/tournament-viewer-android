package de.tum.kickercoding.tournamentviewer;

import android.app.Application;
import android.util.Log;

import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;

public class TournamentViewerApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		initSingletons();
	}

	private void initSingletons() {
		try {
			AppManager.getInstance().initialize(getApplicationContext());
		} catch (AppManagerException e) {
			Log.e(TournamentViewerApplication.class.toString(), "FATAL ERROR: initSingletons failed");
		}
	}
}
