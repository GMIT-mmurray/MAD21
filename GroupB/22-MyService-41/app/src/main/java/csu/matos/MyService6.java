// This is the GPS service. Requests location updates
// in a parallel thread. sends broadcast using filter.
package csu.matos;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class MyService6 extends Service {
	String GPS_FILTER = "matos.action.GPSFIX";
	Thread serviceThread;
	LocationManager lm;
	GPSListener myLocationListener;

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
		// super.onStart(intent, startId);
		Log.e("<<MyGpsService-onStart>>", "I am alive-GPS!");

		// we place the slow work of the service in its own
		// thread so the response we send our caller who run
		// a "startService(...)" method gets a quick OK from us.
		// Thread triggerService = new Thread(new Runnable() {
		serviceThread = new Thread(new Runnable() {
			public void run() {
				getGPSFix_Version1();				
				getGPSFix_Version2();
			}// run

		});
		
		serviceThread.start();

	}// onStart

	// ========================================================
	public void getGPSFix_Version1() {

		    // Get the location manager
		    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		    // work with best provider
		    Criteria criteria = new Criteria();
		    String provider = locationManager.getBestProvider(criteria, false);
		    Location location = locationManager.getLastKnownLocation(provider);
		    if ( location != null ){
		    	// capture location data sent by current provider
				double latitude = location.getLatitude();
				double longitude = location.getLongitude();
				// assemble data bundle to be broadcasted
				Intent myFilteredResponse = new Intent(GPS_FILTER);
				myFilteredResponse.putExtra("latitude", latitude);
				myFilteredResponse.putExtra("longitude", longitude);
				myFilteredResponse.putExtra("provider", provider);
				Log.e(">>GPS_Service<<", provider + " =>Lat:" + latitude + " lon:" + longitude);				
				// send the location data out
				sendBroadcast(myFilteredResponse);
		    }
	}
	
	// =============================================================
	public void getGPSFix_Version2() {
		try {
			Looper.prepare();
			// try to get your GPS location using the
			// LOCATION.SERVIVE provider
			lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

			// This listener will catch and disseminate location updates
			myLocationListener = new GPSListener();
			// define update frequency for GPS readings
			long minTime = 2000; // 2 seconds
			float minDistance = 5; // 5 meter
			// request GPS updates
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime,
					minDistance, myLocationListener);
			Looper.loop();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e("<<MyGpsService-onDestroy>>", "I am dead-GPS");
		try {
			lm.removeUpdates(myLocationListener);
			isRunning = false;
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.getMessage(), 1).show();
		}
	}// onDestroy

	// ///////////////////////////////////////////////////////////////////////
	private class GPSListener implements LocationListener {
		public void onLocationChanged(Location location) {
			// capture location data sent by current provider
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			// assemble data bundle to be broadcasted
			Intent myFilteredResponse = new Intent(GPS_FILTER);
			myFilteredResponse.putExtra("latitude", latitude);
			myFilteredResponse.putExtra("longitude", longitude);
			myFilteredResponse.putExtra("provider", location.getProvider());
			Log.e(">>GPS_Service<<", "Lat:" + latitude + " lon:" + longitude);
			// send the location data out
			sendBroadcast(myFilteredResponse);
		}

		public void onProviderDisabled(String provider) {

		}

		public void onProviderEnabled(String provider) {

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

		}
	};// GPSListener class
		// //////////////////////////////////////////////////////////////////////
}// MyService3