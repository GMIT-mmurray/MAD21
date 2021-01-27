package csu.matos.androidderby;
// TimerThread
// Implements the SIMULATION clock (ticks one second at the time).
// its value is shown in the main thread using a delegate runner.
// -------------------------------------------------------------------

import android.util.Log;

public class TimerThread extends Thread {
	//this is the ancestor's name and reference
	MainActivity main;
	String name = "";

	// get name and reference of your ancestor process 
	public TimerThread(String name, MainActivity main) {
		super();
		this.main = main;
		this.name = name;
	}

	@Override
	public void run() {
		while (true) {
			// keep ticking until all horses are resting in the semaphore's queue
			while (main.restingSemaphore.getQueueLength() < main.TOTALHORSES){
				try {
					Thread.sleep(1000);
					// safely update simulation time
					main.clockLock.writeLock().lock();
						main.clock++;
					main.clockLock.writeLock().unlock();

					// call a foreground runnable to update clock-GUI 
					// (showing how to use of runnables instead of messages)	
					main.mainHandler.post(main.clockUpdateRunnable);					
				
				} catch (InterruptedException e) {
					Log.e("TimerThread", e.getMessage() );
				}

			}// while
		}
	}// run

}
