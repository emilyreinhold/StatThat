package com.example.statthat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NewPlayerActivity extends Activity {
	Team team;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_new_player);
		Bundle extras = this.getIntent().getExtras();
		long team_id = extras.getLong("team");
		team = Team.findById(Team.class, team_id);
		
		Button add = (Button)findViewById(R.id.add_another_player);
		add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(createPlayer()) {
					finish();
					Intent new_player = new Intent(NewPlayerActivity.this, NewPlayerActivity.class);
					new_player.putExtra("team", team.getId());
					startActivity(new_player);
				}
				
			}
		});
		Button done = (Button) findViewById(R.id.done_adding_players);
		done.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(createPlayer()) {
					finish();
				}
				
			}
		});
	}
	
	public boolean textFieldIsNull(int id) {
		EditText textbox = (EditText)findViewById(id);
		String contents = textbox.getText().toString();
		if (contents.isEmpty()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean createPlayer() {
		String toast_message = "";
		EditText playerFirstName = (EditText)findViewById(R.id.PlayerFirstName);
		String first_name = "";
		int count_of_nulls = 0;
		if(textFieldIsNull(R.id.PlayerFirstName)) {
			count_of_nulls++;
			toast_message += "Please enter the player's first name";
		}
		else {
			first_name = playerFirstName.getText().toString();
		}
		
		EditText playerLastName = (EditText)findViewById(R.id.PlayerLastName);
		String last_name = "";
		if(textFieldIsNull(R.id.PlayerLastName)) {
			count_of_nulls++;
			if(count_of_nulls >= 2) {
				toast_message += ", last name";
			}
			else {
				toast_message += "Please enter the player's last name";
			}
		}
		else {
			last_name = playerLastName.getText().toString();
		}
		
		EditText playerNumber = (EditText)findViewById(R.id.PlayerNumber);
		int number = -100;
		if(textFieldIsNull(R.id.PlayerNumber)) {
			count_of_nulls++;
			if(count_of_nulls > 2) {
				toast_message += ", and number.";
			}
			else if(count_of_nulls == 2) {
				toast_message += " and number.";
			}
			else {
				toast_message += "Please enter the player's number.";
			}
		}
		else {
			number = Integer.parseInt(playerNumber.getText().toString());
		}
		
		EditText playerHeightFeet = (EditText)findViewById(R.id.PlayerHeightFeet);
		int feet = 0;
		if(!textFieldIsNull(R.id.PlayerHeightFeet)) {
			feet = Integer.parseInt(playerHeightFeet.getText().toString());
		}
		
		EditText playerHeightInches = (EditText)findViewById(R.id.PlayerHeightInches);
		int inches = 0;
		if(!textFieldIsNull(R.id.PlayerHeightInches)) {
			inches = Integer.parseInt(playerHeightInches.getText().toString());
		}
		
		EditText playerWeight = (EditText)findViewById(R.id.PlayerWeight);
		double weight = 0;
		if(!textFieldIsNull(R.id.PlayerWeight)) {
			weight = Double.parseDouble(playerWeight.getText().toString());
		}

		Spinner years = (Spinner)findViewById(R.id.year_spinner);
		int year_index = years.getSelectedItemPosition();
		
		String year = (String)years.getItemAtPosition(year_index);
		Spinner positions = (Spinner)findViewById(R.id.position_spinner);
		int pos_index = positions.getSelectedItemPosition();
		String position = (String)positions.getItemAtPosition(pos_index);
		
		if(toast_message.isEmpty()) {
			Player p = new Player(getApplicationContext(), first_name, last_name, team, number, position, inches, feet, weight, year);
			p.save();
			return true;
		}
		else {
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(getApplicationContext(), toast_message, duration);
			toast.show();
			return false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_player, menu);
		return true;
	}

}
