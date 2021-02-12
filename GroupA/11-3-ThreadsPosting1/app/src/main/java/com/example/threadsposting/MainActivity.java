package com.example.threadsposting;

// using Handler post(...) method to execute 
// foreground runnables
// --------------------------------------------------
// ProgressBar
// style="?android:attr/progressBarStyleHorizontal"
// style="?android:attr/progressBarStyleSmall"
// style="?android:attr/progressBarStyle"
// style="?android:attr/progressBarStyleLarge"
//--------------------------------------------------

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	ProgressBar myBarHorizontal;
	ProgressBar myBarCircular;

	TextView lblTopCaption;
	EditText txtDataBox;
	Button btnDoSomething;
	Button btnDoItAgain;
	int progressStep = 5;
	final int MAX_PROGRESS = 100;

	int globalVar = 0;
	int accum = 0;

	long startingMills = System.currentTimeMillis();
	boolean isRunning = false;
	String PATIENCE = "Some important data is being collected now. "
			+ "\nPlease be patient...wait...\n ";

	Handler myHandler = new Handler();

	// ///////////////////////////////////////////////////////////////////
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Java-UI wiring
		lblTopCaption = (TextView) findViewById(R.id.lblTopCaption);

		myBarHorizontal = (ProgressBar) findViewById(R.id.myBarHor);
		myBarCircular = (ProgressBar) findViewById(R.id.myBarCir);

		txtDataBox = (EditText) findViewById(R.id.txtBox1);
		txtDataBox.setHint(" Foreground distraction\n Enter some data here...");

		btnDoItAgain = (Button) findViewById(R.id.btnDoItAgain);
		btnDoItAgain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onStart();
			}// onClick
		});// setOnClickListener

		btnDoSomething = (Button) findViewById(R.id.btnDoSomething);
		btnDoSomething.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String txt = txtDataBox.getText().toString();
				// just to prove that we are responsive
				Toast.makeText(MainActivity.this,
						"I'm Quick - You said >> \n" + txt, Toast.LENGTH_LONG).show();
			}// onClick
		});// setOnClickListener

	}// onCreate

	// ///////////////////////////////////////////////////////////////////
	@Override
	protected void onStart() {
		super.onStart();
		// prepare UI components
		txtDataBox.setText("");
		btnDoItAgain.setEnabled(false);

		// reset and show progress bars
		accum = 0;
		myBarHorizontal.setMax(MAX_PROGRESS);
		myBarHorizontal.setProgress(0);
		myBarHorizontal.setVisibility(View.VISIBLE);
		myBarCircular.setVisibility(View.VISIBLE);

		// create background thread were the busy work will be done
		Thread myBackgroundThread = new Thread(backgroundTask, "backAlias1");
		myBackgroundThread.start();
	}

	// FOREGROUND
	// this foreground Runnable works on behave of the background thread,
	// its mission is to update the main UI which is unreachable to back worker
	private Runnable foregroundRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				// update UI, observe globalVar is changed in back thread
				lblTopCaption.setText(PATIENCE + "\nPct progress: " + accum
						+ "  globalVar: " + globalVar);

				// advance ProgressBar
				myBarHorizontal.incrementProgressBy(progressStep);
				accum += progressStep;

				// are we done yet?
				if (accum >= myBarHorizontal.getMax()) {
					lblTopCaption.setText("Slow background work is OVER!");
					myBarHorizontal.setVisibility(View.INVISIBLE);
					myBarCircular.setVisibility(View.INVISIBLE);
					btnDoItAgain.setEnabled(true);
				}
			} catch (Exception e) {
				Log.i("<<foregroundTask>>", e.getMessage());
			}
		}
	}; // foregroundTask

	// ////////////////////////////////////////////////////////////////////
	// BACKGROUND
	// this is the back runnable that executes the slow work
	// ////////////////////////////////////////////////////////////////////
	private Runnable backgroundTask = new Runnable() {
		@Override
		public void run() {
			// busy work goes here...
			try {
				for (int n = 0; n < MAX_PROGRESS/progressStep; n++) {
					// this simulates 1 sec. of busy activity
					Thread.sleep(1000);
					// change a global variable from here...
					globalVar++;
					// try: next two UI operations should NOT work
					//Toast.makeText(getApplication(), "Hi ", 1).show();
					//txtDataBox.setText("Hi ");

					// wake up foregroundRunnable delegate to speak for you
					myHandler.post(foregroundRunnable);
				}
			} catch (InterruptedException e) {
				Log.e("<<foregroundTask>>", e.getMessage());
			}

		}// run
	};// backgroundTask

}// ThreadsPosting

