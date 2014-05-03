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
public class TeamPageActivity extends FragmentActivity { // fragmentactivity
	MyPageAdapter pageAdapter;
	public static String teamName;
	// http://architects.dzone.com/articles/android-tutorial-using
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

		List<Fragment> fragments = getFragments();
		pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
		final ViewPager pager = (ViewPager)findViewById(R.id.pager);
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

		actionBar.addTab(actionBar.newTab().setText("Players").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Stats").setTabListener(tabListener));

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

//				TableRow row = new TableRow(v.getContext());
//				TextView s = new TextView(v.getContext());
//
//
//				s.setText(playerNameThenNumber[i1] + "       "); //hacky way to add space
//				s.setTextSize(20);
//
//				TextView ss = new TextView(v.getContext());
//				ss.setText(playerNameThenNumber[i1+1]);
//				ss.setTextSize(20);
//
//				row.addView(s);
//				row.addView(ss);
//				
//				tableLayout.addView(row);
				
				
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
				
			
				
				
//				ShapeDrawable border = new ShapeDrawable(new RectShape());
//				
//				border.getPaint().setStyle(Style.STROKE);
//				border.getPaint().setColor(Color.BLACK);
//				
//				
//				
//				TableRow row = new TableRow(v.getContext());
//				TextView s = new TextView(v.getContext());
//
//
//				s.setText(playerNameThenNumber[i1] + "       "); //hacky way to add space
//				s.setPadding(8, 5, 8, 5);
//				s.setTextSize(20);
//				
//
//				TextView ss = new TextView(v.getContext());
//				ss.setPadding(8, 5, 8, 5);
//				ss.setText(playerNameThenNumber[i1+1]);
//				ss.setTextSize(20);
//				
//				s.setBackground(border);
//				ss.setBackground(border);
//
//				row.addView(s);
//				row.addView(ss);
//		
//				//row.setBackground(border);
//				
//				tableLayout.addView(row);
				
				
				
				
				
				
				
//				TableRow tr = new TableRow(v.getContext());
//				tr.setBackgroundColor(Color.BLACK);
//				tr.setPadding(0, 0, 0, 2); //Border between rows
//			
//				
//				TableRow.LayoutParams llp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//				//llp.setMargins(0, 0, 2, 0);//2px right-margin
//
//				//New Cell
//				LinearLayout cell = new LinearLayout(v.getContext());
//				cell.setBackgroundColor(Color.WHITE);
//				cell.setLayoutParams(llp);//2px border on the right for the cell
//
//
//				TextView tv = new TextView(v.getContext());
//				tv.setText("Some Text");
//				tv.setPadding(0, 0, 4, 3);
//
//				cell.addView(tv);
//				tr.addView(cell);
//				//add as many cells you want to a row, using the same approach
//
//				tableLayout.addView(tr);
				
				
				
				
				

			}
			
