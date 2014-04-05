package com.example.statthat;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GameSetup extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_setup);
		
		//Submit form
		Button confirm = (Button) findViewById(R.id.confirm);
		confirm.setOnClickListener(new Button.OnClickListener(){  

			@Override
			public void onClick(View arg0) {
				if(validateFields()){
					
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
		EditText location = (EditText) findViewById(R.id.game_location);
		EditText date = (EditText) findViewById(R.id.game_date);
		
		// checks to see if location and text are empty
		return location.getText().toString().trim().isEmpty() &&
				date.getText().toString().trim().isEmpty();
	}

}
