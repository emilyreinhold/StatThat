package com.example.statthat;


import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CalendarView;
import android.widget.DatePicker;

public class GameSetup extends Activity {
	private String error =""; 
	
	private EditText date;
	private EditText location;
	private EditText opponent;
		
	private Team team = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_game_setup);
				
		// Get team passed from intent
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			Long team_id = extras.getLong("team");
			team = Team.findById(Team.class, team_id);
		}else{
			// for testing
			// TODO delete for production
			team = Team.findById(Team.class, (long)1);
		}
		
		location = (EditText) findViewById(R.id.game_location);
		opponent = (EditText) findViewById(R.id.game_opponent);
		
		// Date dialog and selection
		date = (EditText) findViewById(R.id.game_date);
		Button datePicker = (Button) findViewById(R.id.date_picker);
		
		datePicker.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				showDialog(1);
			}
		});
	
		
		//Submit form
		Button confirm = (Button) findViewById(R.id.confirm);
		confirm.setOnClickListener(new Button.OnClickListener(){  

			@Override
			public void onClick(View v) {
				if(validateFields()){
					
					Game game = new Game(
							getApplicationContext(),
							team,
							opponent.getText().toString(),
							location.getText().toString(), 
							date.getText().toString() );
					
					game.save();
					Intent record_start = new Intent(GameSetup.this, RecordStat.class);
					record_start.putExtra("game_id", game.getId());
					startActivityForResult(record_start, 1);
					
					
				}else{
					// Error message if fields are not filled in
					int duration = Toast.LENGTH_SHORT;

					Toast toast = Toast.makeText(getApplicationContext(), error, duration);
					toast.show();
				}
			};
         });
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_setup, menu);
		return true;
	}
	
	// Check to see if required fields are populated
	private boolean validateFields(){
		error = "";
		
		if (location.getText().toString().trim().isEmpty())
			error += "You must include a location. ";
		if (date.getText().toString().trim().isEmpty())
			error += "You must include a date. ";
		if (opponent.getText().toString().trim().isEmpty())
			error += "You must include a opponents.";
		
		// checks to see if location and text are empty
		return  error.isEmpty();
	}
	
	
	// Date selector code
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case 1:
             
            // open datepicker dialog. 
            // set date picker for current date 
            // add pickerListener listner to date picker
        	return new DatePickerDialog(this, pickerListener, 2014, 4, 5);
        }
        return null;
    }
	
 
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
 
        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                int selectedMonth, int selectedDay) {
             
            Integer year  = selectedYear;
            Integer month = selectedMonth;
            Integer day   = selectedDay;
 
            // Show selected date 
            date.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));
     
           }
        };
        
    // Closes activity when you press done on record_stat screen    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
             if (resultCode == RESULT_OK) {
                this.finish();
             }
         }
    }

}