			return v;

		}
	}


	// when pressing back button, we still want the button to be highlighted
	//	protected void onResume() {
	//		super.onResume();
	//		Button b = (Button) findViewById(R.id.button_team_page);
	//		b.setPressed(true);
	//	}

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
			for	(Game g : games) {
				gamess.put("vs. " + g.opposingTeamName, g.date + " @ " + g.location);
			}


			LinearLayout gamesLayout = (LinearLayout)v.findViewById(R.id.linear_games);

			for (String vs : gamess.keySet()) {
				Button button = new Button(v.getContext());
				button.setText(vs + "\n" + gamess.get(vs));
				button.setOnClickListener(this);
				button.setTextSize(25);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				params.setMargins(10,10,10,10);
				button.setBackgroundResource(R.drawable.custom_button_confirm);
				button.setLayoutParams(params);
				gamesLayout.addView(button);
			}


			return v;
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

	private List<Fragment> getFragments(){
		List<Fragment> fList = new ArrayList<Fragment>();

		// instead of hard coding, get players from Players class; will do later

		List<Team> t = Team.find(Team.class, "name = ?", teamName);
		Team team = t.get(0);
		List<Player> p = team.getPlayers();


		fList.add(MyFragment.newInstance(p));

		List<Game> games = team.getGames();
		HashMap<String, String> gamess = new HashMap<String, String>();

		for (Game g : games) {
			gamess.put("vs. " + g.opposingTeamName, g.date + " @ " + g.location);
		}

		fList.add(MyFragment.newInstance(gamess)); 

		return fList;
	}

	public static class MyFragment extends Fragment implements OnClickListener{
		public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

		public final static MyFragment newInstance(String message)
		{
			MyFragment f = new MyFragment();
			Bundle bdl = new Bundle(1);
			bdl.putString(EXTRA_MESSAGE, message);
			f.setArguments(bdl);
			return f;
		}

		public final static MyFragment newInstance(HashMap<String,String> message) {
			MyFragment f = new MyFragment();
			Bundle bdl = new Bundle(message.size());

			bdl.putSerializable(EXTRA_MESSAGE, message);
			f.setArguments(bdl);
			return f;
		}


		public final static MyFragment newInstance(String[] message)
		{
			MyFragment f = new MyFragment();
			Bundle bdl = new Bundle(message.length);
			bdl.putStringArray(EXTRA_MESSAGE, message);
			f.setArguments(bdl);
			return f;
		}

		public final static MyFragment newInstance(List<Player> message)
		{
			MyFragment f = new MyFragment();
			Bundle bdl = new Bundle(message.size());
			String[] playerNameThenNumber = new String[message.size() * 2];

			int i = 0;
			for (Player p: message) {
				String name = p.firstName + " " + p.lastName;
				String number = Integer.toString(p.number);
				playerNameThenNumber[i] = name; 
				playerNameThenNumber[ i + 1] = number; 
				i += 2;

			}

			bdl.putStringArray(EXTRA_MESSAGE, playerNameThenNumber);
			f.setArguments(bdl);
			return f;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.myfragment_layout, container, false);

			if (getArguments().getString(EXTRA_MESSAGE) instanceof String) {
				String message = getArguments().getString(EXTRA_MESSAGE);
				TextView messageTextView = (TextView)v.findViewById(R.id.textView);
				messageTextView.setText(message);

			}

			// for player and number
			if (getArguments().getStringArray(EXTRA_MESSAGE) instanceof String[]) {
				String[] message = getArguments().getStringArray(EXTRA_MESSAGE);

				TableLayout tableLayout = (TableLayout) v.findViewById(R.id.table_layout);

				// reset tablerows
				tableLayout.removeAllViews();

				TableRow r = new TableRow(v.getContext());
				r.setPadding(0, 0, 0, 18);
				TextView s1 = new TextView(v.getContext());
				TextView s2 = new TextView(v.getContext());
				s1.setText("Player");
				s1.setTextSize(23);
				s2.setText("#");
				s2.setTextSize(23);
				r.addView(s1);
				r.addView(s2);

				// add line
				//				View view = new View(v.getContext());
				//				view.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 1));
				//				view.setBackgroundColor(Color.rgb(51, 51, 51));

				tableLayout.addView(r);
				//tableLayout.addView(view);

				for (int i=0; i < message.length; i += 2) {

					TableRow row = new TableRow(v.getContext());
					TextView s = new TextView(v.getContext());


					s.setText(message[i] + "       "); //hacky way to add space
					s.setTextSize(20);

					TextView ss = new TextView(v.getContext());
					ss.setText(message[i+1]);
					ss.setTextSize(20);

					row.addView(s);
					row.addView(ss);
					tableLayout.addView(row);	
				}
			}

			if (getArguments().getSerializable(EXTRA_MESSAGE) instanceof HashMap<?,?>) {

				HashMap<String, String> hash = (HashMap<String, String>) getArguments().getSerializable(EXTRA_MESSAGE);
				LinearLayout gamesLayout = (LinearLayout)v.findViewById(R.id.linear_games);

				for (String vs : hash.keySet()) {
					Button button = new Button(container.getContext());
					button.setText(vs + "\n" + hash.get(vs));
					button.setOnClickListener(this);
					button.setTextSize(25);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
					params.setMargins(10,10,10,10);
					button.setBackgroundResource(R.drawable.custom_button_confirm);
					button.setLayoutParams(params);
					gamesLayout.addView(button);
				}
			}
			return v;
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
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void makeNewTeam(View v) {
		Intent intent = new Intent(v.getContext(), GameSetup.class);
		intent.putExtra("team",teamName);
		startActivity(intent); 

	}


}