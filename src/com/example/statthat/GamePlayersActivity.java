package com.example.statthat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class GamePlayersActivity extends Activity  implements OnItemSelectedListener{

	String location;
	String teamName;
	HashMap<String, Integer> team;
	HashMap<String, HashMap<String, Integer>> players;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_players);
		getActionBar().hide();
		
		Button b = (Button) findViewById(R.id.button_players);
		b.setPressed(true);
		
		
		Intent in = getIntent();
		location = in.getStringExtra("location");
		teamName = in.getStringExtra("teamName");
		
		// set team name 
		TextView tView = (TextView) findViewById(R.id.team_title);
		tView.setText(teamName);
		
		LinearLayout gamesLayout = (LinearLayout) findViewById(R.id.linear_player_stats); 
		Spinner spinner = (Spinner) findViewById(R.id.spinner_players);
		
		List<Game> games = Game.find(Game.class, "location = ?", location);
		Game game = games.get(0);
		List<Stat> teamStats =  game.getStats();
		team = new HashMap<String,Integer>();
		
		
		players = new HashMap<String, HashMap<String, Integer>>();

		Team t = Team.find(Team.class, "name = ?", teamName).get(0);
		List<Player> plays = t.getPlayers();
		for (Player p: plays) {
			HashMap<String, Integer> h = new HashMap<String, Integer>();
			List<Stat> playerStats = game.getStatsForPlayer(p); // questionable
			for(Stat s: playerStats) {
				String stat = s.statType.name;
				if (h.containsKey(s)) {
					int c = h.get(s);
					c ++;
					h.put(stat, c);
				}
				else {
					h.put(stat, 1);
				}
			}
			players.put(p.firstName + " " + p.lastName, h);
		}

		for (Stat stat: teamStats) {
			String s = stat.statType.name;
			if (team.containsKey(s)) {
				int c = team.get(s);
				c ++;
				team.put(s, c);
			}
			else {
				team.put(s, 1);
			}
		}
		
		List l = new ArrayList();
		l.addAll(players.keySet());
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(gamesLayout.getContext(),
				android.R.layout.simple_dropdown_item_1line, l);

		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
	}
	
	
	
	public void onTeamClick(View v) {
		Intent intent = new Intent(GamePlayersActivity.this, GameActivity.class);
		intent.putExtra("location", location);
		intent.putExtra("teamName", teamName);		
		GamePlayersActivity.this.startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_players, menu);
		return true;
	}

    protected void onResume() {
    	super.onResume();
    	Button b = (Button) findViewById(R.id.button_players);
		b.setPressed(true);
    }


	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		// make spinner text bigger
		
		((TextView) arg0.getChildAt(0)).setTextSize(25);			
		String s = (String) arg0.getItemAtPosition(arg2);
		TableLayout layout = (TableLayout) findViewById(R.id.table_layout_players);
		// reset tablerows
		layout.removeAllViews();
		for (String stat: players.get(s).keySet()) {
			TableRow row = new TableRow(layout.getContext());
			TextView t = new TextView(layout.getContext());
			t.setText(stat + "        ");
			t.setTextSize(30);

			TextView tt = new TextView(layout.getContext());
			tt.setText(String.valueOf(players.get(s).get(stat)));
			tt.setTextSize(30);

			row.addView(t);
			row.addView(tt);
			layout.addView(row);		
		}

	}



	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
