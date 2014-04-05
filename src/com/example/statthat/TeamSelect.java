package com.example.statthat;

import java.util.*;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TeamSelect extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_select);
		
		
		//Create Existing Team Buttons
		ListView existing_teams = (ListView) findViewById(R.id.existing_team_view);

		//Need to create an array to lost into ArrayAdapter
		List<Team> temp_list = Team.listAll(Team.class); 
		Team[] teams = new Team[temp_list.size()];
		temp_list.toArray(teams);
		
		//Testing
//		String[] teamz = new String[]{"Team1", "Team2", "Team3"};
//		existing_teams.setAdapter(new ArrayAdapter<String>(this, R.layout.team_list, teamz));
		
		existing_teams.setAdapter(new TeamList(this,temp_list));
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.team_select, menu);
		
		return true;
	}

}
