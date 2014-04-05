package com.example.statthat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Add basketball and its stat types to database once
		Context c = getApplicationContext();
		StatType.populateBballStatTypes(c);
		
		// Add a team, players, game, and stats for testing
		// DBHelper.populateTestData(c);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		
		testing();

		return true;
	}
	
	
	
	// Andrew - Used for testing
	private void testing(){
		//destroy DB and seed
		reset_and_seed();
		
		Intent teamSelect = new Intent(this, TeamSelect.class);
		Intent gameSetup = new Intent(this, GameSetup.class);

		
		//Start app on...
		startActivity(gameSetup);
	}
	
	private void  reset_and_seed(){
		Team.deleteAll(Team.class);
		String[] team_names = new String[]{"Team1","Team2","Team3"};
		for(String name : team_names){
			new Team(getApplicationContext(), name, null).save();
		}
	}

}
