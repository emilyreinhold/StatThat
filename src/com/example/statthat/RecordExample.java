package com.example.statthat;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class RecordExample extends Activity {

	private static final int REQUEST_CODE = 1234;
	TextView text;
	TextView text2;
	boolean pressed = false;
	SpeechRecognizer speech;
	Intent speechIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_example);
		
		Button button = (Button) findViewById(R.id.button1);
		text = (TextView) findViewById(R.id.textView1);
		text2 = (TextView) findViewById(R.id.textView2);
		speech = SpeechRecognizer.createSpeechRecognizer(this);
		
		button.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
			}
			
		});
		
		button.setOnTouchListener(new Button.OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent e) {
				switch(e.getAction()){
					case MotionEvent.ACTION_DOWN:
					    startVoiceRecognitionActivity();
					case MotionEvent.ACTION_UP:
						speech.destroy();
				}
				return false;
			}
		});
		
		List<Player> players = Player.listAll(Player.class);
		String output = "";
		for (Player player : players){
			output += "Player: " + Integer.toString(player.number) + "\n";
		}
		List<Player> player = Player.find(Player.class, "number=?", "1");
		output += "Size of players with 1 = " + player.size(); 
		text2.setText(output);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.record_example, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	private void startVoiceRecognitionActivity() {
		  speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		  speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
		    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		  speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,
		    "AndroidBite Voice Recognition...");
//		  startActivityForResult(intent, REQUEST_CODE);
		  speech.startListening(speechIntent);
	}
	
	
	 @Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	   if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
		   String output = "";

		   ArrayList<String> matches = data
	      .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	    	text.setText("");
	   
	    for(String match : matches){
	    	output += "Original: " + match +"\n";
	    	String[] sentence = match.split(" ");
	    	
	    	// Currently everything should have at least 4 items "player", number, action, result.
	    	if(sentence.length > 3){
	    		
	    		Stat stat = new Stat(getApplicationContext());
	    		if(sentence[0].toLowerCase().equals("player")){
	    			try{
	    				List<Player> players = Player.find(Player.class, "number=?", sentence[1]);
	    				if(players.size() >= 1){
	    					try{
		    					Player player = players.get(0);
		    					output += "Player found, num: " + player.number;
		    					stat.player = player;
	    					}catch(Exception e){

	    	    				output += "ERROR at player!! = " + e.toString() + "\n";
	    	    			}
	    					
	    				}else{
	    					output += "Player not found, sentence[1] = " + sentence[1] + "\n";
	    					continue;
	    				}
	    			}catch(NumberFormatException e){
	    				output += "-Failed number parse\n";
	    				continue;
	    			}
	    		
	    		String last = sentence[sentence.length - 1].toLowerCase();
	    		StatType statType = null;
	    		
	    		if(last.equals("hit") || last.equals("miss")){
	    			// Look for action in StatType
	    			statType = new StatType(getApplicationContext());
	    			statType.name = "free throw";
	    			statType.save();
	    			stat.statType = statType;
	    			stat.result = last.equals("hit");
	    			
//	    			for(int i = 2; i < sentence.length - 2; i++){
//	    				List<StatType> statTypes = StatType.findWithQuery(StatType.class, "Select * from StatType where name like ?", sentence[i]);
//	    				if(statTypes.size() > 1){
//	    					try{
//		    					statType = statTypes.get(0);
//		    					stat.statType = statType;
//		    					stat.result = last.equals("hit");
//	    					break;
//	    					}catch(Exception e){
//	    	    				output += "ERROR at statType!! = " + e.toString() + "\n";
//	    	    			}
//	    					
//	    				}
//	    			}
//	    			if(statType == null){
//	    				output += "-Failed to find StatType";
//	    				continue;
//	    			}
	    			
	    			stat.save();
	    			output += "Player exists? " + (stat.player != null) +"\n";
	    			output += "StatType exits? " + (stat.statType != null) + "\n";
	    			output += "Result = " + stat.result + "\n";
	    			
	    			try{
	    			output += "Player: " + Integer.toString(stat.player.number) + "\n" +
	    					  "StatType: " + stat.statType.name + "\n" + 
	    					  "Result: " + stat.result + "\n";
	    			}catch(Exception e){
	    				output += "ERROR at end!! = " + e.toString() + "\n";
	    			}
	    			
	    		}else{output += "-Failed hit/miss\n";}
	    	}
	    	else{ output += "-Failed player\n";}
	    }else{output += "-Failed length\n";}
	   }
	    text.setText(output);

	   super.onActivityResult(requestCode, resultCode, data);
	  }
	}
	 
	 
	 
	 protected class SpeechRecognitionListener implements RecognitionListener
	 {

	     @Override
	     public void onBeginningOfSpeech()
	     {               
	         //Log.d(TAG, "onBeginingOfSpeech"); 
	     }

	     @Override
	     public void onBufferReceived(byte[] buffer)
	     {

	     }

	     @Override
	     public void onEndOfSpeech()
	     {
	         //Log.d(TAG, "onEndOfSpeech");
	      }

	     @Override
	     public void onError(int error)
	     {
	          speech.startListening(speechIntent);

	         //Log.d(TAG, "error = " + error);
	     }

	     @Override
	     public void onEvent(int eventType, Bundle params)
	     {

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
	         //Log.d(TAG, "onResults"); //$NON-NLS-1$
	         ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
	         // matches are the return values of speech recognition engine
	         // Use these values for whatever you wish to do
	     }

	     @Override
	     public void onRmsChanged(float rmsdB)
	     {

	     }

	 }
}
