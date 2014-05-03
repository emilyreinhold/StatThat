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
	MyPageAdapter pageAdapter;
	String opposingTeamName;
	static String location;
	static String teamName;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		Intent in = getIntent();
		location = in.getStringExtra("location");
		teamName = in.getStringExtra("teamName");
		TextView tView = (TextView) findViewById(R.id.team_title);
		tView.setText(teamName);


		Button b = (Button) findViewById(R.id.button_team);
		b.setPressed(true);

		getActionBar().hide();
		getActionBar().setDisplayShowHomeEnabled(false);              
		getActionBar().setDisplayShowTitleEnabled(false);
		List<Fragment> fragments = getFragments();
		pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
		final ViewPager pager = (ViewPager)findViewById(R.id.game_pager);
		pager.setAdapter(pageAdapter);

		final ActionBar actionBar = getActionBar();

		// Specify that tabs should be displayed in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create a tab listener that is called when the user changes tabs.
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
				// show the given tab
				pager.setCurrentItem(tab.getPosition());
			}

			public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
				// hide the given tab
			}

			public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
				// probably ignore this event
			}
		};


		actionBar.addTab(actionBar.newTab().setText("Team").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Players").setTabListener(tabListener));


		pager.setOnPageChangeListener(
				new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// When swiping between pages, select the
						// corresponding tab.
						getActionBar().setSelectedNavigationItem(position);
					}
				});



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


			List<Game> games = Game.find(Game.class, "location = ?", location);
			Game game = games.get(0);
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

			List<Game> games = Game.find(Game.class, "location = ?", location);
			Game game = games.get(0);

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



			//View v = inflater.inflate(R.layout.myfragment_game_layout, container, false);

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


	// when pressing back button, we still want the button to be highlighted
	//    protected void onResume() {
	//    	super.onResume();
	//    	Button b = (Button) findViewById(R.id.button_team);
	//		b.setPressed(true);
	//    }


	// FOR THE BUTTONS


	public void onPlayersClick(View v) {
		Intent intent = new Intent(GameActivity.this, GamePlayersActivity.class);
		intent.putExtra("location", location);
		intent.putExtra("teamName", teamName);
		GameActivity.this.startActivity(intent);

	}

	HashMap<String, HashMap<String, Integer>> players;

	private List<Fragment> getFragments(){
		List<Fragment> fList = new ArrayList<Fragment>();
		players = new HashMap<String, HashMap<String, Integer>>();

		List<Game> games = Game.find(Game.class, "location = ?", location);
		Game game = games.get(0);

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

		// make team stats
		HashMap<String, Integer> team = new HashMap<String,Integer>();
		ArrayList<Integer> statCounts = t.getBballStatCountsForGame(game);
		ArrayList<String> statNames = StatType.getBballStatTypeNames();
		for (int i = 0; i < statCounts.size(); i++) {
			team.put(statNames.get(i), statCounts.get(i));
		}
		//		team.put("Points Scored", "56");
		//		team.put("Rebounds", "20");
		//		team.put("Fouls", "9");
		//		team.put("Three Pointers", "11");		
		//		team.put("Layups", "15");
		//		team.put("Time-outs", "3");
		//		team.put("Free Throws", "20");
		//		team.put("Assist", "40");
		//		team.put("Steal", "16");
		//		team.put("Block", "20");
		//		team.put("Turnover", "5");

		fList.add(MyFragment.newInstance(team));		
		fList.add(MyFragment.newInstance(players, 0)); 

		return fList;
	}

	public static class MyFragment extends Fragment implements OnClickListener, OnItemSelectedListener{
		public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
		public static final String PLAYERS = "PLAYERS";
		public static final String TEAM = "TEAM";

		View v;
		HashMap<String, HashMap<String,String>> hash;

		// this is for players with stats
		// hacky way to differentiate between team and players
		public final static MyFragment newInstance(HashMap<String,HashMap<String,Integer>> message, int x) {
			MyFragment f = new MyFragment();
			Bundle bdl = new Bundle(message.size());
			bdl.putSerializable(PLAYERS, message);
			f.setArguments(bdl);
			return f;
		}

		// for team
		public final static MyFragment newInstance(HashMap<String,?> message) {
			MyFragment f = new MyFragment();
			Bundle bdl = new Bundle(message.size());
			bdl.putSerializable(TEAM, message);
			f.setArguments(bdl);
			return f;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			v = inflater.inflate(R.layout.myfragment_game_layout, container, false);

			if (getArguments().getSerializable(PLAYERS) instanceof HashMap<?,?>) {
				v = inflater.inflate(R.layout.myfragment_game_viewplayers, container, false);

				hash = (HashMap<String, HashMap<String,String>>) getArguments().getSerializable(PLAYERS);
				LinearLayout gamesLayout = (LinearLayout)v.findViewById(R.id.linear_players); 
				Spinner spinner = (Spinner) v.findViewById(R.id.spinner);

				List l = new ArrayList();
				l.addAll(hash.keySet());
				ArrayAdapter<String> adapter=new ArrayAdapter<String>(v.getContext(),
						android.R.layout.simple_dropdown_item_1line, l);

				adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

				spinner.setAdapter(adapter);
				spinner.setOnItemSelectedListener(this);
			}

			if (getArguments().getSerializable(TEAM) instanceof HashMap<?,?>) {

				v = inflater.inflate(R.layout.myfragment_game_viewteam, container, false);
				HashMap<String, String> hash = (HashMap<String, String>) getArguments().getSerializable(TEAM);
				TableLayout layout = (TableLayout) v.findViewById(R.id.table_layout_team);

				// reset tablerows
				layout.removeAllViews();

				for (String stat: StatType.getBballStatTypeNames()) {
					if (!hash.containsKey(stat)) {
						continue;
					}
					TableRow row = new TableRow(v.getContext());
					TextView s = new TextView(v.getContext());

					s.setText(stat + "        ");
					s.setTextSize(30);

					TextView ss = new TextView(v.getContext());
					ss.setText(String.valueOf(hash.get(stat)));
					ss.setTextSize(30);

					row.addView(s);
					row.addView(ss);
					layout.addView(row);			
				}
			}
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
			((TextView) arg0.getChildAt(0)).setTextSize(25);			
			String s = (String) arg0.getItemAtPosition(arg2);
			TableLayout layout = (TableLayout) v.findViewById(R.id.table_layout);
			// reset tablerows
			layout.removeAllViews();
			for (String stat: StatType.getBballStatTypeNames()) {
				if (!hash.get(s).containsKey(stat)) {
					continue;
				}
				TableRow row = new TableRow(v.getContext());
				TextView t = new TextView(v.getContext());
				t.setText(stat + "        ");
				t.setTextSize(30);

				TextView tt = new TextView(v.getContext());
				tt.setText(String.valueOf(hash.get(s).get(stat)));
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


	class MyPageAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragments;

		public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}
		@Override 
		public Fragment getItem(int position) {
			return this.fragments.get(position);
		}

		@Override
		public int getCount() {
			return this.fragments.size();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}

}
