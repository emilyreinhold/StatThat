package com.example.statthat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class TeamPageActivity extends FragmentActivity {
	public static String teamName;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().hide();
		getActionBar().setDisplayShowHomeEnabled(false);              
		getActionBar().setDisplayShowTitleEnabled(false);
		setContentView(R.layout.activity_team_page);	

		// get intent for team name
		Intent in = getIntent();
		teamName = in.getStringExtra("team");

		TextView tView = (TextView) findViewById(R.id.team_title2);
		tView.setText(teamName);

		FragmentTabHost tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		tabHost.setup(this, getSupportFragmentManager(), R.id.tab_1);
		tabHost.addTab(tabHost.newTabSpec("players").setIndicator("Players"),
				PFragment.class, null);
		tabHost.addTab(tabHost.newTabSpec("stats").setIndicator("Game Reports"),
				SFragment.class, null);
	}

	public static class PFragment extends Fragment {
		@SuppressLint("NewApi")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Inflate the layout for this fragment
			View v = inflater.inflate(R.layout.myfragment_layout, container, false);


			List<Team> t = Team.find(Team.class, "name = ?", teamName);
			Team team = t.get(0);
			List<Player> p = team.getPlayers();

			String[] playerNameThenNumber = new String[p.size() * 2];

			int i = 0;
			for (Player pp: p) {
				String name = pp.firstName + " " + pp.lastName;
				String number = Integer.toString(pp.number);
				playerNameThenNumber[i] = name; 
				playerNameThenNumber[ i + 1] = number; 
				i += 2;
			}

			TableLayout tableLayout = (TableLayout) v.findViewById(R.id.table_layout);

			// reset tablerows
			tableLayout.removeAllViews();

			TableRow r = new TableRow(v.getContext());
			r.setPadding(0, 0, 0, 18);
			TextView s1 = new TextView(v.getContext());
			TextView s2 = new TextView(v.getContext());
			s1.setText("  Player");
			s1.setTextSize(23);
			s2.setText("#");
			s2.setTextSize(23);
			r.addView(s1);
			r.addView(s2);


			tableLayout.addView(r);

			for (int i1=0; i1 < playerNameThenNumber.length; i1 += 2) {
				
				TableRow row = new TableRow(v.getContext());
				TextView s = new TextView(v.getContext());


				s.setText("   " + playerNameThenNumber[i1] + "                                "); //hacky way to add space
				s.setTextSize(20);

				TextView ss = new TextView(v.getContext());
				ss.setText(playerNameThenNumber[i1+1]);
				ss.setTextSize(20);

				row.addView(s);
				row.addView(ss);
								
				TableRow row2 = new TableRow(v.getContext());
				TextView sss = new TextView(v.getContext());
				sss.setText("");
				row2.addView(sss);
				row2.setBackgroundResource(R.drawable.line);
			
				
				tableLayout.addView(row);
				tableLayout.addView(row2);		
			}
			
			return v;
		}
	}



	// FOR BUTTON
	public void onStatsClick(View v) {

		Intent intent = new Intent(TeamPageActivity.this, TeamPageStatsActivity.class);
		intent.putExtra("team", teamName);
		TeamPageActivity.this.startActivity(intent);
	}



	public static class SFragment extends Fragment implements OnClickListener{

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View v = inflater.inflate(R.layout.myfragment_layout, container, false);

			List<Team> t = Team.find(Team.class, "name = ?", teamName);
			Team team = t.get(0);
			List<Game> games = team.getGames();
			HashMap<String, String> gamess = new HashMap<String, String>();
			
			LinearLayout gamesLayout = (LinearLayout)v.findViewById(R.id.linear_games);
			
			for	(final Game g : games) {
//				gamess.put("vs. " + g.opposingTeamName, g.date + " @ " + g.location);
				
				Button button = new Button(v.getContext());
				button.setText("vs. " + g.opposingTeamName +"\n" + g.date + " @ " + g.location);
				
				button.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent in = new Intent(v.getContext(), GameActivity.class);
						in.putExtra("id", g.getId());
						in.putExtra("teamName",teamName);
//						TextView tView = (TextView) v.findViewById(R.id.team_title);
//						tView.setText(teamName);
						startActivity(in);
					}
				});
				
				button.setTextSize(25);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				params.setMargins(10,10,10,10);
				button.setBackgroundResource(R.drawable.custom_button_confirm);
				button.setLayoutParams(params);
				gamesLayout.addView(button);
			}



			for (String vs : gamess.keySet()) {
//				Button button = new Button(v.getContext());
//				button.setText(vs + "\n" + gamess.get(vs));
//				button.setOnClickListener(this);
//				button.setTextSize(25);
//				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//				params.setMargins(10,10,10,10);
//				button.setBackgroundResource(R.drawable.custom_button_confirm);
//				button.setLayoutParams(params);
//				gamesLayout.addView(button);
			}


			return v;
		}

		@Override
		public void onClick(View arg0) {
//			// TODO Auto-generated method stub
//
//			// intent to game page
//			Button b = (Button) arg0;
//			String bText = b.getText().toString();
//			String[] bArr = bText.split(" ");
//			
//			System.out.println("bArr OUTPUT");
//			System.out.println(bArr[0] + " " + bArr[1] + " " + bArr[2] + " " + bArr[3]);
//			
//			Intent intent = new Intent(arg0.getContext(), GameActivity.class);
//			intent.putExtra("teamName", teamName);
//			intent.putExtra("location", bArr[3]);
//			startActivity(intent);

		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void makeNewTeam(View v) {
		Intent intent = new Intent(v.getContext(), GameSetup.class);
		intent.putExtra("team",teamName);
		startActivity(intent); 

	}


}