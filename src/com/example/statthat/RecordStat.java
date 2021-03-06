package com.example.statthat;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class RecordStat extends Activity {
	
	TableLayout stat_table;
	Game game = null;
	
	// Recording Dialog
	private Dialog record_diag;
	private RecordAction record_stat;
	private Parser parser;
	
	// Edit Dialog
	private Dialog edit_diag;
	private Button edit_confirm_button;
	
	// View buttons
	private Button done;
	private Button edit;
	private Button next_quarter;
	private Button record;
	private Button start_clock;
	
	// keeping track of stats
	private int current_quarter;
	private double report_time;
	private long stopped_at = 0;
	private boolean clock_stopped = true;
	ArrayList<Long> stats = new ArrayList<Long>();

	// edit spinners
	Spinner player_spinner;
	ArrayAdapter<String> player_spinnerArrayAdapter;
	Spinner result_spinner;
	ArrayAdapter<String> result_spinnerArrayAdapter;
	Spinner stat_spinner;
	ArrayAdapter<String> stat_spinnerArrayAdapter;
	
	// clock
	private Chronometer clock;

	int row_id = 1;
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
		
		// setup recording stat
		record_stat = new RecordAction();
		parser = new Parser(game, getApplicationContext());
		
		// Set variables
	    done = (Button) findViewById(R.id.done_button);
	    edit = (Button) findViewById(R.id.edit_stat_button);
	    next_quarter = (Button) findViewById(R.id.next_quarter_button);
	    record = (Button) findViewById(R.id.record_button);
	    start_clock = (Button) findViewById(R.id.start_clock_button);
	    
	    clock = (Chronometer) findViewById(R.id.clock);
	    stat_table = (TableLayout) findViewById(R.id.recent_stats_table);
	    stat_table.setColumnCollapsed(0, true);

	    current_quarter = 1;
		
		// Setup onclick listeners
		setupOnClickListeners();
		
		// Setup Record Dialog
		setupDialog();
		setupSpinner();
		fakeStats();
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
		
		
		edit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				undoLastStat();
				editStats();
			}
		});
				
		// next_quarter - increment quarter by 1
		next_quarter.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				current_quarter++;	
			}
			
		});
		
		// record stat
		record.setOnTouchListener(new Button.OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 switch (event.getAction() & MotionEvent.ACTION_MASK) {

				    case MotionEvent.ACTION_DOWN:
						if(clock_stopped)
							report_time = -1*stopped_at;
						else
							report_time = SystemClock.elapsedRealtime() - clock.getBase();
					
						record_diag.show();
						record_stat.startListening();
				        break;
				    case MotionEvent.ACTION_UP:

				    	record_diag.dismiss();
				    	record_stat.stopListening();
				    	break;
				    case MotionEvent.ACTION_OUTSIDE:
				        break;
				    case MotionEvent.ACTION_POINTER_DOWN:
				        break;
				    case MotionEvent.ACTION_POINTER_UP:
				        break;
				    case MotionEvent.ACTION_MOVE:
				        break;
				    }
				
				return false;
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
		
		Stat stat = new Stat(ctx, new Player(ctx), game, new StatType(ctx), time , current_quarter, true, null);
		
		return stat;
	}
	
	private void undoLastStat() {
		int children = stat_table.getChildCount();
		if(children >= 2) {
			stat_table.removeViewAt(children - 1);
		}
	}
	
	private void editStats(){
		if(stat_table.isColumnCollapsed(0))
			stat_table.setColumnCollapsed(0,false);
		else
			stat_table.setColumnCollapsed(0,true);

	}
	
	private void updateRecentStats(final Stat stat){
		Context ctx = getApplicationContext();
		TableRow row = new TableRow(ctx);
		row.setId(row_id);
		final Integer current_row_id = row_id;
		row_id++;
		
		LayoutParams params;
		
		Button edit_button = new Button(ctx);
		edit_button.setBackgroundResource(R.drawable.circlethree);
//		edit_button.setLayoutParams(new LinearLayout.LayoutParams(20, 20));
		edit_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Stat temp_stat = Stat.findById(Stat.class, stat.getId());
				int pos = player_spinnerArrayAdapter.getPosition(Integer.toString(temp_stat.player.number));
				player_spinner.setSelection(pos);
				
				String result = temp_stat.result ? "MADE" : "MISS";
				pos = result_spinnerArrayAdapter.getPosition(result);
				result_spinner.setSelection(pos);
				
				pos = stat_spinnerArrayAdapter.getPosition(stat.statType.name);
				stat_spinner.setSelection(pos);
				
				edit_confirm_button.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						editUpdate(player_spinner.getSelectedItem().toString(), result_spinner.getSelectedItem().toString(), 
									stat_spinner.getSelectedItem().toString(), stat, current_row_id);
						
						edit_diag.hide();
					}
				});
				
				
				edit_diag.show();
			}
		});
		row.addView(edit_button);
		
		
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
		result.setText(stat.result ? "MADE" : "MISS");
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
		
		TextView flag = new TextView(ctx);
		if (stat.flag == null) {
			flag.setText("good");
		} else {
			flag.setText("!");
		}
		// params = ((TextView) findViewById(R.id.flag_header)).getLayoutParams();
		// flag.setLayoutParams(params);
		flag.setGravity(Gravity.CENTER_HORIZONTAL);
		flag.setTextColor(Color.parseColor("#424242"));
		
		// attach listener to flag textbox that opens manual edit dialog
		
		// row.addView(flag);
		
		stat_table.addView(row);
		
		
	
	}
	
	private void setupDialog(){
		
		//record_diag = new Dialog(this);
		//record_diag.setContentView(R.layout.activity_record_action);
		//record_diag.setTitle("Record a Stat");
		String msg = "\t\t\t\t\t\t\t\tex: \"player one made free throw\"\n\n";
		msg += "Available stat types:\n";
		for (String s : DBHelper.bballStatTypes) {
			msg += "\t" + s + "\n";
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg).setTitle("Record a stat now");
		builder.setIcon(android.R.drawable.ic_btn_speak_now);
		record_diag = builder.create();

	}
	
	private void setupSpinner(){
		LayoutInflater factory = getLayoutInflater();

		View record_stat_diag = factory.inflate(R.layout.record_stat_dialog, null);
		
		edit_confirm_button = (Button) record_stat_diag.findViewById(R.id.edit_confirm);
		
		edit_diag = new Dialog(RecordStat.this);
		edit_diag.setContentView(record_stat_diag);
		edit_diag.setTitle("Edit Stat");

		player_spinner = (Spinner) record_stat_diag.findViewById(R.id.select_player);
		result_spinner = (Spinner) record_stat_diag.findViewById(R.id.select_result);
		stat_spinner = (Spinner) record_stat_diag.findViewById(R.id.select_stat);
		
		
		ArrayList<String> numbers = new ArrayList<String>();
		ArrayList<String> statTypes = new ArrayList<String>();
		ArrayList<String> results = new ArrayList<String>();
		results.add("MADE");
		results.add("MISS");
		
		for (Player player : Player.listAll(Player.class)){
			numbers.add(Integer.toString(player.number));
		}
		
		for (StatType statType : StatType.listAll(StatType.class)){
			statTypes.add(statType.name);
		}

		player_spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, numbers);
		player_spinner.setAdapter(player_spinnerArrayAdapter);	
		
		
		result_spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, results);
		result_spinner.setAdapter(result_spinnerArrayAdapter);	
		
		stat_spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, statTypes);
		stat_spinner.setAdapter(stat_spinnerArrayAdapter);	

	}

	private void editUpdate(String number, String result, String statType, Stat stat, Integer row_id){
		// Update Stat
		stat.player.number = Integer.parseInt(number);
		stat.result = result.equals("MADE");
		stat.statType = StatType.find(StatType.class, "name=?", statType).get(0);
		stat.save();
		
		// Update Row
		TableRow row = (TableRow) findViewById(row_id);
		TextView row_number = (TextView) row.getChildAt(1);
		TextView row_stat = (TextView) row.getChildAt(2);
		TextView row_result = (TextView) row.getChildAt(3);
		
		row_number.setText(number);
		row_stat.setText(statType);
		row_result.setText(result);
		
		
	}
	
	private class RecordAction {
		private SpeechRecognizer mSpeechRecognizer;
		private Intent mSpeechRecognizerIntent; 
		private boolean mIsListening; 
		private SpeechRecognitionListener listener;
		
		//
		public RecordAction(){
			mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
			listener = new SpeechRecognitionListener();
			
		    mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		    mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
		                                     RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		    mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
		                                     getApplicationContext().getPackageName());
		    
		    mSpeechRecognizer.setRecognitionListener(listener);
		
		}
		
		public void startListening(){
			
		    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
		}
		
		public void stopListening(){
			mSpeechRecognizer.stopListening();
		
		}
		
		protected class SpeechRecognitionListener implements RecognitionListener
		{
			
		    @Override
		    public void onBeginningOfSpeech()
		    {          

		    }

		    @Override
		    public void onBufferReceived(byte[] buffer)
		    {

		    }

		    @Override
		    public void onEndOfSpeech()
		    {
		    	
		    }

		    @Override
		    public void onError(int error)
		    {
//		         mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
				int duration = Toast.LENGTH_SHORT;
				String msg = "Error";
				Toast toast = Toast.makeText(getApplicationContext(), msg, duration);
				toast.show();

		    }

		    @Override
		    public void onEvent(int eventType, Bundle params)
		    {
				int duration = Toast.LENGTH_SHORT;
				String msg = "Event";
				Toast toast = Toast.makeText(getApplicationContext(), msg, duration);
				toast.show();
		    }

		    @Override
		    public void onPartialResults(Bundle partialResults)
		    {

		    }

		    @Override
		    public void onReadyForSpeech(Bundle params)
		    {

		    }

		    @Override
		    public void onResults(Bundle results)
		    {
				
		    	// TODO call speech parser class
				String result = null;
				int stat_id = 0;
				
				if(results != null && results.containsKey(SpeechRecognizer.RESULTS_RECOGNITION)){
					result = parser.parseMatch(results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION), report_time, current_quarter, game.team);
//					result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
				}
				
				
				// Try to convert the result into an id
				try{
					stat_id = Integer.parseInt(result);
					Stat stat = Stat.findById(Stat.class, (long)stat_id);
					updateRecentStats(stat);
//					
//					int duration = Toast.LENGTH_LONG;
//					Toast toast = Toast.makeText(getApplicationContext(), result, duration);
//					toast.show();
				
				}catch(Exception e){
					// DEV - if not an int display bad results
					int duration = Toast.LENGTH_LONG;
					String error_msg = "Error parsing stat. Review flagged stat to correct.";
					Toast toast = Toast.makeText(getApplicationContext(), error_msg, duration);
					toast.show();
				}
				

		    }

		    @Override
		    public void onRmsChanged(float rmsdB)
		    {

		    }

		}
		
	}
	
	private void fakeStats(){
		List<Player> players = game.team.getPlayers();
		int player_count = 0;
		double time = 1000;

		for(Player player : players){
			List<Stat> player_stats = Stat.find(Stat.class, "player = ?", player.getId().toString());
			if (player_stats.size() > 0){
				int count = 0;
				
				for(Stat stat : player_stats){
					if (count > 2)
						break;
					time += 10000*Math.random();
					stat.time = time;
					updateRecentStats(stat);
					count++;
				}
			}
			if(player_count > 2)
				return;
			
			player_count++;
			
		}
	}

	
	
}
