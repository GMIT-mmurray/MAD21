package csu.matos.androidderby;
// HorseThread
// Simulate the 'life' of a race horse: sleep, wake-up and decide
// how far to advance, finish the race, eat, rest, repeat...
// ----------------------------------------------------------------
import java.util.Random;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HorseThread extends Thread {
	int horseNo;
	int localClock;
	int currentTrackPosition = 0;
	Handler mainHandler;
	String horseName = null;
	MainActivity mainThread = null;

	public HorseThread(String horseName, MainActivity mainThread) {
		this.horseName = horseName;
		this.mainThread = mainThread;
		this.horseNo = (horseName.equals("HORSE1")) ? 1 : 2;
	}

	@Override
	public void run() {
		super.run();
		Random randomGenerator = new Random();

		while (true) {
			try {

				if (currentTrackPosition < mainThread.MAXDISTANCE) {
					// not done yet. decide how much to advance
					
					Thread.sleep(1000);
					localClock++;

					// move this horse a bit closer to the finish line
					// calculate next horse's position
					int rndIncrement = randomGenerator.nextInt(4);
					currentTrackPosition += rndIncrement;
					
					// send to main a message describing horse's position
					Message msg = mainThread.mainHandler.obtainMessage(horseNo,
							localClock, rndIncrement, currentTrackPosition);
					msg.sendToTarget();

				} else {
					// this horse already finished, get it ready for next race
					currentTrackPosition = 0;
					localClock = 0;

					// horse waits in this semaphore's queue until RESET
					mainThread.restingSemaphore.acquire();
				}

			} catch (Exception e) {
				Log.e("HorseThread", "Trouble. Horse:" + horseNo + e.getMessage());
			}
		}
	}

}
