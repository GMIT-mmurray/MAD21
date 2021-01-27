// Intensive CPU service running its heavy duty task in a 
// parallel thread. Uses Message handling for synchronization
package cis493.demos;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService2 extends Service {
    boolean isRunning = true;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		//super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		//super.onStart(intent, startId);
		Log.e ("<<MyService2-onStart>>", "I am alive-2!");

		//we place the slow work of the service in its own 
		//thread so the response we send our caller who run
		// a "startService(...)" method gets a quick OK from us.
		
		Thread triggerService = new Thread ( new Runnable(){
            long startingTime = System.currentTimeMillis();
            
			public void run() {
				for(int i=0; (i< 120) & isRunning; i++) { 
					try {			
						//fake that you are very busy here	
						long tics = System.currentTimeMillis() - startingTime;
						
						Intent intentGoService2 = new Intent("matos.action.GOSERVICE2");
						
						String msg = i + " value: " + tics;
						intentGoService2.putExtra("serviceData", msg);
						sendBroadcast(intentGoService2);
						Thread.sleep(3000); //three secs.
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}//for
				
			}//run
			
		});
		triggerService.start();

	}//onStart

	@Override
	public void onDestroy() {
		//super.onDestroy();
		Log.e ("<<MyService2-onDestroy>>", "I am dead-2");
		isRunning = false;
	}//onDestroy
	

	
}//MyService2