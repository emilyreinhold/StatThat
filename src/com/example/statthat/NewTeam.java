package com.example.statthat;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class NewTeam extends Activity {
	Team team;
	String team_name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_new_team);
		
		Button addPlayer = (Button) findViewById(R.id.add_player);
		addPlayer.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText teamName = (EditText)findViewById(R.id.TeamName);
				team_name = teamName.getText().toString();
				Sport bball = new Sport(getApplicationContext(), "basketball", "quarter");
				if(team == null) {
					team = new Team(getApplicationContext(), team_name, bball);
					team.save();
				}
				Intent new_player = new Intent(NewTeam.this, NewPlayerActivity.class);
				new_player.putExtra("team", team.getId());
				startActivity(new_player);
			}
		});
		
		Button done = (Button) findViewById(R.id.done_adding_players);
		done.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}
	@Override
	protected void onResume() {
		super.onResume();
		populateTeams();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_team, menu);
		return true;
	}
	
	public void populateTeams() {
		ListView player_list = (ListView) findViewById(R.id.player_list);
		if (team!=null) {
			List<Player> players = team.getPlayers();
			
			player_list.setAdapter(new PlayerShowList(this,players));
		}
	}

}
