package com.example.statthat;


import java.util.ArrayList;

import android.os.Bundle;
import android.os.SystemClock;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
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
	ArrayList<Long> stats = new ArrayList<Long>();

	
	// clock
	private Chronometer clock;

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
				stopped_at = clock.getBase() - SystemClock.elapsedRealtime();
				clock.stop();
			}
			
		});
		
		// start_clock - starts the clock
		start_clock.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				clock.setBase(stopped_at + SystemClock.elapsedRealtime());
				clock.start();
			}
		
		});
		
		
	}
	

		
		
	private Stat recordStat(){
		Context ctx = getApplicationContext();
		Stat stat = new Stat(ctx, new Player(ctx), game, new StatType(ctx), (double)SystemClock.elapsedRealtime() - clock.getBase(),"1", true);
		
		return stat;
	}
	
	private void updateRecentStats(Stat stat){
		Context ctx = getApplicationContext();
		TableRow row = new TableRow(ctx);
		
		// time
		TextView time = new TextView(ctx);
		time.setText("0");
		row.addView(time);
		
		// player number
		TextView player = new TextView(ctx);
		player.setText("5");
		row.addView(player);
		
		// result 
		TextView result = new TextView(ctx);
		result.setText("MISS");
		row.addView(result);
		
		stat_table.addView(row);
		
		
	
	}

}
