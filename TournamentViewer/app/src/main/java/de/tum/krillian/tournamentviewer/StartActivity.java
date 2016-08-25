package de.tum.krillian.tournamentviewer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import de.tum.krillian.tournamentviewer.monsterdyp.MonsterDypSetupActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    /** Called when the user clicks the MonsterDYP button */
    public void monsterDypSetup(View view) {
        Intent intent = new Intent(this, MonsterDypSetupActivity.class);
        startActivity(intent);
    }
}
