package csu.matos.androidderby;
// MainHandler
// Implements the Android HANDLER class. It provides a
// protected priority queue to background workers in which
// messages and calls to foreground-delegates are appended
// in an asynchronous form. The Handler object is integral 
// part of the main thread and can directly update its GUI 
// -------------------------------------------------------

import java.text.DecimalFormat;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MainHandler extends Handler {

	MainActivity main = null;

	// ------------------------------------------------------------
	public MainHandler(MainActivity main) {
		this.main = main;
	}

	// ------------------------------------------------------------
	@Override
	public void handleMessage(Message msg) {

		// dismember arriving message sent by a HorseThread	
		int threadNo = msg.what;
		int threadClock = msg.arg1;
		int threadJump = msg.arg2;
		Integer threadTrackLocation = (Integer) msg.obj;
		
		// assemble log line - update GUI's TextBox 
		DecimalFormat df = new DecimalFormat("00");
		String newLine = " Horse" + threadNo + ":" 
				+ " Clock: " + df.format(threadClock) 
				+ " Incr:" + threadJump 
				+ " NewPos: " + threadTrackLocation + "\n";

		// update GUI with message coming from background thread
		if (threadNo == 1) {
			main.bar1.setProgress(threadTrackLocation);
			main.txtLocHorse1.setText(" Horse1:  " + threadTrackLocation + "/"
					+ main.MAXDISTANCE);
			
			String text1 = main.txtLog1.getText().toString();
			main.txtLog1.setText(newLine + text1);

		} else {
			main.bar2.setProgress(threadTrackLocation);
			main.txtLocHorse2.setText(" Horse2:  " + threadTrackLocation + "/"
					+ main.MAXDISTANCE);
			
			String text2 = main.txtLog2.getText().toString();
			main.txtLog2.setText(newLine + text2);
		}

		// do we have a winner?
		if (threadTrackLocation >= main.MAXDISTANCE) {
			// this horse has crossed the finish line, now check
			// same time as the winner?  the first finisher?
			if (main.winnerTime == threadClock || main.winnerTime == 0) {

				main.winnerTime = threadClock;
				main.winnerNo += ((main.winnerNo.length() > 0) ? ", " : "")
						+ threadNo;

				main.txtWinnerIs.setText("Horse(s) [ " + main.winnerNo
						+ " ] wins @ Time:" + threadClock);
				

				// put a "Winner!!" line on horse's progress log
				if (threadNo == 1) {
					String text1 = main.txtLog1.getText().toString();
					main.txtLog1.setText(" WINNER !!!\n\n" + text1);

				} else {
					String text2 = main.txtLog2.getText().toString();
					main.txtLog2.setText(" WINNER !!!\n\n" + text2);
				}

			}

		
		}// if finished?
		 

	}// handleMessage
}
