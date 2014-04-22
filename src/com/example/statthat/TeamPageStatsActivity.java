package com.example.statthat;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TeamPageStatsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_page_stats);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.team_page_stats, menu);
		return true;
	}

}
