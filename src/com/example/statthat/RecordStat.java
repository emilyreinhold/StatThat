package com.example.statthat;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class RecordStat extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_stat);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.record_stat, menu);
		return true;
	}

}
