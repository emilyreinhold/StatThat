package com.example.statthat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Toast;

public class RecordAction {
	private SpeechRecognizer mSpeechRecognizer;
	private Intent mSpeechRecognizerIntent; 
	private Context ctx;
	
	//
	public RecordAction(Context ctx, String package_name){
		this.ctx = ctx;
		mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(ctx);
	    mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	    mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
	                                     RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	    mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
	                                     package_name);
		
	    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
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
	         mSpeechRecognizer.startListening(mSpeechRecognizerIntent);

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
	    	// TODO call speech parser class
			int duration = Toast.LENGTH_SHORT;
			String msg = "Nothing";
			if(results != null && results.containsKey(SpeechRecognizer.RESULTS_RECOGNITION)){
				msg = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
			}
				
			
			Toast toast = Toast.makeText(ctx, msg, duration);
			toast.show();
	    }

	    @Override
	    public void onRmsChanged(float rmsdB)
	    {

	    }

	}
	
	

}
