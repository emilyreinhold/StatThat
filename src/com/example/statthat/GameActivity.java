package com.example.statthat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class GameActivity extends FragmentActivity {
	String opposingTeamName;
	static String location;
	static String teamName;
	static Long id;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		
		Intent in = getIntent();
//		location = in.getStringExtra("location");
		id = in.getLongExtra("id", 0);

		teamName = in.getStringExtra("teamName");
		TextView tView = (TextView) findViewById(R.id.team_title);
		tView.setText(teamName);

		getActionBar().hide();
		getActionBar().setDisplayShowHomeEnabled(false);              
		getActionBar().setDisplayShowTitleEnabled(false);

		
		FragmentTabHost tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		tabHost.setup(this, getSupportFragmentManager(), R.id.tab_1);

		tabHost.addTab(tabHost.newTabSpec("team").setIndicator("Team Stats"),
				TFragment.class, null);

		tabHost.addTab(tabHost.newTabSpec("players").setIndicator("Players Stats"),
				PFragment.class, null);
	}



	public static class TFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			HashMap<String, Integer> team = new HashMap<String,Integer>();


//			List<Game> games = Game.find(Game.class, "location = ?", location);
			Game game = Game.findById(Game.class, id);

//			Game game = games.get(0);
			ArrayList<Integer> statCounts = game.team.getBballStatCountsForGame(game);
			ArrayList<String> statNames = StatType.getBballStatTypeNames();
			for (int i = 0; i < statCounts.size(); i++) {
				team.put(statNames.get(i), statCounts.get(i));
			}

			View v = inflater.inflate(R.layout.myfragment_game_viewteam, container, false);
			TableLayout layout = (TableLayout) v.findViewById(R.id.table_layout_team);

			// reset tablerows
			layout.removeAllViews();

			for (String stat: StatType.getBballStatTypeNames()) {
				if (!team.containsKey(stat)) {
					continue;
				}
				TableRow row = new TableRow(v.getContext());
				TextView s = new TextView(v.getContext());

				s.setText(stat + "        ");
				s.setTextSize(25);

				TextView ss = new TextView(v.getContext());
				ss.setText(String.valueOf(team.get(stat)));
				ss.setTextSize(25);
				
				
				TableRow row2 = new TableRow(v.getContext());
				TextView sss = new TextView(v.getContext());
				sss.setText("");
				row2.addView(sss);
				row2.setBackgroundResource(R.drawable.line);

				row.addView(s);
				row.addView(ss);
				layout.addView(row);			
				layout.addView(row2);
			}


			return v;
		}
	}

	public static class PFragment extends Fragment implements OnClickListener, OnItemSelectedListener{

		HashMap<String, HashMap<String, Integer>> players;
		View v;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			players = new HashMap<String, HashMap<String, Integer>>();

//			List<Game> games = Game.find(Game.class, "location = ?", location);
//			Game game = games.get(0);

			Game game = Game.findById(Game.class, id);
		
			
			Team t = Team.find(Team.class, "name = ?", teamName).get(0);
			
			List<Player> plays = t.getPlayers();

			for (Player p: plays) {
				HashMap<String, Integer> h = new HashMap<String, Integer>();
				ArrayList<Integer> statCounts = p.getBballStatCountsForGame(game);
				ArrayList<String> statNames = StatType.getBballStatTypeNames();
				for (int i = 0; i < statCounts.size(); i++) {
					h.put(statNames.get(i), statCounts.get(i));
				}
				players.put(p.firstName + " " + p.lastName, h);
			}

			v = inflater.inflate(R.layout.myfragment_game_viewplayers, container, false);

			LinearLayout gamesLayout = (LinearLayout)v.findViewById(R.id.linear_players); 
			Spinner spinner = (Spinner) v.findViewById(R.id.spinner);

			List l = new ArrayList();
			l.addAll(players.keySet());
			ArrayAdapter<String> adapter=new ArrayAdapter<String>(v.getContext(),
					android.R.layout.simple_dropdown_item_1line, l);

			adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

			spinner.setAdapter(adapter);
			spinner.setOnItemSelectedListener(this);

			return v;

		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub


		}

		/*
		 * For spinner
		 * 
		 */
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			// make spinner text bigger
			if (arg0 != null && arg0.getChildAt(0) != null) {
				((TextView) arg0.getChildAt(0)).setTextSize(25);			
				String s = (String) arg0.getItemAtPosition(arg2);
				TableLayout layout = (TableLayout) v.findViewById(R.id.table_layout);
				// reset tablerows
				layout.removeAllViews();
				for (String stat: StatType.getBballStatTypeNames()) {
										TableRow row = new TableRow(v.getContext());
					TextView t = new TextView(v.getContext());
					t.setText(stat + "        ");
					t.setTextSize(25);

					TextView tt = new TextView(v.getContext());
					tt.setText(String.valueOf(players.get(s).get(stat)));
					tt.setTextSize(25);

					row.addView(t);
					row.addView(tt);
					
					
					TableRow row2 = new TableRow(v.getContext());
					TextView sss = new TextView(v.getContext());
					sss.setText("");
					row2.addView(sss);
					row2.setBackgroundResource(R.drawable.line);
					
					
					
					layout.addView(row);
					layout.addView(row2);
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub


		}
	}




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}

}
