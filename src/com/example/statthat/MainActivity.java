package com.example.statthat;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_main);
		
		Context c = getApplicationContext();
		
		// Deletes all data in database
		DBHelper.deleteAllData();
		
		// Add basketball and its stat types to database once
		StatType.populateBballStatTypes(c);
		
		// Add a team, players, game, and stats for testing
		DBHelper.populateTestData(c);
		
		// login - no username/password checks
		Button login = (Button) findViewById(R.id.login);
		login.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent team_select = new Intent(MainActivity.this, TeamSelect.class);
				startActivity(team_select);
			}
			
		});
		Parser p = new Parser();
		String[] m = {"playeR three three point missed", "player twenty two offensive rebound", "player five foul", "player 6 steal"};
		ArrayList<String> list = new ArrayList<String>();
		for (String w : m) {
			list.add(w);
		}
		System.out.println("===============================");
		System.out.println(p.parseMatch(list));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
//		testing();

		return true;
	}
	
	
	
	// Andrew - Used for testing
	private void testing(){
		//destroy DB and seed
//		reset_and_seed();
		
		Intent teamSelect = new Intent(this, TeamSelect.class);
		Intent gameSetup = new Intent(this, GameSetup.class);
		Intent recordStat = new Intent(this, RecordStat.class);
		
		//Start app on...
		startActivity(recordStat);
	}
	
	private void  reset_and_seed(){
		Team.deleteAll(Team.class);
		String[] team_names = new String[]{"Team1","Team2","Team3"};
		for(String name : team_names){
			new Team(getApplicationContext(), name, null).save();
		}
	}

}
