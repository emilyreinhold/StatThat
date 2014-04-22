package com.example.statthat;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class TeamSelect extends Activity {
	private ListView existing_teams;
	private List<Team> teams;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_team_select);
		
		
		//Create Existing Team Buttons
		teams = Team.listAll(Team.class); 
		
		existing_teams = (ListView) findViewById(R.id.existing_team_view);
		
		//Make buttons clickable
		existing_teams.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(arg0.getContext(), TeamPageActivity.class);
				Team team = (Team) arg0.getItemAtPosition(arg2);
				intent.putExtra("team", team.name);
				startActivity(intent);
				
			}
			
		});
		
		existing_teams.setAdapter(new TeamList(this,teams));
		Button new_team = (Button)findViewById(R.id.button1);
		new_team.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent new_team_intent = new Intent(TeamSelect.this, NewTeam.class);
				startActivity(new_team_intent);
			}
		});
		
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.team_select, menu);
		
		return true;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		teams = Team.listAll(Team.class);
		existing_teams.setAdapter(new TeamList(this,teams));
	}

}
