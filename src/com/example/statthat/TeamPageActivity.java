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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TeamPageActivity extends FragmentActivity {
	MyPageAdapter pageAdapter;

	// http://architects.dzone.com/articles/android-tutorial-using
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getActionBar().hide();
		getActionBar().setDisplayShowHomeEnabled(false);              
		getActionBar().setDisplayShowTitleEnabled(false);
		setContentView(R.layout.activity_team_page);	

		// get all variables from DB, programmatically set to XML
		// set team name from db 

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
		actionBar.addTab(actionBar.newTab().setText("Reports").setTabListener(tabListener));

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

	private List<Fragment> getFragments(){
		List<Fragment> fList = new ArrayList<Fragment>();

		// instead of hard coding, get players from Players class; will do later
		String[] players = {"Daphne Hsu       05", "Emily Reinhold       22", "Zack Mayeda      41", "Andrew Dorsett       20", "Max Dougherty       13"};

		fList.add(MyFragment.newInstance(players));

		HashMap<String, String> games = new HashMap<String, String>();
		games.put("vs. Stanford", "April 1, 2014 (3:14 pm)");
		games.put("vs. UCLA", "April 15, 2014 (2:18 pm)");
		games.put("vs. MIT", "April 20, 2014 (1:50 pm)");

		fList.add(MyFragment.newInstance(games)); 

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

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.myfragment_layout, container, false);

			if (getArguments().getString(EXTRA_MESSAGE) instanceof String) {
				String message = getArguments().getString(EXTRA_MESSAGE);
				TextView messageTextView = (TextView)v.findViewById(R.id.textView);
				messageTextView.setText(message);

			}
			if (getArguments().getStringArray(EXTRA_MESSAGE) instanceof String[]) {
				String[] message = getArguments().getStringArray(EXTRA_MESSAGE);
				LinearLayout layout = (LinearLayout)v.findViewById(R.id.linear_players);
				for (String i: message) {
					TextView text=new TextView(container.getContext());
					text.setText(i);
					text.setTextSize(20);
					text.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
					layout.addView(text);
				}
			}

			if (getArguments().getSerializable(EXTRA_MESSAGE) instanceof HashMap<?,?>) {

				HashMap<String, String> hash = (HashMap<String, String>) getArguments().getSerializable(EXTRA_MESSAGE);
				LinearLayout gamesLayout = (LinearLayout)v.findViewById(R.id.linear_games);

				for (String vs : hash.keySet()) {
					Button button = new Button(container.getContext());
					button.setText(vs + "\n" + hash.get(vs));
					button.setOnClickListener(this);
					button.setWidth(500);
					//button.bringToFront();

					button.setBackgroundColor(Color.parseColor("#FA6900"));
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
					params.setMargins(10,10,10,10);

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

			Intent intent = new Intent(arg0.getContext(), GameActivity.class);
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
		startActivity(intent); 
		
	}
	

}