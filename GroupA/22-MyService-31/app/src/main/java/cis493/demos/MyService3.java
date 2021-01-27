
package cis493.demos;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService3 extends Service {
    boolean isRunning = true;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.e ("<<MyService3-onStart>>", "I am alive-3!");

		Thread serviceThread = new Thread ( new Runnable(){


			public void run() {
				for(int i=0; (i< 120) & isRunning; i++) { 
					try {			
						//fake that you are very busy here	
						Thread.sleep(1000); 
						Intent intentDataForMyClient = new Intent("matos.action.GOSERVICE3");
						String msg = "data-item-" + i;
						intentDataForMyClient.putExtra("service3Data", msg);
						sendBroadcast(intentDataForMyClient);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}//for
				
			}//run
			
		});
		serviceThread.start();

	}//onStart

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e ("<<MyService3-onDestroy>>", "I am Dead-3");
		isRunning = false;
	}//onDestroy
	

	
}//MyService3