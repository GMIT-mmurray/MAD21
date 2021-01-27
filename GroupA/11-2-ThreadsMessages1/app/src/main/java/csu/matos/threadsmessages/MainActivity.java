package csu.matos.threadsmessages;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {

	ProgressBar bar1;
	ProgressBar bar2;

	TextView msgWorking;
	TextView msgReturned;
	
	ScrollView myScrollView;

	// this is a control var used by backg. threads
	boolean isRunning = false;

	// lifetime (in seconds) for background thread
	final int MAX_SEC = 30;

	// String globalStrTest = "global value seen by all threads ";
	int globalIntTest = 0;
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			String returnedValue = (String) msg.obj;

            String fromThread = (msg.what == 1)? "1" : "2";
			// do something with the value sent by the background thread here
			msgReturned.append("\n returned value from Thread : " + fromThread + returnedValue);
			myScrollView.fullScroll(View.FOCUS_DOWN);

			bar1.setMax(MAX_SEC);
			bar1.incrementProgressBy(1);

			// testing early termination
			if (bar1.getProgress() == MAX_SEC) {
				msgReturned.append(" \nDone \n back thread has been stopped");
				isRunning = false;
			}

			if (bar1.getProgress() == bar1.getMax()) {
				msgWorking.setText("Done");
				bar1.setVisibility(View.INVISIBLE);
				bar2.setVisibility(View.INVISIBLE);
	
			} else {
				msgWorking.setText( "globalIntTest (changed by the back thread) " 
			                      + globalIntTest 
			                      + "\nWorking..." + bar1.getProgress());
			}
		}
	}; // handler

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_main);

		bar1 = (ProgressBar) findViewById(R.id.progress1);
		bar1.setProgress(0);
		bar1.setMax(MAX_SEC);

		bar2 = (ProgressBar) findViewById(R.id.progress2);

		msgWorking = (TextView) findViewById(R.id.txtWorkProgress);
		msgReturned = (TextView) findViewById(R.id.txtReturnedValues);
		
		myScrollView = (ScrollView) findViewById(R.id.myscroller);

		// globalStrTest += "XXX"; // slightly change the global string
		globalIntTest = 1;

	}// onCreate

	public void onStart() {
		super.onStart();
		// this code creates the background activity where busy work is done
		Thread background = new Thread(new Runnable() {
			public void run() {
				try {
					// experiment using/not-using isRunning flag
					for (int i = 0; i < MAX_SEC && isRunning; i++) {
						// try a Toast method here (it will not work!)
						// fake busy busy work here
						Thread.sleep(1000); // one second at a time

						// this is a locally generated value between 0-100
						Random rnd = new Random();
						int localData = (int) rnd.nextInt(101);
						// we can see and change (global) class variables
						String data = "Data-" + globalIntTest + "-" + localData;
						globalIntTest++; 
						// request a message token and put some data in it
						Message msg = handler.obtainMessage(1, (String) data);

						// if thread is still alive send the message
						if (isRunning) {
							handler.sendMessageAtTime(msg, 1000);
							Log.e("Back-Worker", data);
						}
					}
				} catch (Throwable t) {
					// just end the background thread
					isRunning = false;
				}
			}
		});// Tread

		Thread background1 = new Thread(new Runnable() {
			public void run() {
				try {
					// experiment using/not-using isRunning flag
					for (int i = 0; i < MAX_SEC && isRunning; i++) {
						// try a Toast method here (it will not work!)
						// fake busy busy work here
						Thread.sleep(1000); // one second at a time

						// this is a locally generated value between 0-100
						Random rnd = new Random();
						int localData = (int) rnd.nextInt(101);
						// we can see and change (global) class variables
						String data = "Data-" + globalIntTest + "-" + localData;
						globalIntTest++;
						// request a message token and put some data in it
						Message msg = handler.obtainMessage(2, (String) data);

						// if thread is still alive send the message
						if (isRunning) {
							handler.sendMessageAtTime(msg, 1000);
							Log.e("Back-Worker", data);
						}
					}
				} catch (Throwable t) {
					// just end the background thread
					isRunning = false;
				}
			}
		});// Tread

		isRunning = true;
		background.start();
		background1.start();
	}// onStart

	public void onStop() {
		super.onStop();
		//isRunning = false;
	}// onStop
}// class

