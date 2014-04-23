package com.example.statthat;

import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class TeamPageStatsActivity extends Activity implements OnClickListener{

	String teamName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_page_stats);
		getActionBar().hide();

		
		
		Intent in = getIntent();
		teamName = in.getStringExtra("team");
		
		TextView tView = (TextView) findViewById(R.id.team_title2);
		tView.setText(teamName);
		
		
		List<Team> t = Team.find(Team.class, "name = ?", teamName);
		Team team = t.get(0);
		
		List<Game> games = team.getGames();
		HashMap<String, String> gamess = new HashMap<String, String>();

		for (Game g : games) {
			gamess.put("vs. " + g.opposingTeamName, g.date + " @ " + g.location);
		}
		
		
		
		LinearLayout gamesLayout = (LinearLayout) findViewById(R.id.linear_games);

		for (String vs : gamess.keySet()) {
			Button button = new Button(gamesLayout.getContext());
			button.setText(vs + "\n" + gamess.get(vs));
			button.setOnClickListener(this);
			button.setTextSize(25);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			params.setMargins(10,10,10,10);
			button.setBackgroundResource(R.drawable.custom_button_confirm);
			button.setLayoutParams(params);
			gamesLayout.addView(button);
		}
		
	}

	
	public void onPlayersClick(View v) {
		
		Intent intent = new Intent(TeamPageStatsActivity.this, TeamPageActivity.class);
		intent.putExtra("team", teamName);

		TeamPageStatsActivity.this.startActivity(intent);
	}
	
	  protected void onResume() {
	    	super.onResume();
	    	Button b = (Button) findViewById(R.id.button_team_stats);
			b.setPressed(true);
	    }
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.team_page_stats, menu);
		return true;
	}


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		// intent to game page
		Button b = (Button) arg0;
		String bText = b.getText().toString();
		String[] bArr = bText.split(" ");
		
		Intent intent = new Intent(arg0.getContext(), GameActivity.class);
		intent.putExtra("teamName", teamName);
		intent.putExtra("location", bArr[3]);
		startActivity(intent);
		
	}

}
