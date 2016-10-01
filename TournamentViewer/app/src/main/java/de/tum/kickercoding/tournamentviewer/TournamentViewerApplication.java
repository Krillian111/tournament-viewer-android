package de.tum.kickercoding.tournamentviewer;

import android.app.Application;
import android.util.Log;

import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;

public class TournamentViewerApplication extends Application {

	// reference to appManager to prevent garbage collection during application lifecycle
	private AppManager appManager;

	@Override
	public void onCreate() {
		super.onCreate();
		initSingletons();
	}

	private void initSingletons() {
		try {
			appManager = AppManager.getInstance();
			appManager.initialize(getApplicationContext());

		} catch (AppManagerException e) {
			Log.e(TournamentViewerApplication.class.toString(), "FATAL ERROR: initSingletons failed");
		}
	}
}
