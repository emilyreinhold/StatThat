package com.example.statthat;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.os.Bundle;
import android.os.SystemClock;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.DigitalClock;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextClock;
import android.widget.TextView;

public class RecordStat extends Activity {
	
	TableLayout stat_table;
	Game game = null;
	private Dialog record_diag;
	
	// View buttons
	private Button done;
	private Button edit;
	private Button next_quarter;
	private Button record;
	private Button start_clock;
	
	// keeping track of stats
	private int current_quarter;
	private long report_time;
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
	    start_clock = (Button) findViewById(R.id.start_clock_button);
	    
	    clock = (Chronometer) findViewById(R.id.clock);
	    stat_table = (TableLayout) findViewById(R.id.recent_stats_table);

	    current_quarter = 1;
		
		// Setup onclick listeners
		setupOnClickListeners();
		
		// Setup Record Dialog
		setupDialog();
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
			    setResult(RESULT_OK, null);
				finish();
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
//				Stat recent_stat = recordStat();
//				stats.add(recent_stat.getId());
//				updateRecentStats(recent_stat);
				if(clock_stopped)
					report_time = -1*stopped_at;
				else
					report_time = SystemClock.elapsedRealtime() - clock.getBase();
				
				record_diag.show();
//				System.out.println("PRINT!");
//
//				System.out.println(record_diag);
				
				
				
			}
			
		});
		

		
		// start_clock - starts the clock
		start_clock.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				if(clock_stopped){
					clock_stopped = false;
					clock.setBase(stopped_at + SystemClock.elapsedRealtime());
					start_clock.setBackgroundResource(R.drawable.pause);
					stopped_at = 0;
					clock.start();
				}else{
					start_clock.setBackgroundResource(R.drawable.play);
					clock_stopped = true;
					stopped_at = clock.getBase() - SystemClock.elapsedRealtime();
					clock.stop();
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
		player.setText(Integer.toString(stat.player.number));
		params = ((TextView) findViewById(R.id.player_header)).getLayoutParams();
		player.setGravity(Gravity.CENTER_HORIZONTAL);
		player.setTextColor(Color.parseColor("#424242"));
		player.setLayoutParams(params);
		row.addView(player);

		
		// stat_type
		TextView stat_type = new TextView(ctx);
		stat_type.setText(stat.statType.name);
		params = ((TextView) findViewById(R.id.stat_type_header)).getLayoutParams();
		stat_type.setGravity(Gravity.CENTER_HORIZONTAL);
		stat_type.setTextColor(Color.parseColor("#424242"));
		stat_type.setLayoutParams(params);
		row.addView(stat_type);
		
		
		// result 
		TextView result = new TextView(ctx);
		result.setText(stat.result ? "HIT" : "MISS");
		params = ((TextView) findViewById(R.id.result_header)).getLayoutParams();
		result.setGravity(Gravity.CENTER_HORIZONTAL);
		result.setTextColor(Color.parseColor("#424242"));
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
		time.setTextColor(Color.parseColor("#424242"));
		
		row.addView(time);
		
		stat_table.addView(row);
		
		
	
	}
	private void setupDialog(){
		

		record_diag = new Dialog(this);
		record_diag.setContentView(R.layout.record_stat_dialog);
		record_diag.setTitle("Record Stat");
		
		List<Player> players = Player.listAll(Player.class);
		List<StatType> stat_types = StatType.listAll(StatType.class);
		
		ArrayList<Integer> player_numbers = new ArrayList<Integer>();
		for(Player player : players){
			player_numbers.add(player.number);
		}
		
		ArrayList<String> stat_type_strings = new ArrayList<String>();
		for(StatType stat_type : stat_types){
			stat_type_strings.add(stat_type.name);
		}
		
		
		
		// Add player selection
		ArrayAdapter<Integer> player_adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, player_numbers);
		final Spinner select_player_spinner = (Spinner) record_diag.findViewById(R.id.select_player);
		select_player_spinner.setAdapter(player_adapter);
		
		// Add stat selection
		ArrayAdapter<String> stat_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, stat_type_strings);
		final Spinner select_stat_spinner = (Spinner) record_diag.findViewById(R.id.select_stat);
		select_stat_spinner.setAdapter(stat_adapter);
		
		// Add result selection
		ArrayList<String> result = new ArrayList<String>(Arrays.asList("HIT", "MISS"));
		ArrayAdapter<String> result_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, result);
		final Spinner select_result_spinner = (Spinner) record_diag.findViewById(R.id.select_result);
		select_result_spinner.setAdapter(result_adapter);
		
		
		// Add confirm listen
		Button confirm = (Button) record_diag.findViewById(R.id.confirm);
		confirm.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Stat stat = new Stat(getApplicationContext());
				stat.player = Player.find(Player.class, "number=?", select_player_spinner.getSelectedItem().toString()).get(0);
				stat.statType = StatType.find(StatType.class, "name=?",select_stat_spinner.getSelectedItem().toString()).get(0);
				stat.result = select_result_spinner.getSelectedItem().toString() == "HIT";
				stat.time = report_time;
				stat.save();
				
				updateRecentStats(stat);
				record_diag.dismiss();
				
			}
			
		});
		
		
	}
	
}
