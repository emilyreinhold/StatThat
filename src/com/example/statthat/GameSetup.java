package com.example.statthat;


import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_setup);
		
		date = (EditText) findViewById(R.id.game_date);
		
		Button datePicker = (Button) findViewById(R.id.date_picker);
		
		datePicker.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				showDialog(1111);
			}
			
			
		});
	
		
		//Submit form
		Button confirm = (Button) findViewById(R.id.confirm);
		confirm.setOnClickListener(new Button.OnClickListener(){  

			@Override
			public void onClick(View v) {
				if(validateFields()){
					
				}else{
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
		
		EditText location = (EditText) findViewById(R.id.game_location);
		EditText date = (EditText) findViewById(R.id.game_date);
		
		if (location.getText().toString().trim().isEmpty())
			error += "You must include a location. ";
		if (date.getText().toString().trim().isEmpty())
			error += "You must include a date. ";
		
		// checks to see if location and text are empty
		return  error.isEmpty();
	}
	
	
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case 1111:
             
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

}
