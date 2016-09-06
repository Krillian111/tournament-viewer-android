package de.tum.kickercoding.tournamentviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;
import de.tum.kickercoding.tournamentviewer.modes.monsterdyp.MonsterDypSetupActivity;

// TODO: add comments to methods/class
public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initializeApp();
    }

    /** Called when the user clicks the MonsterDYP button */
    public void monsterDypSetup(View view) {
        Intent intent = new Intent(this, MonsterDypSetupActivity.class);
        startActivity(intent);
    }

    private void initializeApp() {
        try {
            AppManager.getInstance().initialize(getApplicationContext());
        } catch (AppManagerException e) {
            e.printStackTrace();
            // TODO: implement error handling
        }
    }
}
