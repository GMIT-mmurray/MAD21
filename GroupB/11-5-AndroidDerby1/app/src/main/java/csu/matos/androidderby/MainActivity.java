package csu.matos.androidderby;
//-----------------------------------------------------------------------
// App:		Android-Derby
// Author:	Matos-Grasser
// Date:	Sep. 2014
// Goal:	A fictional horse race is used to demonstrate concurrency
//			control in Android based on traditional Java classes (Threads,
//			ReentrantLocks, Semaphores) and Android's HANDLER class.
// -----------------------------------------------------------------------

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	//GUI controls
	ProgressBar bar1;
	ProgressBar bar2;

	TextView txtLocHorse1;
	TextView txtLocHorse2;

	TextView txtLog1;
	TextView txtLog2;
	TextView txtWinnerIs;
	ScrollView scrollview;
	Button btnReset;
	TextView txtClock;
	
	// ----------------------------------------------------------------------
	// distance (clicks) to be run by the horses
	final int MAXDISTANCE = 10; 
	int TOTALHORSES = 2;
	
	// ----------------------------------------------------------------------
	// global simulation clock stops after both horses had finished
	int clock = 0;
	// ----------------------------------------------------------------------
	
	int winnerTime = 0;
	String winnerNo = "";
	
	// ----------------------------------------------------------------------
	// concurrency controls:
	// arg 'true'=> a 'fair' semaphore respects seniority
	// after a horse crosses the finish line we place it
	// in the waiting queue of this semaphore
	Semaphore restingSemaphore = new Semaphore(0, true);

	//make clockLock mutually exclusive
	ReadWriteLock clockLock = new ReentrantReadWriteLock(true);
	
	// ----------------------------------------------------------------------	
	// background threads & handler
	Handler 	mainHandler;
	HorseThread horse1;
	HorseThread horse2;
	TimerThread timer;
	
	
	// ----------------------------------------------------------------------
	Runnable   clockUpdateRunnable = new Runnable(){
		@Override
		public void run() {
			// this foreground worker will be called 
			// by background thread to continuously 
			// update GUI (uses lock protected var. clock) 
			clockLock.readLock().lock();
				txtClock.setText("Runn. Time: " + clock);
		    clockLock.readLock().unlock();
		
		}
	};
	
	// ---------------------------------------------------------------------
	@Override
	protected void onStop() {
		super.onStop();
		Log.e("MAIN-onStop", "adios...");
	}//onStop

	// ///////////////////////////////////////////////////////////////////////
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// wire-up GUI controls 
		setContentView(R.layout.activity_main);
		scrollview = (ScrollView) findViewById(R.id.ScrollView01);

		bar1 = (ProgressBar) findViewById(R.id.ProgressBar01);
		bar1.setMax(MAXDISTANCE);
		bar1.setProgress(0);

		bar2 = (ProgressBar) findViewById(R.id.ProgressBar02);
		bar2.setMax(MAXDISTANCE);
		bar2.setProgress(0);

		txtLocHorse1 = (TextView) findViewById(R.id.TextView01);
		txtLocHorse2 = (TextView) findViewById(R.id.TextView02);

		txtLog1 = (TextView) findViewById(R.id.EditText01);
		txtLog2 = (TextView) findViewById(R.id.EditText02);
		txtWinnerIs = (TextView) findViewById(R.id.EditText03);

		btnReset = (Button) findViewById(R.id.btnReset);
		btnReset.setOnClickListener(this);
		
		txtClock = (TextView) findViewById(R.id.txtClock);

	}// onCreate

	// ---------------------------------------------------------------------
	@Override
	protected void onStart() {
		super.onStart();

		mainHandler = new MainHandler(this);

		horse1 = new HorseThread("HORSE1", this);
		horse1.start();
		
		horse2 = new HorseThread("HORSE2", this);
		horse2.start();
		
		timer = new TimerThread("TIMER", this);
		timer.start();
			
	}// onStart
	// ---------------------------------------------------------------------
	@Override
	public void onClick(View v) {
		// attend clicking of RESET GUI button 
		if (v.getId() == btnReset.getId()) {
			bar1.setProgress(0);
			bar2.setProgress(0);
			
			txtLocHorse1.setText("0/" + MAXDISTANCE);
			txtLocHorse2.setText("0/" + MAXDISTANCE);
			
			txtLog1.setText(" Horse1 ready...");
			txtLog2.setText(" Horse2 ready...");
			txtWinnerIs.setText(" Winner is...");
			txtClock.setText("Running Time: 0");
			
			winnerTime = 0;
			winnerNo = "";
			try {				
				// reset clock and count of running horses
				clockLock.writeLock().lock();
					clock = 0;
				clockLock.writeLock().unlock();
				
				// all waiting horses (if any) should become active
				restingSemaphore.release(restingSemaphore.getQueueLength());
			} 
			//catch (InterruptedException e) {
			catch (Exception e) {
				Log.e("Main Click", "Troubles! " +  e.getMessage());
			}

		}

	}//onClick

}// MainActivity