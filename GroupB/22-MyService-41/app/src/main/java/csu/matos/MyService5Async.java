package csu.matos;

//------------------------------------------------------------------------
//Intensive CPU service running its heavy duty task in an 
//AsyncTask object. Uses 'Message handling' for synchronization.
//computing Fibonacci numbers between 20 & 50 [ O(2^n) ]


import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class MyService5Async extends Service {
boolean isRunning = true;
Integer localVar;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.e("MyService5Async-Handler", "Handler got from MyService5Async: " + (String)msg.obj);
		}
	};

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
		Log.e ("<<MyService5Async-onStart>>", "I am alive-5Async!");

		// we place the slow work of the service in an AsynTask
		// so the response we send our caller who run
		// a "startService(...)" method gets a quick OK from us.
		
		new ComputeFibonacciRecursivelyTask().execute(20, 50);


	}//onStart
	// ///////////////////////////////////////////////////////////////////////
	// this recursive evaluation of Fibonacci numbers is exponential O(2^n) 
	// for large n values it should be very time-consuming!
	public Integer fibonacci(Integer n){
		if ( n==0 || n==1 )
			return 1;
		else 
			return fibonacci(n-1) + fibonacci(n-2);
	}
	// ////////////////////////////////////////////////////////////////////////
	@Override
	public void onDestroy() {
		//super.onDestroy();
		Log.e ("<<MyService5Async-onDestroy>>", "I am dead-5-Async");
		isRunning = false;
	}//onDestroy
	
	// /////////////////////////////////////////////////////////////////////////////////////////
	public class ComputeFibonacciRecursivelyTask extends AsyncTask <Integer, Integer, Integer> {
		
		@Override
		protected Integer doInBackground(Integer... params) {
			for (int i=params[0]; i<params[1]; i++){
				Integer fibn = fibonacci(i);
				publishProgress(i, fibn);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {			
			super.onProgressUpdate(values);
			
			Intent intentFilter5 = new Intent("matos.action.GOSERVICE5");
			String data = "dataItem-5-fibonacci-AsyncTask" + values[0] + ": " + values[1];
			intentFilter5.putExtra("MyService5DataItem", data);
			sendBroadcast(intentFilter5);
			
			// tell service about work done by back thread
			// (not really needed!!! - we did the broadcasting here)
			Message msg = handler.obtainMessage(5, data);
			handler.sendMessage(msg);
			
		}
	}// ComputeFibonacciRecursivelyTask

}//MyService5

