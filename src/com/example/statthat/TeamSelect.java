package com.example.statthat;

import java.util.*;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TeamSelect extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_select);
		
		
		//Create Existing Team Buttons
		ListView existing_teams = (ListView) findViewById(R.id.existing_team_view);
		List<Team> teams = Team.listAll(Team.class); 
		
		//Make buttons clickable
		existing_teams.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(arg0.getContext(), TeamPageActivity.class);
				startActivity(intent);
				
			}
			
		});
		
		existing_teams.setAdapter(new TeamList(this,teams));
		
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.team_select, menu);
		
		return true;
	}

}
