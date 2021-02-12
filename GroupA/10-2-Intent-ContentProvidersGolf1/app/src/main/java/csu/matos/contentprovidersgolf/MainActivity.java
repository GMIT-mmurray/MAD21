package csu.matos.contentprovidersgolf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	TextView txtMsg;
	EditText txtUri;
	TextView txtProviders;
	Button btnCallActivity2;
	
	Context context;
	String text;
	int duration = Toast.LENGTH_LONG;
	String TAG = "info";

	Uri[] uriProvider = {
			Uri.parse("content://media/external/audio/media"), 
			Uri.parse("content://media/external/images/media"),
			android.provider.ContactsContract.Contacts.CONTENT_URI,
			android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,					
			android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
			android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI
	};
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		context = getApplication();
		
		try {
			
			txtMsg = (TextView) findViewById(R.id.txtMsg);
			txtUri = (EditText) findViewById(R.id.txtProviderOption);
			//txtUri.setText(2); // by default point to Contacts
			// show some examples of built-in content providers
			txtProviders = (TextView) findViewById(R.id.txtProviders);
			for (int i=0; i<uriProvider.length; i++)
				txtProviders.append("\n" + i + "   " 
			                     + uriProvider[i].toString());

			btnCallActivity2 = (Button) findViewById(R.id.btnOption);
			btnCallActivity2.setOnClickListener(new ClickHandler());

		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
		
	}// onCreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class ClickHandler implements OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				// start myActivity2 (a content provider).
				// Tell it that our requestCode (nickname) is 222
				int option = Integer.parseInt(txtUri.getText().toString());

				Intent myActivity2 = new Intent(Intent.ACTION_PICK,
												uriProvider[option]);
				
				Log.i(TAG, "Calling PICK=" + uriProvider[option] );
				
				startActivityForResult(myActivity2, 222);
				dismissKeyboard(v);
				
			} catch (Exception e) {
				Toast.makeText(getBaseContext(), e.getMessage(),
						Toast.LENGTH_LONG).show();
			}
		}// onClick

		
	}// ClickHandler

	private void dismissKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			// use requestCode to find out who is talking to us
			switch (requestCode) {
			case (222): {
				// 222 is our friendly contact-picker activity
				if (resultCode == Activity.RESULT_OK) {
					// it will return an URI that looks like:
					// 		content://contacts/people/ID
					// where ID is the selected contact's ID
					String returnedData = data.getDataString();
					text = "id " + returnedData;
					
					Toast.makeText(context, text, duration).show();
					Log.e(TAG, "Returned-1->" + text);
					
					Log.e(TAG, "Calling VIEW->");
					Intent myAct3 = new Intent(Intent.ACTION_VIEW,
							Uri.parse(returnedData));
					startActivity(myAct3);
					
				} else {
					// user pressed the BACK button to end called activity
					txtMsg.setText("Selection CANCELLED " + requestCode + " "
							+ resultCode);
				}
				break;
			}
			}// switch
		} catch (Exception e) {
			Toast.makeText(context, e.getMessage(), duration).show();
		}
	}// onActivityResult

}// MainActivity






