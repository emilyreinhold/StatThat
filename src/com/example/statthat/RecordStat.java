package com.example.statthat;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import android.os.Bundle;
import android.os.SystemClock;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.DigitalClock;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextClock;
import android.widget.TextView;

public class RecordStat extends Activity {
	
	TableLayout stat_table;
	Game game = null;
	
	// View buttons
	private Button done;
	private Button edit;
	private Button next_quarter;
	private Button record;
	private Button stop_clock;
	private Button start_clock;
	
	// keeping track of stats
	private int current_quarter;
	private long stopped_at = 0;
	private boolean clock_stopped = true;
	ArrayList<Long> stats = new ArrayList<Long>();

	
	// clock
	private Chronometer clock;

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_record_stat);
		
		// get game id
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			game = Game.findById(Game.class, extras.getLong("game_id"));
		}else{
			game = Game.findById(Game.class, (long)1);
		}
		
		// Set variables
	    done = (Button) findViewById(R.id.done_button);
	    edit = (Button) findViewById(R.id.edit_game_button);
	    next_quarter = (Button) findViewById(R.id.next_quarter_button);
	    record = (Button) findViewById(R.id.record_button);
	    stop_clock = (Button) findViewById(R.id.stop_clock_button);
	    start_clock = (Button) findViewById(R.id.start_clock_button);
	    
	    clock = (Chronometer) findViewById(R.id.clock);
	    stat_table = (TableLayout) findViewById(R.id.recent_stats_table);

	    current_quarter = 1;
		
		// Setup onclick listeners
		setupOnClickListeners();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.record_stat, menu);
		

		return true;
	}
	
	private void setupOnClickListeners(){
		// done - return to team page
		done.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// some sort of intent
				Intent team_page = new Intent(RecordStat.this, TeamPageActivity.class);
				startActivity(team_page);
			}
			
		});
		
		// edit - do we want to edit right now?
		
		// next_quarter - increment quarter by 1
		next_quarter.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				current_quarter++;	
			}
			
		});
		
		// record stat
		record.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				Stat recent_stat = recordStat();
				stats.add(recent_stat.getId());
				updateRecentStats(recent_stat);
				
				
				
			}
			
		});
		
		// stop_clock - stops the clock
		stop_clock.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				if(!clock_stopped){
					clock_stopped = true;
					stopped_at = clock.getBase() - SystemClock.elapsedRealtime();
					clock.stop();
				}
			}
			
		});
		
		// start_clock - starts the clock
		start_clock.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				if(clock_stopped){
					clock_stopped = false;
					clock.setBase(stopped_at + SystemClock.elapsedRealtime());
					stopped_at = 0;
					clock.start();
				}
			}
		
		});
		
		
	}
	

		
		
	private Stat recordStat(){
		Context ctx = getApplicationContext();
		double time;
		
		// get time according to clock
		if(clock_stopped)
			// stopped_at is offset for base, need to make it a positive
			time = -1 * stopped_at;
		else
			time = SystemClock.elapsedRealtime() - clock.getBase();
		
		Stat stat = new Stat(ctx, new Player(ctx), game, new StatType(ctx), time , current_quarter, true);
		
		return stat;
	}
	
	private void updateRecentStats(Stat stat){
		Context ctx = getApplicationContext();
		TableRow row = new TableRow(ctx);
		LayoutParams params;
		
		
		// player number
		TextView player = new TextView(ctx);
		player.setText("5");
		params = ((TextView) findViewById(R.id.player_header)).getLayoutParams();
		player.setGravity(Gravity.CENTER_HORIZONTAL);
		player.setLayoutParams(params);
		row.addView(player);

		
		// stat_type
		TextView stat_type = new TextView(ctx);
		stat_type.setText("Free Throw");
		params = ((TextView) findViewById(R.id.stat_type_header)).getLayoutParams();
		stat_type.setGravity(Gravity.CENTER_HORIZONTAL);
		stat_type.setLayoutParams(params);
		row.addView(stat_type);
		
		
		// result 
		TextView result = new TextView(ctx);
		result.setText("MISS");
		params = ((TextView) findViewById(R.id.result_header)).getLayoutParams();
		result.setGravity(Gravity.CENTER_HORIZONTAL);
		result.setLayoutParams(params);
		row.addView(result);
		
		
		
		// time
		TextView time = new TextView(ctx);
		
		//convert stat time to HH:MM:SS format
		String time_str = String.format("%02d:%02d:%02d", 
				TimeUnit.MILLISECONDS.toHours((long)stat.time),
				TimeUnit.MILLISECONDS.toMinutes((long) stat.time) -  
				TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours((long)stat.time)), // The change is in this line
				TimeUnit.MILLISECONDS.toSeconds((long)stat.time) - 
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)stat.time)));  
		
		time.setText(time_str);

		params = ((TextView) findViewById(R.id.time_header)).getLayoutParams();
		time.setLayoutParams(params);
		time.setGravity(Gravity.CENTER_HORIZONTAL);
		row.addView(time);
		
		stat_table.addView(row);
		
		
	
	}

}
