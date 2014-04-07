package com.example.statthat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
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

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_game);

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


	}

	HashMap<String, HashMap<String, String>> players;

	private List<Fragment> getFragments(){
		List<Fragment> fList = new ArrayList<Fragment>();

		// instead of hard coding, get stats from Game class; will do later

		players = new HashMap<String, HashMap<String, String>>();

		HashMap<String, String> a_stats = new HashMap<String, String>();
		a_stats.put("Free Throws", "2");
		a_stats.put("Points Scored", "13");
		a_stats.put("Fouls", "3");
		a_stats.put("Three Pointers", "2");
		a_stats.put("Rebounds", "1");
		a_stats.put("Layups", "4");

		HashMap<String, String> d_stats = new HashMap<String, String>();
		d_stats.put("Free Throws", "1");
		d_stats.put("Points Scored", "10");
		d_stats.put("Fouls", "1");
		d_stats.put("Three Pointers", "3");
		d_stats.put("Rebounds", "2");
		d_stats.put("Layups", "4");

		HashMap<String, String> e_stats = new HashMap<String, String>();
		e_stats.put("Free Throws", "4");
		e_stats.put("Points Scored", "15");
		e_stats.put("Fouls", "2");
		e_stats.put("Three Pointers", "2");
		e_stats.put("Rebounds", "5");
		e_stats.put("Layups", "4");


		players.put("Andrew Dorsett", a_stats);
		players.put("Daphne Hsu", d_stats);
		players.put("Emily Reinhold", e_stats);
		// add players stats

		// make team stats
		HashMap<String,String> team = new HashMap<String,String>();
		team.put("Points Scored", "56");
		team.put("Rebounds", "20");
		team.put("Fouls", "9");
		team.put("Three Pointers", "11");		
		team.put("Layups", "15");
		team.put("Time-outs", "3");
		team.put("Free Throws", "20");

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
		public final static MyFragment newInstance(HashMap<String,HashMap<String,String>> message, int x) {
			MyFragment f = new MyFragment();
			Bundle bdl = new Bundle(message.size());
			bdl.putSerializable(PLAYERS, message);
			f.setArguments(bdl);
			return f;
		}

		// for team
		public final static MyFragment newInstance(HashMap<String,String> message) {
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

				for (String stat: hash.keySet()) {
					TableRow row = new TableRow(v.getContext());
					TextView s = new TextView(v.getContext());

					s.setText(stat + "        ");
					s.setTextSize(20);

					TextView ss = new TextView(v.getContext());
					ss.setText(hash.get(stat));
					ss.setTextSize(20);

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
			String s = (String) arg0.getItemAtPosition(arg2);
			TableLayout layout = (TableLayout) v.findViewById(R.id.table_layout);
			// reset tablerows
			layout.removeAllViews();
			for (String stat: hash.get(s).keySet()) {
				TableRow row = new TableRow(v.getContext());
				TextView t = new TextView(v.getContext());
				t.setText(stat + "        ");
				t.setTextSize(20);

				TextView tt = new TextView(v.getContext());
				tt.setText(hash.get(s).get(stat));
				tt.setTextSize(20);

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
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}

}
