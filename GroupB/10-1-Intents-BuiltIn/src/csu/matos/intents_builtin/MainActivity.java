package csu.matos.intents_builtin;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.Menu;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// ex01A_ActionCall();
		 ex01B_ActionDial();
		// ex02_ActionWebSearch();
		// ex03_ActionSendTo();
		// ex04_ActionGetContent_Pictures();
		// ex05_ActionViewContacts();
		// ex06_ActionEditContacts();
		// ex07_ActionViewWebPage();
		// ex08_ActionViewMapsLandmarks();
		// ex09_ActionViewMapsCoordinates();
		// ex10_ActionViewMapsDirections();
		// ex11_ActionViewMapsStreetView();
		// ex12_ActionMusicPlayer();
		// ex13_ActionViewMusicSD();
		// ex14_ActionSendSMS();
		// ex15_ActionMain();

	}

	private void ex15_ActionMain() {
			startActivity(new Intent(
				android.provider.Settings.ACTION_INTERNAL_STORAGE_SETTINGS
				));

	}

	private void ex14_ActionSendSMS() {
		// send email 
		String[] emailReceiverList = {"v.matos@csuohio.edu"}; 
		String emailSubject = "Department Meeting";
		String emailText = "We�ll discuss the new project "
		                 + "on Tue. at 9:00am @ room BU344"; 
		
		
		Intent intent = new Intent(Intent.ACTION_SEND); 
		//intent.setType("text/html");
		intent.putExtra(Intent.EXTRA_EMAIL, emailReceiverList);  
		intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject); 
		intent.putExtra(Intent.EXTRA_TEXT, emailText); 
		intent.setType("vnd.android.cursor.dir/email");
		startActivity(Intent.createChooser(intent, 
		                      "To complete action choose:"));

	}

	private void ex13_ActionViewMusicSD() {
		Intent myActivity2 = new Intent(Intent.ACTION_VIEW);
		Uri data = Uri.parse("file://"
				+ Environment.getExternalStorageDirectory()
							 .getAbsolutePath()
				+ "/Music/Bella Luna/Amarcord.mp3");

		myActivity2.setDataAndType(data, "audio/mp3");

		startActivity(myActivity2);

	}

	private void ex12_ActionMusicPlayer() {
		Intent myActivity2 = new Intent("android.intent.action.MUSIC_PLAYER");

		startActivity(myActivity2);

	}

	private void ex11_ActionViewMapsStreetView() {
		String geoCode = "google.streetview:" + "cbll=41.5020952,-81.6789717&"
				+ "cbp=1,270,,45,1&mz=7";
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoCode));
		startActivity(intent);

	}

	private void ex10_ActionViewMapsDirections() {

		Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
				Uri.parse("http://maps.google.com/maps?"
						+ "saddr=9.938083,-84.054430&"
						+ "daddr=9.926392,-84.055964"));
		startActivity(intent);

	}

	private void ex09_ActionViewMapsCoordinates() {

		// map is centered aroung given Lat, Long
		String geoCode = "geo:41.5020952,-81.6789717&z=16";

		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoCode));
		startActivity(intent);

	}

	private void ex08_ActionViewMapsLandmarks() {
		// (you may get multiple results)
		String thePlace = "Cleveland State University, Ohio";
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
				Uri.parse("geo:0,0?q=(" + thePlace + ")"));
		startActivity(intent);

	}

	private void ex07_ActionViewWebPage() {
		String myUriString = "https://www.youtube.com/results?search_query=ping pong";

		Intent myActivity2 = new Intent(Intent.ACTION_VIEW,
				Uri.parse(myUriString));
		startActivity(myActivity2);

	}

	private void ex06_ActionEditContacts() {
		String myData = ContactsContract.Contacts.CONTENT_URI + "/" + "5";

		Intent myActivity2 = new Intent(Intent.ACTION_EDIT, Uri.parse(myData));

		startActivity(myActivity2);

	}

	// --------------------------------------------------------------
	private void ex05_ActionViewContacts() {
		String myData = "content://contacts/people/";
		Intent myActivity2 = new Intent(Intent.ACTION_VIEW, Uri.parse(myData));
		startActivity(myActivity2);

	}

	// --------------------------------------------------------------

	private void ex03_ActionSendTo() {
		Intent intent = new Intent(Intent.ACTION_SENDTO,
				Uri.parse("smsto:5554"));

		intent.putExtra("sms_body", "are we playing golf next Sunday?");

		startActivity(intent);

	}

	private void ex04_ActionGetContent_Pictures() {
		Intent intent = new Intent();

		intent.setType("image/pictures/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);

		startActivity(intent);

	}

	// --------------------------------------------------------------
	private void ex02_ActionWebSearch() {

		Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);

		intent.putExtra(SearchManager.QUERY, "straight hitting golf clubs");

		startActivity(intent);

	}

	// --------------------------------------------------------------
	private void ex01A_ActionCall() {

		String myPhoneNumberUri = "tel:555-1234";
		Intent myActivity2 = new Intent(Intent.ACTION_CALL,
				Uri.parse(myPhoneNumberUri));
		startActivity(myActivity2);

	}

	// --------------------------------------------------------------
	private void ex01B_ActionDial() {

		String myPhoneNumberUri = "tel:555-1234";
		Intent myActivity2 = new Intent(Intent.ACTION_DIAL,
				Uri.parse(myPhoneNumberUri));
		startActivity(myActivity2);

	}

	// --------------------------------------------------------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
